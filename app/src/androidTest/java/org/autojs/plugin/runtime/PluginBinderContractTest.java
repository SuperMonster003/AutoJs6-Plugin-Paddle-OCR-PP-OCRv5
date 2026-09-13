package org.autojs.plugin.runtime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import androidx.test.platform.app.InstrumentationRegistry;
import org.autojs.plugin.common.api.PluginInfo;
import org.autojs.plugin.common.api.PluginCapabilityKeys;
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin;
import org.autojs.plugin.paddle.ocr.api.OcrOptions;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

/** Requires a compatible native ABI. These tests execute real AIDL parcels. */
public class PluginBinderContractTest {
    @Test(timeout = 180000) public void discoveryMetadataInferenceInvalidInputAndRebind() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent discovery = new Intent("org.autojs.plugin.PADDLE_OCR").addCategory("paddle-ocr").setPackage(context.getPackageName());
        List<ResolveInfo> services = context.getPackageManager().queryIntentServices(discovery, 0);
        assertEquals(1, services.size());
        ServiceInfo service = services.get(0).serviceInfo;
        assertTrue(service.exported);
        assertEquals("org.autojs.permission.PLUGIN", service.permission);
        Intent wake = new Intent("org.autojs.plugin.action.WAKE").addCategory(Intent.CATEGORY_DEFAULT).setPackage(context.getPackageName());
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(wake, 0);
        assertEquals(1, activities.size());
        assertTrue(activities.get(0).activityInfo.exported);
        assertEquals("org.autojs.permission.PLUGIN", activities.get(0).activityInfo.permission);
        Bundle metadata = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        assertNotNull(metadata.getString("org.autojs.plugin.WAKE_ACTIVITY"));
        for (int repetition = 0; repetition < 2; repetition++) {
            ArrayBlockingQueue<IBinder> connectionResult = new ArrayBlockingQueue<>(1);
            ServiceConnection connection = new ServiceConnection() {
                @Override public void onServiceConnected(ComponentName name, IBinder binder) { connectionResult.add(binder); }
                @Override public void onServiceDisconnected(ComponentName name) {}
            };
            boolean bound = context.bindService(new Intent().setComponent(new ComponentName(service.packageName, service.name)), connection, Context.BIND_AUTO_CREATE);
            assertTrue(bound);
            try {
                IBinder binder = connectionResult.poll(30, TimeUnit.SECONDS);
                assertNotNull(binder);
                assertEquals("org.autojs.plugin.paddle.ocr.api.IOcrPlugin", binder.getInterfaceDescriptor());
                // Force the generated proxy even when instrumentation shares the service process.
                Binder transport = new Binder() {
                    @Override protected boolean onTransact(int code, Parcel input, Parcel output, int flags) throws RemoteException {
                        return binder.transact(code, input, output, flags);
                    }
                };
                IOcrPlugin plugin = IOcrPlugin.Stub.asInterface(transport);
                PluginInfo info = plugin.getInfo();
                PackageInfo installed = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                assertEquals(installed.versionName, info.getVersionName());
                assertEquals(installed.versionCode, info.getVersionCode());
                assertEquals(InstalledPackageIdentity.stringResource(context, "app_name"), info.getName());
                assertEquals(InstalledPackageIdentity.stringResource(context, "plugin_description"), info.getDescription());
                assertArrayEquals(InstalledPackageIdentity.supportedAbis(context), info.getSupportedAbis());
                assertFalse(info.getId().isEmpty());
                assertFalse(info.getEngine().isEmpty());
                assertFalse(info.getVariant().isEmpty());
                assertTrue(info.getCapabilities().getInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION) > 0);
                File image = File.createTempFile("binder-blank-", ".png", context.getCacheDir());
                try {
                    Bitmap bitmap = Bitmap.createBitmap(960, 960, Bitmap.Config.ARGB_8888);
                    new Canvas(bitmap).drawColor(Color.WHITE);
                    try (FileOutputStream stream = new FileOutputStream(image)) { assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)); }
                    bitmap.recycle();
                    try (ParcelFileDescriptor fd = ParcelFileDescriptor.open(image, ParcelFileDescriptor.MODE_READ_ONLY)) {
                        assertNotNull(plugin.detect(fd, new OcrOptions()));
                    }
                    ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                    AtomicReference<Throwable> pipeFailure = new AtomicReference<>();
                    Thread writer = new Thread(() -> {
                        try (InputStream source = new FileInputStream(image);
                             OutputStream target = new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])) {
                            byte[] bytes = new byte[8192];
                            int count;
                            while ((count = source.read(bytes)) != -1) target.write(bytes, 0, count);
                        } catch (Throwable error) { pipeFailure.set(error); }
                    }, "binder-image-pipe");
                    writer.setDaemon(true);
                    writer.start();
                    try (ParcelFileDescriptor fd = pipe[0]) {
                        assertNotNull(plugin.detect(fd, new OcrOptions()));
                    }
                    writer.join(10000);
                    assertFalse("Image pipe writer leaked", writer.isAlive());
                    assertNull(pipeFailure.get());
                    try {
                        plugin.detect(null, new OcrOptions());
                        fail("Null input must be rejected");
                    } catch (RuntimeException | RemoteException expected) {
                        assertNotNull(expected);
                    }
                } finally {
                    assertTrue(image.delete() || !image.exists());
                }
            } finally {
                context.unbindService(connection);
            }
        }
    }
}

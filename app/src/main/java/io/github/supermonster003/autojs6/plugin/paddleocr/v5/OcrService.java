package io.github.supermonster003.autojs6.plugin.paddleocr.v5;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SharedMemory;

import org.autojs.plugin.common.api.PluginCapabilityKeys;
import org.autojs.plugin.common.api.PluginInfo;
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin;
import org.autojs.plugin.paddle.ocr.api.OcrOptions;
import org.autojs.plugin.paddle.ocr.api.OcrResult;
import org.autojs.plugin.paddle.ocr.api.PaddleOcrOptionExtraKeys;
import org.autojs.plugin.paddle.ocr.api.PaddleOcrPluginCapabilityKeys;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class OcrService extends Service {

    private static final String[] SUPPORTED_ABIS = {"arm64-v8a", "armeabi-v7a"};

    private final Object ocrLock = new Object();
    private Object engine;
    private Method recognizeTextMethod;
    private Method detectMethod;

    private final IOcrPlugin.Stub binder = new IOcrPlugin.Stub() {
        @Override
        public PluginInfo getInfo() throws RemoteException {
            PluginInfo info = new PluginInfo();
            info.setName("Paddle OCR (PP-OCRv5)");
            info.setAuthor("SuperMonster003");
            info.setId("paddle-ocr-pp-ocrv5");
            info.setEngine("paddle-ocr");
            info.setVariant("v5");
            info.setVersionName(BuildConfig.VERSION_NAME);
            info.setVersionCode(BuildConfig.VERSION_CODE);
            info.setVersionDate(BuildConfig.VERSION_DATE);
            info.setSupportedAbis(SUPPORTED_ABIS);

            Bundle capabilities = new Bundle();
            capabilities.putInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION, 3835);
            capabilities.putBoolean(PaddleOcrPluginCapabilityKeys.SUPPORTS_RAW_IMAGE, true);
            info.setCapabilities(capabilities);
            return info;
        }

        @Override
        public List<String> recognizeText(ParcelFileDescriptor image, OcrOptions options) throws RemoteException {
            synchronized (ocrLock) {
                Bitmap bitmap = decodeImage(image, options);
                try {
                    return OcrService.this.runRecognizeText(bitmap, options);
                } finally {
                    recycleDecodedBitmap(bitmap);
                }
            }
        }

        @Override
        public List<OcrResult> detect(ParcelFileDescriptor image, OcrOptions options) throws RemoteException {
            synchronized (ocrLock) {
                Bitmap bitmap = decodeImage(image, options);
                try {
                    return OcrService.this.runDetect(bitmap, options);
                } finally {
                    recycleDecodedBitmap(bitmap);
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @SuppressWarnings("unchecked")
    private List<String> runRecognizeText(Bitmap bitmap, OcrOptions options) {
        try {
            return (List<String>) getRecognizeTextMethod().invoke(getEngine(), bitmap, options);
        } catch (ReflectiveOperationException e) {
            throw wrapEngineFailure("recognize text", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<OcrResult> runDetect(Bitmap bitmap, OcrOptions options) {
        try {
            return (List<OcrResult>) getDetectMethod().invoke(getEngine(), bitmap, options);
        } catch (ReflectiveOperationException e) {
            throw wrapEngineFailure("detect text", e);
        }
    }

    private Method getRecognizeTextMethod() {
        getEngine();
        return recognizeTextMethod;
    }

    private Method getDetectMethod() {
        getEngine();
        return detectMethod;
    }

    private Object getEngine() {
        if (engine == null) {
            try {
                ClassLoader loader = getClassLoader();
                Class<?> variantSpecClass = Class.forName("com.baidu.paddle.lite.ocr.VariantSpec", true, loader);
                Class<?> nativeBridgeClass = Class.forName("com.baidu.paddle.lite.ocr.NativeBridge", true, loader);
                Class<?> predictorNativeBridgeClass = Class.forName("com.baidu.paddle.lite.ocr.PredictorNativeBridge", true, loader);
                Class<?> engineClass = Class.forName("com.baidu.paddle.lite.ocr.PaddleOcrEngine", true, loader);
                Object companion = variantSpecClass.getField("Companion").get(null);
                Object variant = companion.getClass().getMethod("v5").invoke(companion);
                Object nativeBridge = predictorNativeBridgeClass.getConstructor().newInstance();
                engine = engineClass
                        .getConstructor(android.content.Context.class, variantSpecClass, nativeBridgeClass)
                        .newInstance(this, variant, nativeBridge);
                recognizeTextMethod = engineClass.getMethod("recognizeText", Bitmap.class, OcrOptions.class);
                detectMethod = engineClass.getMethod("detect", Bitmap.class, OcrOptions.class);
            } catch (ReflectiveOperationException e) {
                throw wrapEngineFailure("initialize Paddle OCR engine", e);
            }
        }
        return engine;
    }

    private RuntimeException wrapEngineFailure(String action, ReflectiveOperationException e) {
        Throwable cause = e instanceof InvocationTargetException
                ? ((InvocationTargetException) e).getTargetException()
                : e;
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        if (cause instanceof Error) {
            throw (Error) cause;
        }
        return new IllegalStateException("Paddle OCR engine failed to " + action, cause);
    }

    private Bitmap decodeImage(ParcelFileDescriptor descriptor, OcrOptions options) {
        Bundle extras = options == null ? null : options.extras;
        boolean useRaw = extras != null && extras.getBoolean(PaddleOcrOptionExtraKeys.RAW_IMAGE, false);
        if (useRaw) {
            int width = extras.getInt(PaddleOcrOptionExtraKeys.RAW_WIDTH, -1);
            int height = extras.getInt(PaddleOcrOptionExtraKeys.RAW_HEIGHT, -1);
            int stride = extras.getInt(PaddleOcrOptionExtraKeys.RAW_STRIDE, width * 4);
            if (width > 0 && height > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    Bitmap decoded;
                    try {
                        decoded = tryDecodeSharedMemory(descriptor, width, height, stride);
                    } catch (Throwable e) {
                        decoded = null;
                    }
                    if (decoded != null) {
                        closeQuietly(descriptor);
                        return decoded;
                    }
                }
                return decodeRawStream(descriptor, width, height, stride);
            }
        }

        try (ParcelFileDescriptor closeable = descriptor) {
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(closeable.getFileDescriptor());
            if (bitmap == null) {
                throw new IllegalStateException("decode image failed");
            }
            return bitmap;
        } catch (IOException e) {
            throw new IllegalStateException("decode image failed", e);
        }
    }

    private void recycleDecodedBitmap(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private Bitmap decodeRawStream(ParcelFileDescriptor descriptor, int width, int height, int stride) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int rowBytes = width * 4;
        int total = stride * height;
        byte[] raw = new byte[total];
        try (ParcelFileDescriptor.AutoCloseInputStream input = new ParcelFileDescriptor.AutoCloseInputStream(descriptor)) {
            int offset = 0;
            while (offset < total) {
                int read = input.read(raw, offset, total - offset);
                if (read < 0) {
                    break;
                }
                offset += read;
            }
            if (offset < total) {
                throw new IllegalStateException("read raw image failed: " + offset + "/" + total);
            }
        } catch (IOException e) {
            throw new IllegalStateException("read raw image failed", e);
        }

        if (stride == rowBytes && bitmap.getRowBytes() == rowBytes) {
            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(raw, 0, rowBytes * height));
        } else {
            ByteBuffer tmp = ByteBuffer.allocate(rowBytes * height);
            int pos = 0;
            for (int row = 0; row < height; row++) {
                tmp.put(raw, pos, rowBytes);
                pos += stride;
            }
            tmp.rewind();
            bitmap.copyPixelsFromBuffer(tmp);
        }
        return bitmap;
    }

    private Bitmap tryDecodeSharedMemory(ParcelFileDescriptor descriptor, int width, int height, int stride) throws IOException {
        ParcelFileDescriptor dup = ParcelFileDescriptor.dup(descriptor.getFileDescriptor());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            closeQuietly(dup);
            return null;
        }

        SharedMemory sharedMemory;
        try {
            sharedMemory = SharedMemory.fromFileDescriptor(dup);
        } catch (Throwable e) {
            closeQuietly(dup);
            return null;
        }

        ByteBuffer buffer;
        try {
            buffer = sharedMemory.mapReadOnly();
        } catch (Throwable e) {
            sharedMemory.close();
            throw new IllegalStateException(e);
        }

        buffer.order(ByteOrder.nativeOrder());
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int rowBytes = width * 4;
            if (stride == rowBytes && bitmap.getRowBytes() == rowBytes) {
                buffer.limit(rowBytes * height);
                bitmap.copyPixelsFromBuffer(buffer);
            } else {
                byte[] packed = new byte[rowBytes * height];
                int srcPos = 0;
                int dstPos = 0;
                while (dstPos < packed.length) {
                    buffer.position(srcPos);
                    buffer.get(packed, dstPos, rowBytes);
                    srcPos += stride;
                    dstPos += rowBytes;
                }
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(packed));
            }
            return bitmap;
        } finally {
            SharedMemory.unmap(buffer);
            sharedMemory.close();
        }
    }

    private static void closeQuietly(ParcelFileDescriptor descriptor) {
        try {
            descriptor.close();
        } catch (Throwable ignored) {
            // Ignore close failures.
        }
    }
}

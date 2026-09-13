package org.autojs.plugin.runtime;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import org.autojs.plugin.common.api.PluginInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipFile;

/** Reads package facts from the installed APK, including single-ABI releases. */
public final class InstalledPackageIdentity {
    private InstalledPackageIdentity() {}

    public static void apply(Context context, PluginInfo info) {
        try {
            PackageInfo installed = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            info.setVersionName(installed.versionName == null ? "" : installed.versionName);
            info.setVersionCode(Build.VERSION.SDK_INT >= 28 ? installed.getLongVersionCode() : installed.versionCode);
            info.setSupportedAbis(supportedAbis(context));
        } catch (PackageManager.NameNotFoundException error) {
            throw new IllegalStateException("The running plugin package cannot be resolved", error);
        }
    }

    public static String[] supportedAbis(Context context) {
        ApplicationInfo app = context.getApplicationInfo();
        List<String> paths = new ArrayList<>();
        paths.add(app.sourceDir);
        if (app.splitSourceDirs != null) Collections.addAll(paths, app.splitSourceDirs);
        List<String> entries = new ArrayList<>();
        for (String path : paths) {
            try (ZipFile apk = new ZipFile(path)) {
                java.util.Enumeration<? extends java.util.zip.ZipEntry> files = apk.entries();
                while (files.hasMoreElements()) entries.add(files.nextElement().getName());
            } catch (IOException error) {
                throw new IllegalStateException("Cannot inspect the installed plugin native inventory", error);
            }
        }
        return nativeAbis(entries);
    }

    public static String[] nativeAbis(Iterable<String> entries) {
        TreeSet<String> result = new TreeSet<>();
        for (String name : entries) {
            if (name != null && name.matches("lib/(arm64-v8a|armeabi-v7a|x86_64|x86)/[^/]+\\.so")) {
                result.add(name.split("/")[1]);
            }
        }
        return result.toArray(new String[0]);
    }

    public static String stringResource(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
        if (id == 0) throw new IllegalStateException("Missing plugin identity resource: " + name);
        return context.getString(id);
    }
}

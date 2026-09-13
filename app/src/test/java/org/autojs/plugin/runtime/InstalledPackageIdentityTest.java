package org.autojs.plugin.runtime;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import java.util.Arrays;

public class InstalledPackageIdentityTest {
    @Test public void reportsOnlyNativeEntriesInThisApk() {
        assertArrayEquals(new String[] {"arm64-v8a", "x86_64"}, InstalledPackageIdentity.nativeAbis(Arrays.asList(
            "lib/arm64-v8a/libengine.so", "lib/x86_64/libengine.so", "lib/arm64-v8a/libother.so",
            "assets/lib/x86/libengine.so", "lib/armeabi-v7a/readme.txt", "lib/x86/nested/libengine.so",
            "lib/unknown/libengine.so", "lib/x86/libengine.so.backup", "", null, "\uD83D\uDE00")));
    }
    @Test public void singleAbiApkDoesNotAdvertiseUniversalSupport() {
        assertArrayEquals(new String[] {"x86"}, InstalledPackageIdentity.nativeAbis(Arrays.asList("lib/x86/libengine.so")));
    }
    @Test public void noNativePayloadIsExplicitlyUnrestricted() {
        assertArrayEquals(new String[0], InstalledPackageIdentity.nativeAbis(Arrays.asList("classes.dex", "AndroidManifest.xml")));
    }
}

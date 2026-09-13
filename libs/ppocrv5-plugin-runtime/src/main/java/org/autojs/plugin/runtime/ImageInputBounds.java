package org.autojs.plugin.runtime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileDescriptor;

/** Allocation limits are checked with long arithmetic before decoding images. */
public final class ImageInputBounds {
    public static final long MAX_PIXELS = 16L * 1024 * 1024;
    public static final long MAX_RAW_BYTES = 64L * 1024 * 1024;
    public static final int MAX_ENCODED_BYTES = 64 * 1024 * 1024;
    private ImageInputBounds() {}

    public static void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0 || (long) width * height > MAX_PIXELS) {
            throw new IllegalArgumentException("Image dimensions must be positive and contain at most 16777216 pixels");
        }
    }

    public static void validateRaw(int width, int height, int stride) {
        validateDimensions(width, height);
        if (stride < (long) width * 4 || (long) stride * height > MAX_RAW_BYTES) {
            throw new IllegalArgumentException("Raw image stride or byte count exceeds the 64 MiB limit");
        }
    }

    /** A host may send a non-seekable pipe: read it once before either decode pass. */
    public static Bitmap decodeEncoded(FileDescriptor descriptor) throws IOException {
        byte[] encoded;
        try (InputStream input = new ParcelFileDescriptor.AutoCloseInputStream(ParcelFileDescriptor.dup(descriptor))) {
            encoded = readAtMost(input, MAX_ENCODED_BYTES);
        }
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(encoded, 0, encoded.length, bounds);
        validateDimensions(bounds.outWidth, bounds.outHeight);
        return BitmapFactory.decodeByteArray(encoded, 0, encoded.length);
    }

    static byte[] readAtMost(InputStream input, int limit) throws IOException {
        if (limit <= 0) throw new IllegalArgumentException("The byte limit must be positive");
        ByteArrayOutputStream output = new ByteArrayOutputStream(Math.min(limit, 8192));
        byte[] buffer = new byte[8192];
        int count;
        while ((count = input.read(buffer)) != -1) {
            if (count > limit - output.size()) throw new IllegalArgumentException("Encoded image exceeds its byte limit");
            output.write(buffer, 0, count);
        }
        return output.toByteArray();
    }
}

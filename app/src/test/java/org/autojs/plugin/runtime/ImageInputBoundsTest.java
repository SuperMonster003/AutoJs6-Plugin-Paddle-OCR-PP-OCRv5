package org.autojs.plugin.runtime;

import org.junit.Test;

public class ImageInputBoundsTest {
    @Test public void acceptsExactMemoryBoundary() { ImageInputBounds.validateRaw(4096, 4096, 16384); }
    @Test public void acceptsPaddedRows() { ImageInputBounds.validateRaw(31, 20, 128); }
    @Test(expected = IllegalArgumentException.class) public void rejectsNegativeDimension() { ImageInputBounds.validateRaw(-1, 5, 20); }
    @Test(expected = IllegalArgumentException.class) public void rejectsZeroDimension() { ImageInputBounds.validateDimensions(1, 0); }
    @Test(expected = IllegalArgumentException.class) public void rejectsIntegerOverflow() { ImageInputBounds.validateRaw(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); }
    @Test(expected = IllegalArgumentException.class) public void rejectsShortStride() { ImageInputBounds.validateRaw(32, 20, 127); }
    @Test(expected = IllegalArgumentException.class) public void rejectsOversizedPaddedBuffer() { ImageInputBounds.validateRaw(1, 100, 1024 * 1024); }
    @Test(expected = IllegalArgumentException.class) public void rejectsOnePixelAboveLimit() { ImageInputBounds.validateDimensions(4097, 4096); }
}

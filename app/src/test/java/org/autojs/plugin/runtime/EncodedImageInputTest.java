package org.autojs.plugin.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class EncodedImageInputTest {
    @Test public void exactByteLimitIsAccepted() throws Exception {
        byte[] bytes = new byte[] {0, 1, -1, 42};
        assertArrayEquals(bytes, ImageInputBounds.readAtMost(new ByteArrayInputStream(bytes), 4));
    }
    @Test(expected = IllegalArgumentException.class) public void byteLimitPlusOneIsRejected() throws Exception {
        ImageInputBounds.readAtMost(new ByteArrayInputStream(new byte[5]), 4);
    }
    @Test public void emptyStreamIsPreservedForInvalidImageDetection() throws Exception {
        assertEquals(0, ImageInputBounds.readAtMost(new ByteArrayInputStream(new byte[0]), 1).length);
    }
    @Test public void fragmentedReadPreservesEveryByte() throws Exception {
        byte[] bytes = new byte[] {-16, -97, -103, -126, 0, 1};
        InputStream fragmented = new ByteArrayInputStream(bytes) {
            @Override public synchronized int read(byte[] target, int offset, int length) {
                return super.read(target, offset, Math.min(length, 1));
            }
        };
        assertArrayEquals(bytes, ImageInputBounds.readAtMost(fragmented, bytes.length));
    }
    @Test(expected = IOException.class) public void ioFailurePropagates() throws Exception {
        ImageInputBounds.readAtMost(new InputStream() {
            @Override public int read() throws IOException { throw new IOException("fixture failure"); }
        }, 4);
    }
}

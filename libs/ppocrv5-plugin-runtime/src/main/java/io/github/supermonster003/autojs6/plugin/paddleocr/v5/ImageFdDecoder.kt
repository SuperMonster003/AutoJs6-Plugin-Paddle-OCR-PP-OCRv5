package io.github.supermonster003.autojs6.plugin.paddleocr.v5

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.SharedMemory
import org.autojs.plugin.paddle.ocr.api.PaddleOcrOptionExtraKeys
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ImageFdDecoder {

    fun decode(imageFd: ParcelFileDescriptor, extras: Bundle?): Bitmap {
        val useRaw = extras?.getBoolean(PaddleOcrOptionExtraKeys.RAW_IMAGE, false) == true
        if (useRaw) {
            val width = extras.getInt(PaddleOcrOptionExtraKeys.RAW_WIDTH, -1)
            val height = extras.getInt(PaddleOcrOptionExtraKeys.RAW_HEIGHT, -1)
            val stride = extras.getInt(PaddleOcrOptionExtraKeys.RAW_STRIDE, width * 4)
            val config = extras.getString(PaddleOcrOptionExtraKeys.RAW_CONFIG)
            if (width > 0 && height > 0 && config == Bitmap.Config.ARGB_8888.name) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val decoded = runCatching {
                        tryDecodeSharedMemory(imageFd, width, height, stride)
                    }.getOrNull()
                    if (decoded != null) {
                        closeQuietly(imageFd)
                        return decoded
                    }
                }
                return decodeRawStream(imageFd, width, height, stride)
            }
        }

        try {
            imageFd.use { closeable ->
                return BitmapFactory.decodeFileDescriptor(closeable.fileDescriptor)
                    ?: error("decode image failed")
            }
        } catch (e: IOException) {
            throw IllegalStateException("decode image failed", e)
        }
    }

    private fun decodeRawStream(
        descriptor: ParcelFileDescriptor,
        width: Int,
        height: Int,
        stride: Int,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rowBytes = width * 4
        val total = stride * height
        val raw = ByteArray(total)
        try {
            ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { input ->
                var offset = 0
                while (offset < total) {
                    val read = input.read(raw, offset, total - offset)
                    if (read < 0) break
                    offset += read
                }
                if (offset < total) {
                    error("read raw image failed: $offset/$total")
                }
            }
        } catch (e: IOException) {
            throw IllegalStateException("read raw image failed", e)
        }

        if (stride == rowBytes && bitmap.rowBytes == rowBytes) {
            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(raw, 0, rowBytes * height))
        } else {
            val packed = ByteBuffer.allocate(rowBytes * height)
            var position = 0
            repeat(height) {
                packed.put(raw, position, rowBytes)
                position += stride
            }
            packed.rewind()
            bitmap.copyPixelsFromBuffer(packed)
        }
        return bitmap
    }

    private fun tryDecodeSharedMemory(
        descriptor: ParcelFileDescriptor,
        width: Int,
        height: Int,
        stride: Int,
    ): Bitmap? {
        val dup = ParcelFileDescriptor.dup(descriptor.fileDescriptor)
        val sharedMemory = try {
            SharedMemory.fromFileDescriptor(dup)
        } catch (_: Throwable) {
            closeQuietly(dup)
            return null
        }

        val buffer = try {
            sharedMemory.mapReadOnly()
        } catch (e: Throwable) {
            sharedMemory.close()
            throw IllegalStateException(e)
        }

        buffer.order(ByteOrder.nativeOrder())
        try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val rowBytes = width * 4
            if (stride == rowBytes && bitmap.rowBytes == rowBytes) {
                buffer.limit(rowBytes * height)
                bitmap.copyPixelsFromBuffer(buffer)
            } else {
                val packed = ByteArray(rowBytes * height)
                var srcPos = 0
                var dstPos = 0
                while (dstPos < packed.size) {
                    buffer.position(srcPos)
                    buffer.get(packed, dstPos, rowBytes)
                    srcPos += stride
                    dstPos += rowBytes
                }
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(packed))
            }
            return bitmap
        } finally {
            SharedMemory.unmap(buffer)
            sharedMemory.close()
        }
    }

    private fun closeQuietly(descriptor: ParcelFileDescriptor) {
        runCatching { descriptor.close() }
    }
}

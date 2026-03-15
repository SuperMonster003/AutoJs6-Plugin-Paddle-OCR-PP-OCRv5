package io.github.supermonster003.autojs6.plugin.paddleocr.v5

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.SharedMemory
import androidx.annotation.RequiresApi
import com.baidu.paddle.lite.ocr.PaddleOcrEngine
import com.baidu.paddle.lite.ocr.VariantSpec
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin
import org.autojs.plugin.paddle.ocr.api.OcrOptions
import org.autojs.plugin.paddle.ocr.api.OcrResult
import org.autojs.plugin.paddle.ocr.api.PluginInfo
import java.nio.ByteBuffer
import java.nio.ByteOrder

class OcrService : Service() {

    private val engine by lazy { PaddleOcrEngine(this, VariantSpec.v5()) }

    private val binder = object : IOcrPlugin.Stub() {

        override fun getInfo(): PluginInfo {
            return PluginInfo().apply {
                name = "Paddle OCR (PP-OCRv5)"
                author = "SuperMonster003"
                id = "paddle-ocr-pp-ocrv5"
                engine = "paddle-ocr"
                variant = "v5"
                description = getString(R.string.plugin_description)
                versionName = BuildConfig.VERSION_NAME
                versionCode = BuildConfig.VERSION_CODE.toLong()
                versionDate = BuildConfig.VERSION_DATE
                capabilities = android.os.Bundle().apply {
                    putBoolean("supportsCls", true)
                    putBoolean("supportsRawImage", true)
                    putString("labels", "labels/ppocr_keys_ocrv5.txt")
                }
            }
        }

        override fun recognizeText(
            image: ParcelFileDescriptor,
            options: OcrOptions,
        ): List<String> {
            val bmp = decodeImage(image, options)
            return engine.recognizeText(bmp, options)
        }

        override fun detect(
            image: ParcelFileDescriptor,
            options: OcrOptions,
        ): List<OcrResult> {
            val bmp = decodeImage(image, options)
            return engine.detect(bmp, options)
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun decodeImage(descriptor: ParcelFileDescriptor, options: OcrOptions): Bitmap {
        val extras = options.extras
        val useRaw = extras?.getBoolean(EXTRA_RAW_IMAGE, false) == true
        if (useRaw) {
            val width = extras.getInt(EXTRA_RAW_WIDTH, -1)
            val height = extras.getInt(EXTRA_RAW_HEIGHT, -1)
            val stride = extras.getInt(EXTRA_RAW_STRIDE, width * 4)
            if (width > 0 && height > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    val decoded = runCatching { tryDecodeSharedMemory(descriptor, width, height, stride) }.getOrNull()
                    if (decoded != null) {
                        runCatching { descriptor.close() }
                        return decoded
                    }
                }
                return decodeRawStream(descriptor, width, height, stride)
            }
        }
        return descriptor.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }
            ?: error("decode image failed")
    }

    private fun decodeRawStream(descriptor: ParcelFileDescriptor, width: Int, height: Int, stride: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rowBytes = width * 4
        val total = stride * height
        val raw = ByteArray(total)
        ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { input ->
            var offset = 0
            while (offset < total) {
                val read = input.read(raw, offset, total - offset)
                if (read < 0) break
                offset += read
            }
            if (offset < total) {
                throw IllegalStateException("read raw image failed: $offset/$total")
            }
        }
        if (stride == rowBytes && bitmap.rowBytes == rowBytes) {
            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(raw, 0, rowBytes * height))
        } else {
            val tmp = ByteBuffer.allocate(rowBytes * height)
            var pos = 0
            repeat(height) {
                tmp.put(raw, pos, rowBytes)
                pos += stride
            }
            tmp.rewind()
            bitmap.copyPixelsFromBuffer(tmp)
        }
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun tryDecodeSharedMemory(descriptor: ParcelFileDescriptor, width: Int, height: Int, stride: Int): Bitmap? {
        val dup = runCatching { ParcelFileDescriptor.dup(descriptor.fileDescriptor) }.getOrNull() ?: return null
        val shm = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                SharedMemory.fromFileDescriptor(dup)
            } else {
                return null
            }
        } catch (_: Throwable) {
            runCatching { dup.close() }
            return null
        }
        val buffer = shm.mapReadOnly()
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
            shm.close()
        }
    }

    companion object {
        private const val EXTRA_RAW_IMAGE = "rawImage"
        private const val EXTRA_RAW_WIDTH = "rawWidth"
        private const val EXTRA_RAW_HEIGHT = "rawHeight"
        private const val EXTRA_RAW_STRIDE = "rawStride"
    }
}

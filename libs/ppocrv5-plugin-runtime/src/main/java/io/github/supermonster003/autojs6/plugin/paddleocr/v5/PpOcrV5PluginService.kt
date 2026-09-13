package io.github.supermonster003.autojs6.plugin.paddleocr.v5

import android.app.Service
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.paddle.ocr.model.OCRRunResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.common.api.PluginInfo
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin
import org.autojs.plugin.paddle.ocr.api.OcrOptions
import org.autojs.plugin.paddle.ocr.api.OcrResult
import org.autojs.plugin.paddle.ocr.api.PaddleOcrPluginCapabilityKeys
import com.paddle.ocr.model.OCRResult as PaddleResult

class PpOcrV5PluginService : Service() {

    private val ocrLock = Any()
    private lateinit var engineHolder: PpOcrV5EngineHolder

    override fun onCreate() {
        super.onCreate()
        engineHolder = PpOcrV5EngineHolder(this)
    }

    override fun onDestroy() {
        engineHolder.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private val binder = object : IOcrPlugin.Stub() {
        override fun getInfo(): PluginInfo {
            val runtimeConfig = PpOcrV5RuntimeConfig.from(this@PpOcrV5PluginService)
            val profile = PpOcrV5Profile.fromConfig(runtimeConfig)
            return PluginInfo().apply {
                name = org.autojs.plugin.runtime.InstalledPackageIdentity.stringResource(this@PpOcrV5PluginService, "app_name")
                description = org.autojs.plugin.runtime.InstalledPackageIdentity.stringResource(this@PpOcrV5PluginService, "plugin_description")
                author = runtimeConfig.pluginAuthor
                id = runtimeConfig.pluginId
                engine = runtimeConfig.pluginEngine
                variant = runtimeConfig.pluginVariant
                versionName = runtimeConfig.versionName
                versionCode = runtimeConfig.versionCode
                versionDate = runtimeConfig.versionDate
                org.autojs.plugin.runtime.InstalledPackageIdentity.apply(this@PpOcrV5PluginService, this)
                capabilities = Bundle().apply {
                    putInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION, 3835)
                    putBoolean(PaddleOcrPluginCapabilityKeys.SUPPORTS_RAW_IMAGE, true)
                    putBoolean(CAPABILITY_SUPPORTS_PP_OCR_V5, true)
                    putBoolean(CAPABILITY_SUPPORTS_TEXT_DETECTION, true)
                    putBoolean(CAPABILITY_SUPPORTS_TEXT_RECOGNITION, true)
                    putBoolean(CAPABILITY_SUPPORTS_TEXTLINE_ORIENTATION_CLASSIFICATION, false)
                    putString(CAPABILITY_MODEL_PROFILE, profile.value)
                    putBoolean(CAPABILITY_RECOMMENDED_FOR_MOBILE_DEFAULT, profile.recommendedForMobileDefault)
                }
            }
        }

        override fun recognizeText(
            imageFd: ParcelFileDescriptor,
            options: OcrOptions,
        ): MutableList<String> {
            return detect(imageFd, options)
                .map { it.text.orEmpty() }
                .toMutableList()
        }

        override fun detect(
            imageFd: ParcelFileDescriptor,
            options: OcrOptions,
        ): MutableList<OcrResult> {
            synchronized(ocrLock) {
                val bitmap = ImageFdDecoder.decode(imageFd, options.extras)
                try {
                    val engine = engineHolder.get(options)
                    val runResult = runBlocking(Dispatchers.IO) {
                        engine.recognize(bitmap)
                    }
                    return runResult.results
                        .map { it.toAutoJsResult(runResult) }
                        .toMutableList()
                } finally {
                    if (!bitmap.isRecycled) {
                        bitmap.recycle()
                    }
                }
            }
        }
    }

    private fun PaddleResult.toAutoJsResult(runResult: OCRRunResult): OcrResult {
        val xs = box.points.map { it.x }
        val ys = box.points.map { it.y }
        return OcrResult().apply {
            text = this@toAutoJsResult.text
            confidence = this@toAutoJsResult.confidence
            bounds = Rect(
                xs.minOrNull()?.toInt() ?: 0,
                ys.minOrNull()?.toInt() ?: 0,
                xs.maxOrNull()?.toInt() ?: 0,
                ys.maxOrNull()?.toInt() ?: 0,
            )
            extras = Bundle().apply {
                putFloatArray(
                    "quad",
                    this@toAutoJsResult.box.points.flatMap { point ->
                        listOf(point.x, point.y)
                    }.toFloatArray(),
                )
                putLong("detectionTimeMs", runResult.detectionTimeMs)
                putLong("recognitionTimeMs", runResult.recognitionTimeMs)
                putLong("totalTimeMs", runResult.totalTimeMs)
                putLong("coldLoadTimeMs", runResult.coldLoadTimeMs)
            }
        }
    }

    private companion object {
        val SUPPORTED_ABIS = arrayOf("arm64-v8a", "armeabi-v7a")
        const val CAPABILITY_SUPPORTS_PP_OCR_V5 = "supportsPpOcrV5"
        const val CAPABILITY_MODEL_PROFILE = "modelProfile"
        const val CAPABILITY_RECOMMENDED_FOR_MOBILE_DEFAULT = "recommendedForMobileDefault"
        const val CAPABILITY_SUPPORTS_TEXT_DETECTION = "supportsTextDetection"
        const val CAPABILITY_SUPPORTS_TEXT_RECOGNITION = "supportsTextRecognition"
        const val CAPABILITY_SUPPORTS_TEXTLINE_ORIENTATION_CLASSIFICATION = "supportsTextLineOrientationClassification"
    }
}

package io.github.supermonster003.autojs6.plugin.paddleocr.v5

import android.content.Context
import android.os.Bundle
import com.paddle.ocr.EngineConfig
import com.paddle.ocr.PaddleOCR
import com.paddle.ocr.PaddleOCRConfig
import com.paddle.ocr.util.OpenCVUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.autojs.plugin.paddle.ocr.api.OcrOptions

class PpOcrV5EngineHolder(context: Context) {

    private val appContext = context.applicationContext
    private val runtimeConfig = PpOcrV5RuntimeConfig.from(appContext)
    private val lock = Any()

    @Volatile
    private var cachedKey: EngineKey? = null

    @Volatile
    private var cachedEngine: PaddleOCR? = null

    fun get(options: OcrOptions): PaddleOCR {
        val profile = PpOcrV5Profile.fromConfig(runtimeConfig)
        val extras = options.extras ?: Bundle().also { options.extras = it }
        val key = EngineKey(
            profile = profile,
            threads = options.cpuThreadNum.coerceAtLeast(1),
            detLimitSideLen = resolveDetLimitSideLen(extras),
            detLimitType = extras.getString(EXTRA_DET_LIMIT_TYPE, "min") ?: "min",
            detMaxSideLimit = resolveDetMaxSideLimit(options, extras),
            detThresh = extras.getFloat(EXTRA_DET_THRESH, 0.3f),
            detBoxThresh = extras.getFloat(EXTRA_DET_BOX_THRESH, 0.6f),
            detUnclipRatio = extras.getFloat(EXTRA_DET_UNCLIP_RATIO, 1.5f),
            recScoreThresh = if (options.scoreThreshold >= 0f) options.scoreThreshold else 0f,
            recBatchSize = extras.getInt(EXTRA_REC_BATCH_SIZE, 1).coerceAtLeast(1),
        )

        cachedEngine?.let { engine ->
            if (cachedKey == key) return engine
        }

        synchronized(lock) {
            cachedEngine?.let { engine ->
                if (cachedKey == key) return engine
            }

            val old = cachedEngine
            val created = runBlocking(Dispatchers.IO) {
                old?.release()
                check(OpenCVUtils.init(appContext)) {
                    "Failed to initialize OpenCV for Paddle OCR PP-OCRv5: ${OpenCVUtils.lastError.orEmpty()}"
                }
                PaddleOCR.create(
                    context = appContext,
                    config = PaddleOCRConfig(
                        detLimitSideLen = key.detLimitSideLen,
                        detLimitType = key.detLimitType,
                        detMaxSideLimit = key.detMaxSideLimit,
                        detThresh = key.detThresh,
                        detBoxThresh = key.detBoxThresh,
                        detUnclipRatio = key.detUnclipRatio,
                        recScoreThresh = key.recScoreThresh,
                        recBatchSize = key.recBatchSize,
                    ),
                    engineConfig = EngineConfig(numThreads = key.threads),
                    detModelAssetPath = profile.detAssetPath,
                    recModelAssetPath = profile.recAssetPath,
                    recConfigAssetPath = profile.recConfigAssetPath,
                )
            }

            cachedKey = key
            cachedEngine = created
            return created
        }
    }

    fun release() {
        synchronized(lock) {
            val engine = cachedEngine
            cachedKey = null
            cachedEngine = null
            if (engine != null) {
                runBlocking(Dispatchers.IO) {
                    engine.release()
                }
            }
        }
    }

    private fun resolveDetLimitSideLen(extras: Bundle): Int {
        val explicit = extras.getInt(EXTRA_DET_LIMIT_SIDE_LEN, 0)
        return if (explicit > 0) explicit else 64
    }

    private fun resolveDetMaxSideLimit(options: OcrOptions, extras: Bundle): Int {
        val explicit = extras.getInt(EXTRA_DET_MAX_SIDE_LIMIT, 0)
        if (explicit > 0) return explicit
        return options.detLongSize.takeIf { it > 0 } ?: 4000
    }

    private data class EngineKey(
        val profile: PpOcrV5Profile,
        val threads: Int,
        val detLimitSideLen: Int,
        val detLimitType: String,
        val detMaxSideLimit: Int,
        val detThresh: Float,
        val detBoxThresh: Float,
        val detUnclipRatio: Float,
        val recScoreThresh: Float,
        val recBatchSize: Int,
    )

    private companion object {
        const val EXTRA_DET_LIMIT_SIDE_LEN = "detLimitSideLen"
        const val EXTRA_DET_LIMIT_TYPE = "detLimitType"
        const val EXTRA_DET_MAX_SIDE_LIMIT = "detMaxSideLimit"
        const val EXTRA_DET_THRESH = "detThresh"
        const val EXTRA_DET_BOX_THRESH = "detBoxThresh"
        const val EXTRA_DET_UNCLIP_RATIO = "detUnclipRatio"
        const val EXTRA_REC_BATCH_SIZE = "recBatchSize"
    }
}

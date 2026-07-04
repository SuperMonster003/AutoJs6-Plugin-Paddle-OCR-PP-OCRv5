package io.github.supermonster003.autojs6.plugin.paddleocr.v5

enum class PpOcrV5Profile(
    val value: String,
    val detAssetPath: String,
    val recAssetPath: String,
    val recConfigAssetPath: String,
    val recommendedForMobileDefault: Boolean,
) {
    MOBILE(
        value = "mobile",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-mobile/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-mobile/rec/inference.yml",
        recommendedForMobileDefault = true,
    ),
    SERVER(
        value = "server",
        detAssetPath = "models/ppocrv5-server/det/inference.onnx",
        recAssetPath = "models/ppocrv5-server/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-server/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ENGLISH(
        value = "english",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-english/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-english/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    KOREAN(
        value = "korean",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-korean/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-korean/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    LATIN(
        value = "latin",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-latin/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-latin/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ESLAV(
        value = "eslav",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-eslav/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-eslav/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    THAI(
        value = "thai",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-thai/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-thai/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    GREEK(
        value = "greek",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-greek/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-greek/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ARABIC(
        value = "arabic",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-arabic/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-arabic/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    CYRILLIC(
        value = "cyrillic",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-cyrillic/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-cyrillic/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    DEVANAGARI(
        value = "devanagari",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-devanagari/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-devanagari/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    TELUGU(
        value = "telugu",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-telugu/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-telugu/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    TAMIL(
        value = "tamil",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-tamil/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-tamil/rec/inference.yml",
        recommendedForMobileDefault = false,
    );

    companion object {
        internal fun fromConfig(config: PpOcrV5RuntimeConfig): PpOcrV5Profile {
            return values().firstOrNull { it.value == config.modelProfile } ?: MOBILE
        }
    }
}

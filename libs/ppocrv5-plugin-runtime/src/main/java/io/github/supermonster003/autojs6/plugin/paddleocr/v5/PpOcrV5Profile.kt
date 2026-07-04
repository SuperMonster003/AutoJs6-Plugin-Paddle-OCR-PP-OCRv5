package io.github.supermonster003.autojs6.plugin.paddleocr.v5

enum class PpOcrV5Profile(
    val value: String,
    val detAssetPath: String,
    val recAssetPath: String,
    val recConfigAssetPath: String,
    val recommendedForMobileDefault: Boolean,
) {
    MOBILE(
        value = "Mobile",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-mobile/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-mobile/rec/inference.yml",
        recommendedForMobileDefault = true,
    ),
    SERVER(
        value = "Server",
        detAssetPath = "models/ppocrv5-server/det/inference.onnx",
        recAssetPath = "models/ppocrv5-server/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-server/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ENGLISH(
        value = "English",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-english/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-english/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    KOREAN(
        value = "Korean",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-korean/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-korean/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    LATIN(
        value = "Latin",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-latin/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-latin/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ESLAV(
        value = "Eslav",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-eslav/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-eslav/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    THAI(
        value = "Thai",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-thai/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-thai/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    GREEK(
        value = "Greek",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-greek/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-greek/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    ARABIC(
        value = "Arabic",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-arabic/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-arabic/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    CYRILLIC(
        value = "Cyrillic",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-cyrillic/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-cyrillic/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    DEVANAGARI(
        value = "Devanagari",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-devanagari/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-devanagari/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    TELUGU(
        value = "Telugu",
        detAssetPath = "models/ppocrv5-mobile-det/det/inference.onnx",
        recAssetPath = "models/ppocrv5-telugu/rec/inference.onnx",
        recConfigAssetPath = "models/ppocrv5-telugu/rec/inference.yml",
        recommendedForMobileDefault = false,
    ),
    TAMIL(
        value = "Tamil",
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

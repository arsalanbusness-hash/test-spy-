package com.example.localization

enum class Language(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val flagEmoji: String,
    val isRtl: Boolean = false
) {
    ENGLISH("en", "English", "English", "🇺🇸", false),
    PERSIAN("fa", "فارسی", "Persian", "🇮🇷", true),
    FRENCH("fr", "Français", "French", "🇫🇷", false),
    SPANISH("es", "Español", "Spanish", "🇪🇸", false),
    GERMAN("de", "Deutsch", "German", "🇩🇪", false),
    ARABIC("ar", "العربية", "Arabic", "🇸🇦", true),
    TURKISH("tr", "Türkçe", "Turkish", "🇹🇷", false),
    RUSSIAN("ru", "Русский", "Russian", "🇷🇺", false),
    ITALIAN("it", "Italiano", "Italian", "🇮🇹", false),
    PORTUGUESE("pt", "Português", "Portuguese", "🇵🇹", false),
    JAPANESE("ja", "日本語", "Japanese", "🇯🇵", false),
    KOREAN("ko", "한국어", "Korean", "🇰🇷", false),
    CHINESE("zh", "中文", "Chinese", "🇨🇳", false);

    companion object {
        fun fromCode(code: String): Language {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

package com.example.dictionary

import com.example.localization.Language
import com.example.localization.StringKey
import com.example.localization.StringsProvider

enum class Category(val stringKey: StringKey, val icon: String) {
    ANIMALS(StringKey.CAT_ANIMALS, "🦁"),
    FOOD(StringKey.CAT_FOOD, "🍕"),
    COUNTRIES(StringKey.CAT_COUNTRIES, "🌍"),
    CITIES(StringKey.CAT_CITIES, "🏙️"),
    OBJECTS(StringKey.CAT_OBJECTS, "📦"),
    TECHNOLOGY(StringKey.CAT_TECHNOLOGY, "💻"),
    VIDEO_GAMES(StringKey.CAT_VIDEOGAMES, "🎮"),
    MOVIES(StringKey.CAT_MOVIES, "🎬"),
    SPORTS(StringKey.CAT_SPORTS, "⚽"),
    CELEBRITIES(StringKey.CAT_CELEBRITIES, "⭐"),
    VEHICLES(StringKey.CAT_VEHICLES, "🚗"),
    NATURE(StringKey.CAT_NATURE, "🌲"),
    JOBS(StringKey.CAT_JOBS, "💼"),
    SCHOOL(StringKey.CAT_SCHOOL, "🎓"),
    RANDOM(StringKey.CAT_RANDOM, "🎲");

    val displayNameKey: StringKey get() = stringKey

    fun getDisplayName(language: Language): String {
        return StringsProvider.get(stringKey, language)
    }

    companion object {
        fun playableCategories(): List<Category> = entries.filter { it != RANDOM }
    }
}

enum class Difficulty(val stringKey: StringKey) {
    EASY(StringKey.DIFF_EASY),
    MEDIUM(StringKey.DIFF_MEDIUM),
    HARD(StringKey.DIFF_HARD),
    EXTREME(StringKey.DIFF_EXTREME);

    val displayNameKey: StringKey get() = stringKey

    fun getDisplayName(language: Language): String {
        return StringsProvider.get(stringKey, language)
    }
}

enum class WordLengthFilter(val stringKey: StringKey) {
    ANY(StringKey.LENGTH_ANY),
    SHORT(StringKey.LENGTH_SHORT),
    MEDIUM(StringKey.LENGTH_MEDIUM),
    LONG(StringKey.LENGTH_LONG);

    val displayNameKey: StringKey get() = stringKey

    fun getDisplayName(language: Language): String {
        return StringsProvider.get(stringKey, language)
    }
}

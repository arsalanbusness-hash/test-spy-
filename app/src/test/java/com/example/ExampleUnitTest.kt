package com.example

import com.example.dictionary.Category
import com.example.dictionary.Difficulty
import com.example.dictionary.WordDictionaryEngine
import com.example.dictionary.WordLengthFilter
import com.example.localization.Language
import com.example.localization.StringKey
import com.example.localization.StringsProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testLanguageRtlProperties() {
        assertTrue(Language.PERSIAN.isRtl)
        assertTrue(Language.ARABIC.isRtl)
        assertFalse(Language.ENGLISH.isRtl)
        assertFalse(Language.SPANISH.isRtl)
    }

    @Test
    fun testStringsProviderPersian() {
        val persianSpy = StringsProvider.get(StringKey.YOU_ARE_SPY, Language.PERSIAN)
        assertTrue(persianSpy.contains("جاسوس"))

        val innocent = StringsProvider.get(StringKey.YOU_ARE_INNOCENT, Language.PERSIAN)
        assertTrue(innocent.contains("بی‌گناه"))

        val secretWord = StringsProvider.get(StringKey.SECRET_WORD_IS, Language.PERSIAN)
        assertEquals("کلمه مخفی", secretWord)
    }

    @Test
    fun testWordDictionaryEngineGeneratesPersianAndEnglishWords() {
        val englishWord = WordDictionaryEngine.getRandomWord(
            language = Language.ENGLISH,
            categories = setOf(Category.FOOD),
            difficulty = Difficulty.EASY,
            lengthFilter = WordLengthFilter.ANY
        )
        assertNotNull(englishWord.word)
        assertTrue(englishWord.word.isNotBlank())
        assertEquals(Category.FOOD, englishWord.category)

        val persianWord = WordDictionaryEngine.getRandomWord(
            language = Language.PERSIAN,
            categories = setOf(Category.ANIMALS),
            difficulty = Difficulty.MEDIUM,
            lengthFilter = WordLengthFilter.ANY
        )
        assertNotNull(persianWord.word)
        assertTrue(persianWord.word.isNotBlank())
        assertEquals(Category.ANIMALS, persianWord.category)
    }
}

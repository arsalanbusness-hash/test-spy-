package com.example.data

import com.example.data.dao.CustomWordDao
import com.example.data.dao.GameHistoryDao
import com.example.data.entity.CustomWord
import com.example.data.entity.GameHistory
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val customWordDao: CustomWordDao,
    private val gameHistoryDao: GameHistoryDao
) {
    val allCustomWords: Flow<List<CustomWord>> = customWordDao.getAllWords()
    val allGameHistory: Flow<List<GameHistory>> = gameHistoryDao.getAllHistory()
    val totalGamesCount: Flow<Int> = gameHistoryDao.getTotalGamesCount()
    val innocentWinsCount: Flow<Int> = gameHistoryDao.getInnocentWinsCount()
    val spyWinsCount: Flow<Int> = gameHistoryDao.getSpyWinsCount()

    suspend fun getCustomWordStrings(): List<String> = customWordDao.getWordStrings()

    suspend fun insertCustomWord(word: String, category: String, languageCode: String): Long {
        return customWordDao.insert(
            CustomWord(
                word = word.trim(),
                categoryName = category.trim(),
                languageCode = languageCode
            )
        )
    }

    suspend fun deleteCustomWord(id: Long) {
        customWordDao.deleteById(id)
    }

    suspend fun recordGame(
        secretWord: String,
        category: String,
        winner: String,
        spyNames: List<String>,
        innocentNames: List<String>,
        accusedName: String,
        wasSpyCaught: Boolean,
        spyGuessedWord: Boolean,
        durationSeconds: Int
    ): Long {
        val history = GameHistory(
            secretWord = secretWord,
            category = category,
            winner = winner,
            spyNames = spyNames.joinToString(", "),
            innocentNames = innocentNames.joinToString(", "),
            accusedName = accusedName,
            wasSpyCaught = wasSpyCaught,
            spyGuessedWord = spyGuessedWord,
            durationSeconds = durationSeconds
        )
        return gameHistoryDao.insert(history)
    }

    suspend fun clearHistory() {
        gameHistoryDao.clearHistory()
    }
}

package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_words")
data class CustomWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val categoryName: String,
    val languageCode: String = "en",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_history")
data class GameHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val secretWord: String,
    val category: String,
    val winner: String, // "INNOCENT" or "SPY"
    val spyNames: String, // comma separated
    val innocentNames: String, // comma separated
    val accusedName: String,
    val wasSpyCaught: Boolean,
    val spyGuessedWord: Boolean,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

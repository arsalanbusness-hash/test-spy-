package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.CustomWord
import com.example.data.entity.GameHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomWordDao {
    @Query("SELECT * FROM custom_words ORDER BY timestamp DESC")
    fun getAllWords(): Flow<List<CustomWord>>

    @Query("SELECT word FROM custom_words")
    suspend fun getWordStrings(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: CustomWord): Long

    @Query("DELETE FROM custom_words WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM custom_words")
    suspend fun deleteAll()
}

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<GameHistory>>

    @Query("SELECT COUNT(*) FROM game_history")
    fun getTotalGamesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_history WHERE winner = 'INNOCENT'")
    fun getInnocentWinsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_history WHERE winner = 'SPY'")
    fun getSpyWinsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: GameHistory): Long

    @Query("DELETE FROM game_history")
    suspend fun clearHistory()
}

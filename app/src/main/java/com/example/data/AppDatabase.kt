package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.CustomWordDao
import com.example.data.dao.GameHistoryDao
import com.example.data.entity.CustomWord
import com.example.data.entity.GameHistory

@Database(entities = [CustomWord::class, GameHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customWordDao(): CustomWordDao
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spy_game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

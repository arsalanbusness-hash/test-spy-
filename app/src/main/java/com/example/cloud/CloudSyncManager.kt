package com.example.cloud

import android.content.Context
import android.util.Log
import com.example.data.GameRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class HostProfile(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val totalGamesHosted: Int = 0,
    val lastSyncTime: Long = System.currentTimeMillis()
)

class CloudSyncManager(private val context: Context) {

    private val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            false
        }

    fun getCurrentUser(): HostProfile? {
        if (!isFirebaseAvailable) return null
        return try {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            if (user != null) {
                HostProfile(
                    uid = user.uid,
                    email = user.email,
                    displayName = user.displayName ?: user.email?.substringBefore('@') ?: "Host"
                )
            } else null
        } catch (e: Exception) {
            Log.w("CloudSync", "Firebase auth not initialized: ${e.message}")
            null
        }
    }

    suspend fun syncGameDataToFirestore(repository: GameRepository): Result<String> = withContext(Dispatchers.IO) {
        if (!isFirebaseAvailable) {
            return@withContext Result.failure(Exception("Firebase is not initialized."))
        }

        try {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser ?: return@withContext Result.failure(Exception("Not signed in"))
            val firestore = FirebaseFirestore.getInstance()

            val historyList = repository.allGameHistory.first()
            val customWords = repository.allCustomWords.first()

            val hostDocRef = firestore.collection("host_profiles").document(user.uid)
            val syncData = hashMapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: ""),
                "totalGames" to historyList.size,
                "lastSyncTimestamp" to System.currentTimeMillis(),
                "historySummary" to historyList.take(20).map {
                    mapOf(
                        "word" to it.secretWord,
                        "category" to it.category,
                        "winner" to it.winner,
                        "spies" to it.spyNames,
                        "timestamp" to it.timestamp
                    )
                },
                "customWords" to customWords.map { it.word }
            )

            hostDocRef.set(syncData).await()
            Result.success("Synced ${historyList.size} games and ${customWords.size} words.")
        } catch (e: Exception) {
            Log.e("CloudSync", "Sync error", e)
            Result.failure(e)
        }
    }

    fun signOut() {
        if (isFirebaseAvailable) {
            try {
                FirebaseAuth.getInstance().signOut()
            } catch (e: Exception) {
                Log.w("CloudSync", "Error signing out", e)
            }
        }
    }
}

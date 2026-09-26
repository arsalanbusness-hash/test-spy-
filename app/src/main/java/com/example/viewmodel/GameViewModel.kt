package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundHapticsManager
import com.example.cloud.CloudSyncManager
import com.example.cloud.HostProfile
import com.example.data.AppDatabase
import com.example.data.GameRepository
import com.example.data.entity.CustomWord
import com.example.data.entity.GameHistory
import com.example.dictionary.Category
import com.example.dictionary.Difficulty
import com.example.dictionary.WordDictionaryEngine
import com.example.dictionary.WordLengthFilter
import com.example.localization.Language
import com.example.localization.StringKey
import com.example.localization.StringsProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    SETUP,
    PASS_DEVICE,
    REVEAL_ROLE,
    ALL_ROLES_VIEWED,
    DISCUSSION,
    VOTING,
    SPY_GUESS,
    RESULT,
    HOW_TO_PLAY,
    SETTINGS,
    CUSTOM_WORDS,
    HISTORY
}

enum class SpyClueSetting {
    NO_CLUE,
    CATEGORY_CLUE,
    FIRST_LETTER,
    WORD_CATEGORY
}

data class Player(
    val id: Int,
    val name: String,
    val isSpy: Boolean,
    val hasRevealed: Boolean = false
)

data class GameUiState(
    val currentScreen: Screen = Screen.HOME,
    val language: Language = Language.ENGLISH,
    val playerCount: Int = 5,
    val spyCount: Int = 1,
    val playerNames: List<String> = listOf("Alex", "Sam", "Mike", "Sarah", "Jake"),
    val spyClueSetting: SpyClueSetting = SpyClueSetting.NO_CLUE,
    val selectedCategories: Set<Category> = Category.entries.toSet(),
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val wordLengthFilter: WordLengthFilter = WordLengthFilter.ANY,
    val discussionTimerDuration: Int = 60, // 0 = off
    val soundEnabled: Boolean = true,
    val vibrateEnabled: Boolean = true,
    val preventRepeatedWords: Boolean = true,
    
    // Active Round State
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val secretWord: String = "",
    val activeCategory: Category = Category.RANDOM,
    val firstCluePlayerName: String = "",
    val timerRemainingSeconds: Int = 60,
    val isTimerRunning: Boolean = false,
    val accusedPlayer: Player? = null,
    val winner: String = "", // "INNOCENT" or "SPY"
    val spyGuessedCorrectly: Boolean = false,
    val roundDurationSeconds: Int = 0,
    val toastMessage: String? = null,
    val hostProfile: HostProfile? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = GameRepository(database.customWordDao(), database.gameHistoryDao())
    val soundManager = SoundHapticsManager(application)
    val cloudManager = CloudSyncManager(application)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    val customWords: StateFlow<List<CustomWord>> = repository.allCustomWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameHistory: StateFlow<List<GameHistory>> = repository.allGameHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalGamesCount: StateFlow<Int> = repository.totalGamesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val innocentWinsCount: StateFlow<Int> = repository.innocentWinsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val spyWinsCount: StateFlow<Int> = repository.spyWinsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private var timerJob: Job? = null
    private var roundStartTime: Long = 0

    init {
        _uiState.value = _uiState.value.copy(
            hostProfile = cloudManager.getCurrentUser()
        )
        refreshDefaultNamesForLanguage(_uiState.value.language)
    }

    fun setLanguage(newLanguage: Language) {
        _uiState.value = _uiState.value.copy(language = newLanguage)
        refreshDefaultNamesForLanguage(newLanguage)
    }

    private fun refreshDefaultNamesForLanguage(lang: Language) {
        val currentNames = _uiState.value.playerNames
        // If names are default English, adapt them naturally to selected language
        val isDefaultEnglish = currentNames == listOf("Alex", "Sam", "Mike", "Sarah", "Jake") ||
                currentNames == listOf("علی", "سارا", "رضا", "مریم", "امیر")

        if (isDefaultEnglish) {
            val localizedDefaults = when (lang) {
                Language.PERSIAN -> listOf("علی", "سارا", "رضا", "مریم", "امیر")
                Language.ARABIC -> listOf("أحمد", "سارة", "محمد", "نور", "خالد")
                Language.SPANISH -> listOf("Carlos", "Sofia", "Mateo", "Lucia", "Diego")
                Language.FRENCH -> listOf("Lucas", "Emma", "Thomas", "Lea", "Hugo")
                Language.GERMAN -> listOf("Lukas", "Mia", "Leon", "Hannah", "Felix")
                Language.RUSSIAN -> listOf("Алексей", "Анна", "Михаил", "Дарья", "Иван")
                Language.TURKISH -> listOf("Ahmet", "Zeynep", "Mehmet", "Elif", "Can")
                else -> listOf("Alex", "Sam", "Mike", "Sarah", "Jake")
            }
            val count = _uiState.value.playerCount
            val padded = if (count <= localizedDefaults.size) {
                localizedDefaults.take(count)
            } else {
                localizedDefaults + (localizedDefaults.size + 1..count).map {
                    if (lang.isRtl) "بازیکن $it" else "Player $it"
                }
            }
            _uiState.value = _uiState.value.copy(playerNames = padded)
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun updatePlayerCount(newCount: Int) {
        val clampedCount = newCount.coerceIn(3, 20)
        val maxSpies = (clampedCount - 1) / 2
        val currentSpies = _uiState.value.spyCount.coerceAtMost(maxSpies.coerceAtLeast(1))

        val currentNames = _uiState.value.playerNames.toMutableList()
        val lang = _uiState.value.language
        while (currentNames.size < clampedCount) {
            val index = currentNames.size + 1
            currentNames.add(if (lang.isRtl) "بازیکن $index" else "Player $index")
        }
        val trimmedNames = currentNames.take(clampedCount)

        _uiState.value = _uiState.value.copy(
            playerCount = clampedCount,
            spyCount = currentSpies,
            playerNames = trimmedNames
        )
    }

    fun updateSpyCount(newCount: Int) {
        val maxSpies = ((_uiState.value.playerCount - 1) / 2).coerceIn(1, 5)
        _uiState.value = _uiState.value.copy(spyCount = newCount.coerceIn(1, maxSpies))
    }

    fun updatePlayerName(index: Int, newName: String) {
        val names = _uiState.value.playerNames.toMutableList()
        if (index in names.indices) {
            names[index] = newName
            _uiState.value = _uiState.value.copy(playerNames = names)
        }
    }

    fun addPlayer() {
        if (_uiState.value.playerCount < 20) {
            updatePlayerCount(_uiState.value.playerCount + 1)
        }
    }

    fun removePlayer(index: Int) {
        if (_uiState.value.playerCount > 3) {
            val names = _uiState.value.playerNames.toMutableList()
            if (index in names.indices) {
                names.removeAt(index)
                _uiState.value = _uiState.value.copy(
                    playerCount = names.size,
                    playerNames = names
                )
            }
        }
    }

    fun setSpyClueSetting(setting: SpyClueSetting) {
        _uiState.value = _uiState.value.copy(spyClueSetting = setting)
    }

    fun toggleCategory(category: Category) {
        val current = _uiState.value.selectedCategories.toMutableSet()
        if (category in current) {
            if (current.size > 1) current.remove(category)
        } else {
            current.add(category)
        }
        _uiState.value = _uiState.value.copy(selectedCategories = current)
    }

    fun selectAllCategories() {
        _uiState.value = _uiState.value.copy(selectedCategories = Category.entries.toSet())
    }

    fun deselectAllCategories() {
        _uiState.value = _uiState.value.copy(selectedCategories = setOf(Category.RANDOM))
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
    }

    fun setWordLengthFilter(filter: WordLengthFilter) {
        _uiState.value = _uiState.value.copy(wordLengthFilter = filter)
    }

    fun setDiscussionTimer(seconds: Int) {
        _uiState.value = _uiState.value.copy(discussionTimerDuration = seconds)
    }

    fun toggleSound() {
        _uiState.value = _uiState.value.copy(soundEnabled = !_uiState.value.soundEnabled)
    }

    fun toggleVibration() {
        _uiState.value = _uiState.value.copy(vibrateEnabled = !_uiState.value.vibrateEnabled)
    }

    fun togglePreventRepeatedWords() {
        _uiState.value = _uiState.value.copy(preventRepeatedWords = !_uiState.value.preventRepeatedWords)
    }

    // ==========================================
    // ROUND LIFECYCLE
    // ==========================================
    fun startNewGame() {
        viewModelScope.launch {
            val customWordList = repository.getCustomWordStrings()
            val wordResult = WordDictionaryEngine.getRandomWord(
                language = _uiState.value.language,
                categories = _uiState.value.selectedCategories,
                difficulty = _uiState.value.difficulty,
                lengthFilter = _uiState.value.wordLengthFilter,
                customWords = customWordList,
                preventRepeated = _uiState.value.preventRepeatedWords
            )

            // Randomly choose spy indices
            val indices = (0 until _uiState.value.playerCount).shuffled()
            val spyIndices = indices.take(_uiState.value.spyCount).toSet()

            val playerList = _uiState.value.playerNames.mapIndexed { index, name ->
                Player(
                    id = index,
                    name = name.ifBlank { "Player ${index + 1}" },
                    isSpy = index in spyIndices,
                    hasRevealed = false
                )
            }

            // Pick a starting player for discussion (random non-spy or any player)
            val starter = playerList.random().name
            roundStartTime = System.currentTimeMillis()

            _uiState.value = _uiState.value.copy(
                players = playerList,
                currentPlayerIndex = 0,
                secretWord = wordResult.word,
                activeCategory = wordResult.category,
                firstCluePlayerName = starter,
                timerRemainingSeconds = _uiState.value.discussionTimerDuration,
                isTimerRunning = false,
                accusedPlayer = null,
                winner = "",
                spyGuessedCorrectly = false,
                currentScreen = Screen.PASS_DEVICE
            )
        }
    }

    fun onRevealTapped() {
        val currentIdx = _uiState.value.currentPlayerIndex
        val player = _uiState.value.players.getOrNull(currentIdx)
        if (player != null) {
            soundManager.playDramaticReveal(
                isSpy = player.isSpy,
                soundEnabled = _uiState.value.soundEnabled,
                vibrateEnabled = _uiState.value.vibrateEnabled
            )
        }
        _uiState.value = _uiState.value.copy(currentScreen = Screen.REVEAL_ROLE)
    }

    fun onHideAndPassTapped() {
        val nextIdx = _uiState.value.currentPlayerIndex + 1
        if (nextIdx < _uiState.value.players.size) {
            _uiState.value = _uiState.value.copy(
                currentPlayerIndex = nextIdx,
                currentScreen = Screen.PASS_DEVICE
            )
        } else {
            // All players viewed their roles!
            soundManager.playTick(_uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
            _uiState.value = _uiState.value.copy(
                currentScreen = Screen.ALL_ROLES_VIEWED
            )
        }
    }

    fun startDiscussion() {
        val duration = _uiState.value.discussionTimerDuration
        _uiState.value = _uiState.value.copy(
            timerRemainingSeconds = duration,
            isTimerRunning = duration > 0,
            currentScreen = Screen.DISCUSSION
        )
        if (duration > 0) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerRemainingSeconds > 0 && _uiState.value.isTimerRunning) {
                delay(1000)
                val remaining = _uiState.value.timerRemainingSeconds - 1
                _uiState.value = _uiState.value.copy(timerRemainingSeconds = remaining)

                if (remaining in 1..5) {
                    soundManager.playWarningTick(_uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
                } else if (remaining > 5 && remaining % 10 == 0) {
                    soundManager.playTick(_uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
                }

                if (remaining == 0) {
                    _uiState.value = _uiState.value.copy(isTimerRunning = false)
                    soundManager.playDefeatChime(_uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
                    break
                }
            }
        }
    }

    fun toggleTimer() {
        val running = !_uiState.value.isTimerRunning
        _uiState.value = _uiState.value.copy(isTimerRunning = running)
        if (running && _uiState.value.timerRemainingSeconds > 0) {
            startTimer()
        } else {
            timerJob?.cancel()
        }
    }

    fun addTimer30Seconds() {
        val newTime = _uiState.value.timerRemainingSeconds + 30
        _uiState.value = _uiState.value.copy(timerRemainingSeconds = newTime)
    }

    fun proceedToVoting() {
        timerJob?.cancel()
        soundManager.playDramaticReveal(false, _uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
        _uiState.value = _uiState.value.copy(
            isTimerRunning = false,
            currentScreen = Screen.VOTING
        )
    }

    fun selectAccusedPlayer(player: Player) {
        _uiState.value = _uiState.value.copy(accusedPlayer = player)
    }

    fun confirmAccusation() {
        val accused = _uiState.value.accusedPlayer ?: return
        if (accused.isSpy) {
            // Spy was caught! Spy gets one last chance
            soundManager.playDramaticReveal(true, _uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
            _uiState.value = _uiState.value.copy(currentScreen = Screen.SPY_GUESS)
        } else {
            // Innocent was accused! Spies win!
            finishRound(
                winner = "SPY",
                wasSpyCaught = false,
                spyGuessedWord = false
            )
        }
    }

    fun handleSpyGuess(spyGuessedCorrectly: Boolean) {
        if (spyGuessedCorrectly) {
            // Spy stole the win
            finishRound(
                winner = "SPY",
                wasSpyCaught = true,
                spyGuessedWord = true
            )
        } else {
            // Innocents win!
            finishRound(
                winner = "INNOCENT",
                wasSpyCaught = true,
                spyGuessedWord = false
            )
        }
    }

    private fun finishRound(winner: String, wasSpyCaught: Boolean, spyGuessedWord: Boolean) {
        val duration = ((System.currentTimeMillis() - roundStartTime) / 1000).toInt().coerceAtLeast(10)
        val spies = _uiState.value.players.filter { it.isSpy }.map { it.name }
        val innocents = _uiState.value.players.filter { !it.isSpy }.map { it.name }

        if (winner == "INNOCENT") {
            soundManager.playVictoryChime(_uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
        } else {
            soundManager.playDramaticReveal(true, _uiState.value.soundEnabled, _uiState.value.vibrateEnabled)
        }

        viewModelScope.launch {
            repository.recordGame(
                secretWord = _uiState.value.secretWord,
                category = _uiState.value.activeCategory.name,
                winner = winner,
                spyNames = spies,
                innocentNames = innocents,
                accusedName = _uiState.value.accusedPlayer?.name ?: "None",
                wasSpyCaught = wasSpyCaught,
                spyGuessedWord = spyGuessedWord,
                durationSeconds = duration
            )
        }

        _uiState.value = _uiState.value.copy(
            winner = winner,
            spyGuessedCorrectly = spyGuessedWord,
            roundDurationSeconds = duration,
            currentScreen = Screen.RESULT
        )
    }

    // ==========================================
    // CUSTOM WORDS
    // ==========================================
    fun addCustomWord(word: String, categoryName: String) {
        if (word.isBlank()) return
        viewModelScope.launch {
            repository.insertCustomWord(
                word = word,
                category = categoryName.ifBlank { "Custom" },
                languageCode = _uiState.value.language.code
            )
            setToast("Word added successfully!")
        }
    }

    fun deleteCustomWord(id: Long) {
        viewModelScope.launch {
            repository.deleteCustomWord(id)
        }
    }

    fun clearGameHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            setToast("Match history cleared.")
        }
    }

    // ==========================================
    // CLOUD SYNC
    // ==========================================
    fun syncToCloud() {
        viewModelScope.launch {
            val result = cloudManager.syncGameDataToFirestore(repository)
            if (result.isSuccess) {
                setToast(StringsProvider.get(StringKey.SYNC_SUCCESS, _uiState.value.language))
            } else {
                setToast("Cloud Sync: ${result.exceptionOrNull()?.localizedMessage ?: "Failed"}")
            }
        }
    }

    fun signOutCloud() {
        cloudManager.signOut()
        _uiState.value = _uiState.value.copy(hostProfile = null)
        setToast("Signed out.")
    }

    fun setToast(message: String?) {
        _uiState.value = _uiState.value.copy(toastMessage = message)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.release()
    }
}

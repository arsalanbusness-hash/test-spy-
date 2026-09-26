package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.LanguageDialog
import com.example.ui.screens.AllRolesViewedScreen
import com.example.ui.screens.CustomWordsScreen
import com.example.ui.screens.DiscussionScreen
import com.example.ui.screens.GameSetupScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HowToPlayScreen
import com.example.ui.screens.PassDeviceScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.RoleRevealScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpyGuessScreen
import com.example.ui.screens.VotingScreen
import com.example.ui.theme.SpyGameTheme
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            var showLanguageDialog by remember { mutableStateOf(false) }
            val snackbarHostState = remember { SnackbarHostState() }

            // Dynamic RTL Support for Persian and Arabic
            val layoutDirection = if (uiState.language.isRtl) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            // Toast effect
            LaunchedEffect(uiState.toastMessage) {
                uiState.toastMessage?.let { msg ->
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    viewModel.setToast(null)
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                SpyGameTheme {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        // Handle back button for screens
                        BackHandler(enabled = uiState.currentScreen != Screen.HOME) {
                            when (uiState.currentScreen) {
                                Screen.CUSTOM_WORDS -> viewModel.navigateTo(Screen.SETTINGS)
                                Screen.PASS_DEVICE, Screen.REVEAL_ROLE, Screen.ALL_ROLES_VIEWED,
                                Screen.DISCUSSION, Screen.VOTING, Screen.SPY_GUESS, Screen.RESULT -> {
                                    viewModel.navigateTo(Screen.HOME)
                                }
                                else -> viewModel.navigateTo(Screen.HOME)
                            }
                        }

                        AnimatedContent(
                            targetState = uiState.currentScreen,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.SETUP -> GameSetupScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.PASS_DEVICE -> PassDeviceScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.REVEAL_ROLE -> RoleRevealScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.ALL_ROLES_VIEWED -> AllRolesViewedScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.DISCUSSION -> DiscussionScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.VOTING -> VotingScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.SPY_GUESS -> SpyGuessScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.RESULT -> ResultScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.HOW_TO_PLAY -> HowToPlayScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.SETTINGS -> SettingsScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.CUSTOM_WORDS -> CustomWordsScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                                Screen.HISTORY -> HistoryScreen(
                                    viewModel = viewModel,
                                    onOpenLanguageDialog = { showLanguageDialog = true }
                                )
                            }
                        }

                        if (showLanguageDialog) {
                            LanguageDialog(
                                currentLanguage = uiState.language,
                                onLanguageSelected = { viewModel.setLanguage(it) },
                                onDismiss = { showLanguageDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }
}

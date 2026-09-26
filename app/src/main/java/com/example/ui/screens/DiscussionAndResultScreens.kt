package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.StringKey
import com.example.localization.StringsProvider
import com.example.ui.components.CircularCountdownTimer
import com.example.ui.components.GameTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowButton
import com.example.ui.components.SecondaryGlowButton
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.InnocentEmerald
import com.example.ui.theme.NeonAccent
import com.example.ui.theme.SpyCrimson
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen

@Composable
fun DiscussionScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.DISCUSSION_TITLE, lang),
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Starter Player Banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    color = NeonAccent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, NeonAccent.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = StringsProvider.get(StringKey.FIRST_CLUE_PROMPT, lang, uiState.firstCluePlayerName),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = NeonAccent,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Timer Display if duration > 0
                if (uiState.discussionTimerDuration > 0) {
                    CircularCountdownTimer(
                        totalSeconds = uiState.discussionTimerDuration,
                        remainingSeconds = uiState.timerRemainingSeconds
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Timer Controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.toggleTimer() },
                            border = BorderStroke(1.dp, GlassBorder),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("toggle_timer_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (uiState.isTimerRunning) "Pause" else "Resume",
                                tint = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = StringsProvider.get(
                                    if (uiState.isTimerRunning) StringKey.PAUSE else StringKey.RESUME,
                                    lang
                                ),
                                color = TextPrimary
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.addTimer30Seconds() },
                            border = BorderStroke(1.dp, GlassBorder),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("add_30s_timer_button")
                        ) {
                            Text(text = StringsProvider.get(StringKey.ADD_30S, lang), color = TextPrimary)
                        }
                    }
                } else {
                    // Timer is Off mode
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = DarkCardSurface,
                        border = BorderStroke(2.dp, NeonAccent)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🗣️", fontSize = 50.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Take turns giving clues in real life!",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Time to Vote button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.TIME_TO_VOTE, lang),
                    onClick = { viewModel.proceedToVoting() },
                    color = SpyCrimson,
                    textColor = Color.White,
                    testTag = "proceed_to_vote_button",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HowToVote,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun VotingScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.TIME_TO_VOTE, lang),
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = StringsProvider.get(StringKey.TIME_TO_VOTE_TITLE, lang),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = StringsProvider.get(StringKey.TIME_TO_VOTE_DESC, lang),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = StringsProvider.get(StringKey.WHO_WAS_THE_SPY, lang),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = SpyCrimson,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Player List to mark accusation
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    uiState.players.forEach { player ->
                        val isSelected = uiState.accusedPlayer?.id == player.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { viewModel.selectAccusedPlayer(player) }
                                .testTag("accuse_player_${player.id}"),
                            color = if (isSelected) SpyCrimson.copy(alpha = 0.25f) else DarkCardSurface,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) SpyCrimson else GlassBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        modifier = Modifier.size(36.dp),
                                        shape = CircleShape,
                                        color = if (isSelected) SpyCrimson else NeonAccent.copy(alpha = 0.2f)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = player.name.firstOrNull()?.uppercase() ?: "?",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = player.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) SpyCrimson else TextPrimary
                                        )
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = SpyCrimson
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Confirm Accusation button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.CONFIRM_ACCUSATION, lang),
                    onClick = { viewModel.confirmAccusation() },
                    color = SpyCrimson,
                    textColor = Color.White,
                    enabled = uiState.accusedPlayer != null,
                    testTag = "confirm_accusation_button"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SpyGuessScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.SPY_WAS_CAUGHT, lang),
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    color = SpyCrimson.copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, SpyCrimson)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🎯", fontSize = 42.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = StringsProvider.get(StringKey.SPY_LAST_CHANCE_TITLE, lang),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = SpyCrimson
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = StringsProvider.get(StringKey.SPY_LAST_CHANCE_DESC, lang),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextPrimary,
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Choice Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.SPY_GUESSED_CORRECT, lang),
                    onClick = { viewModel.handleSpyGuess(true) },
                    color = SpyCrimson,
                    textColor = Color.White,
                    testTag = "spy_guess_correct_button"
                )

                GlowButton(
                    text = StringsProvider.get(StringKey.SPY_GUESSED_WRONG, lang),
                    onClick = { viewModel.handleSpyGuess(false) },
                    color = InnocentEmerald,
                    textColor = Color.Black,
                    testTag = "spy_guess_wrong_button"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ResultScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language
    val isInnocentWin = uiState.winner == "INNOCENT"
    val winColor = if (isInnocentWin) InnocentEmerald else SpyCrimson

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.APP_NAME, lang),
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Victory Emblem
                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    color = winColor.copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, winColor)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isInnocentWin) "🎉" else "🕵️",
                            fontSize = 44.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Win Banner Text
                Text(
                    text = StringsProvider.get(
                        if (isInnocentWin) {
                            StringKey.PLAYERS_WIN
                        } else if (uiState.spyGuessedCorrectly) {
                            StringKey.SPY_STOLE_THE_WIN
                        } else {
                            StringKey.SPIES_WIN
                        },
                        lang
                    ),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = winColor
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Secret Word Reveal Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = winColor
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = StringsProvider.get(StringKey.REVEALED_WORD_WAS, lang),
                            style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.secretWord,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${uiState.activeCategory.icon} ${uiState.activeCategory.getDisplayName(lang)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = NeonAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Spies Reveal
                        val spiesList = uiState.players.filter { it.isSpy }.map { it.name }.joinToString(", ")
                        Text(
                            text = StringsProvider.get(StringKey.THE_REAL_SPIES_WERE, lang),
                            style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = spiesList,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SpyCrimson
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.PLAY_NEXT_ROUND, lang),
                    onClick = { viewModel.startNewGame() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "play_next_round_button",
                    icon = {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = null, tint = Color.Black)
                    }
                )

                SecondaryGlowButton(
                    text = StringsProvider.get(StringKey.CHANGE_SETTINGS, lang),
                    onClick = { viewModel.navigateTo(Screen.SETUP) },
                    testTag = "change_settings_button"
                )

                SecondaryGlowButton(
                    text = StringsProvider.get(StringKey.RETURN_HOME, lang),
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    testTag = "return_home_button"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

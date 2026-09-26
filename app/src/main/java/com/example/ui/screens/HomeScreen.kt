package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.StringKey
import com.example.localization.StringsProvider
import com.example.ui.components.GameTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowButton
import com.example.ui.components.SecondaryGlowButton
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.InnocentEmerald
import com.example.ui.theme.NeonAccent
import com.example.ui.theme.SpyCrimson
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalGames by viewModel.totalGamesCount.collectAsState()
    val innocentWins by viewModel.innocentWinsCount.collectAsState()
    val spyWins by viewModel.spyWinsCount.collectAsState()

    val lang = uiState.language

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Header with Language selector
        GameTopBar(
            title = StringsProvider.get(StringKey.APP_NAME, lang),
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Hero Emblem
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(SpyCrimson.copy(alpha = 0.35f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = DarkCardSurface,
                        border = androidx.compose.foundation.BorderStroke(2.dp, SpyCrimson)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🕵️", fontSize = 42.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = StringsProvider.get(StringKey.APP_NAME, lang),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 2.sp
                    )
                )

                Text(
                    text = StringsProvider.get(StringKey.TAGLINE, lang),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = NeonAccent,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Primary Play Action
                GlowButton(
                    text = StringsProvider.get(StringKey.PLAY, lang),
                    onClick = { viewModel.startNewGame() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "home_play_button",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Custom Game / Setup
                SecondaryGlowButton(
                    text = StringsProvider.get(StringKey.CUSTOM_GAME, lang),
                    onClick = { viewModel.navigateTo(Screen.SETUP) },
                    testTag = "home_custom_game_button",
                    borderColor = NeonAccent.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // How to play & Settings row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        SecondaryGlowButton(
                            text = StringsProvider.get(StringKey.HOW_TO_PLAY, lang),
                            onClick = { viewModel.navigateTo(Screen.HOW_TO_PLAY) },
                            testTag = "home_how_to_play_button"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        SecondaryGlowButton(
                            text = StringsProvider.get(StringKey.SETTINGS, lang),
                            onClick = { viewModel.navigateTo(Screen.SETTINGS) },
                            testTag = "home_settings_button"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Match History button
                SecondaryGlowButton(
                    text = StringsProvider.get(StringKey.HISTORY, lang),
                    onClick = { viewModel.navigateTo(Screen.HISTORY) },
                    testTag = "home_history_button"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Game stats summary card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = totalGames.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = StringsProvider.get(StringKey.TOTAL_GAMES_PLAYED, lang),
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = innocentWins.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = InnocentEmerald
                            )
                        )
                        Text(
                            text = StringsProvider.get(StringKey.INNOCENT_WINS, lang),
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = spyWins.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SpyCrimson
                            )
                        )
                        Text(
                            text = StringsProvider.get(StringKey.SPY_WINS, lang),
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }
    }
}

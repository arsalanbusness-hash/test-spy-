package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.InnocentEmerald
import com.example.ui.theme.NeonAccent
import com.example.ui.theme.SpyCrimson
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen
import com.example.viewmodel.SpyClueSetting

@Composable
fun PassDeviceScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language
    val currentPlayer = uiState.players.getOrNull(uiState.currentPlayerIndex)
    val playerName = currentPlayer?.name ?: "Player ${uiState.currentPlayerIndex + 1}"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = "${uiState.currentPlayerIndex + 1} / ${uiState.players.size}",
            onBack = { viewModel.navigateTo(Screen.HOME) },
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

                // Privacy Shield Icon
                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    color = DarkCardSurface,
                    border = BorderStroke(2.dp, NeonAccent)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Privacy Shield",
                            tint = NeonAccent,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = StringsProvider.get(StringKey.PASS_PHONE_TO, lang, playerName),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = StringsProvider.get(StringKey.GET_READY_PROMPT, lang),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Reveal button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.REVEAL_MY_ROLE, lang),
                    onClick = { viewModel.onRevealTapped() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "reveal_my_role_button",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RoleRevealScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.language
    val currentPlayer = uiState.players.getOrNull(uiState.currentPlayerIndex)
    val isSpy = currentPlayer?.isSpy == true

    val roleColor = if (isSpy) SpyCrimson else InnocentEmerald

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = currentPlayer?.name ?: "",
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
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.9f)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 500.dp)
                        .padding(top = 10.dp),
                    borderColor = roleColor
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Role Emblem
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = roleColor.copy(alpha = 0.15f),
                            border = BorderStroke(2.dp, roleColor)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isSpy) "🕵️" else "🟢",
                                    fontSize = 38.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Role Title
                        Text(
                            text = StringsProvider.get(
                                if (isSpy) StringKey.YOU_ARE_SPY else StringKey.YOU_ARE_INNOCENT,
                                lang
                            ),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = roleColor
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!isSpy) {
                            // Innocent player sees the secret word & category
                            Text(
                                text = StringsProvider.get(StringKey.SECRET_WORD_IS, lang),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = TextSecondary,
                                    letterSpacing = 1.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp)),
                                color = DarkCardSurface,
                                border = BorderStroke(1.dp, InnocentEmerald.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
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
                                            color = InnocentEmerald,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = StringsProvider.get(StringKey.INNOCENT_HINT, lang),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary,
                                    lineHeight = 22.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            // Spy sees hint + clue if configured
                            Text(
                                text = StringsProvider.get(StringKey.SPY_HINT, lang),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextPrimary,
                                    lineHeight = 24.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            // Check Spy Clue Setting
                            if (uiState.spyClueSetting != SpyClueSetting.NO_CLUE) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp)),
                                    color = SpyCrimson.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, SpyCrimson.copy(alpha = 0.5f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = StringsProvider.get(StringKey.SPY_CLUE_LABEL, lang),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = SpyCrimson,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val clueText = when (uiState.spyClueSetting) {
                                            SpyClueSetting.CATEGORY_CLUE ->
                                                "${uiState.activeCategory.icon} ${uiState.activeCategory.getDisplayName(lang)}"
                                            SpyClueSetting.FIRST_LETTER ->
                                                "First letter: ${uiState.secretWord.firstOrNull()?.uppercase() ?: "?"}"
                                            SpyClueSetting.WORD_CATEGORY ->
                                                "${uiState.activeCategory.getDisplayName(lang)} (~${uiState.secretWord.length} letters)"
                                            else -> ""
                                        }
                                        Text(
                                            text = clueText,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Hide & Pass button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.HIDE_AND_PASS, lang),
                    onClick = { viewModel.onHideAndPassTapped() },
                    color = roleColor,
                    textColor = Color.White,
                    testTag = "hide_and_pass_button",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
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
fun AllRolesViewedScreen(
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
            title = StringsProvider.get(StringKey.APP_NAME, lang),
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
                Spacer(modifier = Modifier.height(30.dp))

                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = DarkCardSurface,
                    border = BorderStroke(2.dp, NeonAccent)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "📱", fontSize = 50.sp)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = StringsProvider.get(StringKey.ALL_ROLES_REVEALED_TITLE, lang),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = StringsProvider.get(StringKey.ALL_ROLES_REVEALED_DESC, lang),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextSecondary,
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                GlowButton(
                    text = StringsProvider.get(StringKey.START_DISCUSSION, lang),
                    onClick = { viewModel.startDiscussion() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "start_discussion_button"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

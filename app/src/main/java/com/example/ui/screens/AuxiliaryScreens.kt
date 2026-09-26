package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HowToPlayScreen(
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
            title = StringsProvider.get(StringKey.HOW_TO_PLAY, lang),
            onBack = { viewModel.navigateTo(Screen.HOME) },
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val rules = listOf(
                    Triple(StringKey.RULE_1_TITLE, StringKey.RULE_1_DESC, "📱"),
                    Triple(StringKey.RULE_2_TITLE, StringKey.RULE_2_DESC, "🗣️"),
                    Triple(StringKey.RULE_3_TITLE, StringKey.RULE_3_DESC, "🗳️"),
                    Triple(StringKey.RULE_4_TITLE, StringKey.RULE_4_DESC, "🎯")
                )

                rules.forEach { (titleKey, descKey, icon) ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                modifier = Modifier.size(46.dp),
                                shape = CircleShape,
                                color = NeonAccent.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, NeonAccent)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = icon, fontSize = 22.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = StringsProvider.get(titleKey, lang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = StringsProvider.get(descKey, lang),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        lineHeight = 22.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                GlowButton(
                    text = StringsProvider.get(StringKey.PLAY, lang),
                    onClick = { viewModel.startNewGame() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "rules_play_button"
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
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
            title = StringsProvider.get(StringKey.SETTINGS, lang),
            onBack = { viewModel.navigateTo(Screen.HOME) },
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Language Setting Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLanguageDialog() }
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = NeonAccent
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = StringsProvider.get(StringKey.LANGUAGE, lang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "${lang.flagEmoji} ${lang.nativeName}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = NeonAccent)
                                )
                            }
                        }
                        Text(
                            text = "Change",
                            color = NeonAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Custom Words Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.CUSTOM_WORDS) }
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Spellcheck,
                                contentDescription = null,
                                tint = NeonAccent
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = StringsProvider.get(StringKey.CUSTOM_WORDS, lang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Add your own secret words",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }
                        Text(
                            text = "Manage",
                            color = NeonAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Audio & Haptics Toggles
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.SOUND_EFFECTS, lang),
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = uiState.soundEnabled,
                                onCheckedChange = { viewModel.toggleSound() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.VIBRATION, lang),
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = uiState.vibrateEnabled,
                                onCheckedChange = { viewModel.toggleVibration() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.PREVENT_REPEATED_WORDS, lang),
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = uiState.preventRepeatedWords,
                                onCheckedChange = { viewModel.togglePreventRepeatedWords() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }
                    }
                }

                // Optional Cloud Backup Section (Firebase Firestore + Google Sign-In)
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = NeonAccent
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = StringsProvider.get(StringKey.HOST_CLOUD_SYNC, lang),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = StringsProvider.get(StringKey.HOST_CLOUD_SYNC_DESC, lang),
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (uiState.hostProfile != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${StringsProvider.get(StringKey.SIGNED_IN_AS, lang)} ${uiState.hostProfile?.displayName}",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = InnocentEmerald)
                                )
                                Text(
                                    text = StringsProvider.get(StringKey.SIGN_OUT, lang),
                                    color = SpyCrimson,
                                    modifier = Modifier
                                        .clickable { viewModel.signOutCloud() }
                                        .padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            GlowButton(
                                text = StringsProvider.get(StringKey.SYNC_NOW, lang),
                                onClick = { viewModel.syncToCloud() },
                                color = NeonAccent,
                                textColor = Color.Black
                            )
                        } else {
                            SecondaryGlowButton(
                                text = StringsProvider.get(StringKey.SYNC_NOW, lang),
                                onClick = { viewModel.syncToCloud() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomWordsScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val customWords by viewModel.customWords.collectAsState()
    val lang = uiState.language

    var wordText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.CUSTOM_WORDS, lang),
            onBack = { viewModel.navigateTo(Screen.SETTINGS) },
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                // Add Word Form
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = StringsProvider.get(StringKey.ADD_NEW_WORD, lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = wordText,
                            onValueChange = { wordText = it },
                            placeholder = { Text(StringsProvider.get(StringKey.WORD_INPUT_HINT, lang)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_word_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonAccent,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = DarkCardSurface,
                                unfocusedContainerColor = DarkCardSurface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = categoryText,
                            onValueChange = { categoryText = it },
                            placeholder = { Text(StringsProvider.get(StringKey.CATEGORY_INPUT_HINT, lang)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_category_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonAccent,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = DarkCardSurface,
                                unfocusedContainerColor = DarkCardSurface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GlowButton(
                            text = StringsProvider.get(StringKey.SAVE_WORD, lang),
                            onClick = {
                                if (wordText.isNotBlank()) {
                                    viewModel.addCustomWord(wordText, categoryText)
                                    wordText = ""
                                    categoryText = ""
                                }
                            },
                            enabled = wordText.isNotBlank(),
                            testTag = "save_custom_word_button"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Words List
                if (customWords.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = StringsProvider.get(StringKey.NO_CUSTOM_WORDS, lang),
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(customWords, key = { it.id }) { item ->
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = item.word,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = item.categoryName,
                                            style = MaterialTheme.typography.bodySmall.copy(color = NeonAccent)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteCustomWord(item.id) },
                                        modifier = Modifier.testTag("delete_custom_word_${item.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = SpyCrimson
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    viewModel: GameViewModel,
    onOpenLanguageDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val historyList by viewModel.gameHistory.collectAsState()
    val lang = uiState.language

    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        GameTopBar(
            title = StringsProvider.get(StringKey.HISTORY, lang),
            onBack = { viewModel.navigateTo(Screen.HOME) },
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                if (historyList.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Clear History",
                            color = SpyCrimson,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .clickable { viewModel.clearGameHistory() }
                                .padding(8.dp)
                        )
                    }
                }

                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "📜", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No games played yet. Start a game to build your match history!",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(historyList, key = { it.id }) { match ->
                            val isInnocent = match.winner == "INNOCENT"
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                borderColor = if (isInnocent) InnocentEmerald.copy(alpha = 0.5f) else SpyCrimson.copy(alpha = 0.5f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isInnocent) "🎉 INNOCENTS WON" else "🕵️ SPIES WON",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isInnocent) InnocentEmerald else SpyCrimson
                                            )
                                        )
                                        Text(
                                            text = dateFormatter.format(Date(match.timestamp)),
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Word: ${match.secretWord} (${match.category})",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Spies: ${match.spyNames}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                    )

                                    if (match.accusedName.isNotBlank() && match.accusedName != "None") {
                                        Text(
                                            text = "Accused: ${match.accusedName}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.Category
import com.example.dictionary.Difficulty
import com.example.dictionary.WordLengthFilter
import com.example.localization.StringKey
import com.example.localization.StringsProvider
import com.example.ui.components.GameTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowButton
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonAccent
import com.example.ui.theme.SpyCrimson
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen
import com.example.viewmodel.SpyClueSetting

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameSetupScreen(
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
            title = StringsProvider.get(StringKey.CUSTOM_GAME, lang),
            onBack = { viewModel.navigateTo(Screen.HOME) },
            currentLanguage = lang,
            onLanguageClick = onOpenLanguageDialog
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Players & Spies Steppers Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Player count stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = StringsProvider.get(StringKey.PLAYERS_COUNT, lang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "3 - 20 players",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.updatePlayerCount(uiState.playerCount - 1) },
                                    enabled = uiState.playerCount > 3,
                                    modifier = Modifier.testTag("decrease_players_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = if (uiState.playerCount > 3) NeonAccent else TextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                                Text(
                                    text = "${uiState.playerCount}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.updatePlayerCount(uiState.playerCount + 1) },
                                    enabled = uiState.playerCount < 20,
                                    modifier = Modifier.testTag("increase_players_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = if (uiState.playerCount < 20) NeonAccent else TextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Spy count stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = StringsProvider.get(StringKey.SPIES_COUNT, lang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SpyCrimson
                                    )
                                )
                                Text(
                                    text = "1 - ${((uiState.playerCount - 1) / 2).coerceIn(1, 5)} spies",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }

                            val maxSpies = ((uiState.playerCount - 1) / 2).coerceIn(1, 5)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.updateSpyCount(uiState.spyCount - 1) },
                                    enabled = uiState.spyCount > 1,
                                    modifier = Modifier.testTag("decrease_spies_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease Spies",
                                        tint = if (uiState.spyCount > 1) SpyCrimson else TextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                                Text(
                                    text = "${uiState.spyCount}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SpyCrimson
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.updateSpyCount(uiState.spyCount + 1) },
                                    enabled = uiState.spyCount < maxSpies,
                                    modifier = Modifier.testTag("increase_spies_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase Spies",
                                        tint = if (uiState.spyCount < maxSpies) SpyCrimson else TextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Player Names Editor
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.PLAYER_NAMES, lang),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            IconButton(
                                onClick = { viewModel.addPlayer() },
                                enabled = uiState.playerCount < 20,
                                modifier = Modifier.testTag("add_player_inline_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add player",
                                    tint = NeonAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.playerNames.forEachIndexed { index, name ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(32.dp),
                                        shape = CircleShape,
                                        color = NeonAccent.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, NeonAccent)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                color = NeonAccent,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = { viewModel.updatePlayerName(index, it) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("player_name_input_$index"),
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
                                    if (uiState.playerCount > 3) {
                                        IconButton(
                                            onClick = { viewModel.removePlayer(index) },
                                            modifier = Modifier.testTag("remove_player_$index")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove",
                                                tint = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Spy Clue Settings
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = StringsProvider.get(StringKey.SPY_CLUES_TITLE, lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val clueOptions = listOf(
                            SpyClueSetting.NO_CLUE to (StringKey.CLUE_NONE to StringKey.CLUE_NONE_DESC),
                            SpyClueSetting.CATEGORY_CLUE to (StringKey.CLUE_CATEGORY to StringKey.CLUE_CATEGORY_DESC),
                            SpyClueSetting.FIRST_LETTER to (StringKey.CLUE_FIRST_LETTER to StringKey.CLUE_FIRST_LETTER_DESC),
                            SpyClueSetting.WORD_CATEGORY to (StringKey.CLUE_WORD_CATEGORY to StringKey.CLUE_WORD_CATEGORY_DESC)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            clueOptions.forEach { (setting, keys) ->
                                val (titleKey, descKey) = keys
                                val isSelected = uiState.spyClueSetting == setting
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { viewModel.setSpyClueSetting(setting) }
                                        .testTag("clue_option_${setting.name}"),
                                    color = if (isSelected) SpyCrimson.copy(alpha = 0.2f) else DarkCardSurface,
                                    border = BorderStroke(1.dp, if (isSelected) SpyCrimson else GlassBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = StringsProvider.get(titleKey, lang),
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) SpyCrimson else TextPrimary
                                                )
                                            )
                                            Text(
                                                text = StringsProvider.get(descKey, lang),
                                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                            )
                                        }
                                        if (isSelected) {
                                            Text(text = "✓", color = SpyCrimson, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Round / Timer Settings
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = StringsProvider.get(StringKey.DISCUSSION_TIMER, lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val timerPresets = listOf(
                            0 to StringsProvider.get(StringKey.TIMER_OFF, lang),
                            30 to "30s",
                            60 to "60s",
                            90 to "90s",
                            120 to "2m",
                            180 to "3m"
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            timerPresets.forEach { (seconds, label) ->
                                val isSelected = uiState.discussionTimerDuration == seconds
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setDiscussionTimer(seconds) },
                                    label = { Text(label) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonAccent,
                                        selectedLabelColor = Color.Black,
                                        containerColor = DarkCardSurface,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Audio & Haptic Toggles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.SOUND_EFFECTS, lang),
                                color = TextPrimary
                            )
                            Switch(
                                checked = uiState.soundEnabled,
                                onCheckedChange = { viewModel.toggleSound() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.VIBRATION, lang),
                                color = TextPrimary
                            )
                            Switch(
                                checked = uiState.vibrateEnabled,
                                onCheckedChange = { viewModel.toggleVibration() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.PREVENT_REPEATED_WORDS, lang),
                                color = TextPrimary
                            )
                            Switch(
                                checked = uiState.preventRepeatedWords,
                                onCheckedChange = { viewModel.togglePreventRepeatedWords() },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonAccent)
                            )
                        }
                    }
                }

                // 5. Category Selection
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsProvider.get(StringKey.CATEGORIES_TITLE, lang),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Row {
                                Text(
                                    text = StringsProvider.get(StringKey.SELECT_ALL, lang),
                                    style = MaterialTheme.typography.labelSmall.copy(color = NeonAccent),
                                    modifier = Modifier
                                        .clickable { viewModel.selectAllCategories() }
                                        .padding(4.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = StringsProvider.get(StringKey.DESELECT_ALL, lang),
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                                    modifier = Modifier
                                        .clickable { viewModel.deselectAllCategories() }
                                        .padding(4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Category.entries.forEach { cat ->
                                val isSelected = cat in uiState.selectedCategories
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.toggleCategory(cat) },
                                    label = { Text("${cat.icon} ${cat.getDisplayName(lang)}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonAccent,
                                        selectedLabelColor = Color.Black,
                                        containerColor = DarkCardSurface,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                // 6. Difficulty & Length Filters
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = StringsProvider.get(StringKey.DIFFICULTY_TITLE, lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Difficulty.entries.forEach { diff ->
                                val isSelected = uiState.difficulty == diff
                                Box(modifier = Modifier.weight(1f)) {
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.setDifficulty(diff) },
                                        label = {
                                            Text(
                                                text = diff.getDisplayName(lang),
                                                fontSize = 12.sp,
                                                maxLines = 1
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = NeonAccent,
                                            selectedLabelColor = Color.Black,
                                            containerColor = DarkCardSurface,
                                            labelColor = TextPrimary
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = StringsProvider.get(StringKey.WORD_LENGTH_TITLE, lang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            WordLengthFilter.entries.forEach { filter ->
                                val isSelected = uiState.wordLengthFilter == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setWordLengthFilter(filter) },
                                    label = { Text(filter.getDisplayName(lang)) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonAccent,
                                        selectedLabelColor = Color.Black,
                                        containerColor = DarkCardSurface,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Start Game Button
                GlowButton(
                    text = StringsProvider.get(StringKey.START_GAME, lang),
                    onClick = { viewModel.startNewGame() },
                    color = NeonAccent,
                    textColor = Color.Black,
                    testTag = "setup_start_game_button"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

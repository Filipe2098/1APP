package com.guitartuner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guitartuner.i18n.StringKey
import com.guitartuner.i18n.Strings
import com.guitartuner.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: TunerState,
    onBack: () -> Unit,
    onTunerModeChanged: (TunerMode) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
    onCalibrationChanged: (Double) -> Unit,
    onVibrationChanged: (Boolean) -> Unit,
) {
    val lang = state.language

    fun s(key: StringKey) = Strings.get(key, lang)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        TopAppBar(
            title = {
                Text(
                    text = s(StringKey.SETTINGS),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = s(StringKey.BACK)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // TUNER MODE
            SettingsSection(title = s(StringKey.TUNER_MODE)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        label = s(StringKey.STROBOSCOPIC),
                        selected = state.tunerMode == TunerMode.STROBOSCOPIC,
                        onClick = { onTunerModeChanged(TunerMode.STROBOSCOPIC) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        label = s(StringKey.NEEDLE),
                        selected = state.tunerMode == TunerMode.NEEDLE,
                        onClick = { onTunerModeChanged(TunerMode.NEEDLE) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // THEME
            SettingsSection(title = s(StringKey.THEME)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        label = s(StringKey.DARK),
                        selected = state.isDarkMode,
                        onClick = { onDarkModeChanged(true) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        label = s(StringKey.LIGHT),
                        selected = !state.isDarkMode,
                        onClick = { onDarkModeChanged(false) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // LANGUAGE
            SettingsSection(title = s(StringKey.LANGUAGE)) {
                LanguageGrid(
                    selectedLanguage = state.language,
                    onLanguageSelected = onLanguageChanged
                )
            }

            // CALIBRATION
            SettingsSection(title = s(StringKey.CALIBRATION)) {
                CalibrationControl(
                    calibration = state.a4Calibration,
                    onCalibrationChanged = onCalibrationChanged,
                    lang = lang
                )
            }

            // VIBRATION
            SettingsSection(title = s(StringKey.VIBRATION)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = s(StringKey.VIBRATION_DESC),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Switch(
                        checked = state.vibrationEnabled,
                        onCheckedChange = onVibrationChanged
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
    Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
}

@Composable
private fun ChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val textColor = if (selected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun LanguageGrid(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    // 3 columns
    val languages = AppLanguage.entries
    val rows = languages.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { lang ->
                    val isSelected = lang == selectedLanguage
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface
                            )
                            .then(
                                if (!isSelected) Modifier.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                    RoundedCornerShape(10.dp)
                                ) else Modifier
                            )
                            .clickable { onLanguageSelected(lang) }
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${lang.flag} ${lang.displayName}",
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
                // Fill empty cells in last row
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CalibrationControl(
    calibration: Double,
    onCalibrationChanged: (Double) -> Unit,
    lang: AppLanguage
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = { onCalibrationChanged((calibration - 1).coerceIn(420.0, 460.0)) },
                modifier = Modifier.size(40.dp)
            ) {
                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${calibration.toInt()} Hz",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(16.dp))

            FilledIconButton(
                onClick = { onCalibrationChanged((calibration + 1).coerceIn(420.0, 460.0)) },
                modifier = Modifier.size(40.dp)
            ) {
                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = Strings.get(StringKey.CALIBRATION_RANGE, lang),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

package com.guitartuner.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guitartuner.i18n.StringKey
import com.guitartuner.i18n.Strings
import com.guitartuner.model.TunerMode
import com.guitartuner.model.TunerState
import com.guitartuner.model.TuningAccuracy
import com.guitartuner.ui.components.*
import com.guitartuner.ui.theme.TunerGreen
import com.guitartuner.ui.theme.TunerRed
import com.guitartuner.ui.theme.TunerYellow
import kotlin.math.abs

@Composable
fun TunerScreen(
    state: TunerState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenMetronome: () -> Unit
) {
    val lang = state.language
    fun s(key: StringKey) = Strings.get(key, lang)

    val accuracyColor by animateColorAsState(
        targetValue = when (state.tuningAccuracy) {
            TuningAccuracy.PERFECT -> TunerGreen
            TuningAccuracy.CLOSE -> TunerYellow
            TuningAccuracy.OFF -> TunerRed
        },
        animationSpec = tween(300),
        label = "accuracyColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = s(StringKey.APP_TITLE),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row {
                IconButton(onClick = onOpenMetronome) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = s(StringKey.METRONOME),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
                IconButton(onClick = onOpenSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = s(StringKey.SETTINGS),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Calibration + guitar type info
        Text(
            text = "A4 = ${state.a4Calibration.toInt()} Hz  |  ${state.guitarType.stringCount} ${s(StringKey.STRINGS)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chromatic display - shows all 12 notes
        ChromaticDisplay(
            detectedNote = state.detectedNote,
            cents = state.cents,
            isActive = state.detectedFrequency > 0
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Note display
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 40.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = state.detectedNote,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (state.detectedFrequency > 0) accuracyColor
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    textAlign = TextAlign.Center
                )

                if (state.detectedFrequency > 0) {
                    Text(
                        text = String.format("%.1f Hz", state.detectedFrequency),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tuner visual: stroboscopic or needle
        when (state.tunerMode) {
            TunerMode.STROBOSCOPIC -> {
                StroboscopicTuner(
                    cents = if (state.detectedFrequency > 0) state.cents else 0.0,
                    accuracy = state.tuningAccuracy,
                    isActive = state.detectedFrequency > 0
                )
            }
            TunerMode.NEEDLE -> {
                TunerGauge(
                    cents = if (state.detectedFrequency > 0) state.cents else 0.0,
                    accuracy = state.tuningAccuracy
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cents + tuning hint
        if (state.detectedFrequency > 0) {
            Text(
                text = String.format("%+.1f %s", state.cents, s(StringKey.CENTS)),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = accuracyColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            val hint = when {
                abs(state.cents) <= 2.0 -> s(StringKey.IN_TUNE)
                state.cents < 0 -> s(StringKey.TIGHTEN)
                else -> s(StringKey.LOOSEN)
            }
            Text(
                text = hint,
                fontSize = 14.sp,
                color = accuracyColor.copy(alpha = 0.8f)
            )
        } else {
            Text(
                text = "-- ${s(StringKey.CENTS)}",
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Volume indicator
        VolumeIndicator(volume = state.volume, label = s(StringKey.SIGNAL))

        Spacer(modifier = Modifier.height(8.dp))

        // Readings history
        if (state.readingsHistory.isNotEmpty()) {
            ReadingsHistory(
                readings = state.readingsHistory,
                label = s(StringKey.HISTORY),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Start/Stop button
        Button(
            onClick = {
                if (state.isListening) onStopListening() else onStartListening()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.isListening)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (state.isListening) s(StringKey.STOP) else s(StringKey.START_TUNING),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

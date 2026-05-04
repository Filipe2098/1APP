package com.guitartuner.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    val isActive = state.detectedFrequency > 0

    val accuracyColor by animateColorAsState(
        targetValue = when (state.tuningAccuracy) {
            TuningAccuracy.PERFECT -> TunerGreen
            TuningAccuracy.CLOSE -> TunerYellow
            TuningAccuracy.OFF -> TunerRed
        },
        animationSpec = tween(300),
        label = "accuracyColor"
    )

    val inTuneThreshold = if (state.tunerMode == TunerMode.STROBOSCOPIC) 3.0 else 2.0
    val tuningHint = when {
        !isActive -> ""
        abs(state.cents) <= inTuneThreshold -> s(StringKey.IN_TUNE)
        state.cents < 0 -> s(StringKey.TIGHTEN)
        else -> s(StringKey.LOOSEN)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp),
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

            // Calibration + instrument info
            Text(
                text = "A4 = ${state.a4Calibration.toInt()} Hz  |  ${state.stringCount} ${s(StringKey.STRINGS)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
            )

            if (state.isHighPrecision) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = s(StringKey.HIGH_PRECISION),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chromatic display
            ChromaticDisplay(
                detectedNote = state.detectedNote,
                cents = state.cents,
                isActive = isActive
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mode-specific visual
            when (state.tunerMode) {
                TunerMode.STROBOSCOPIC -> {
                    StroboscopicTuner(
                        cents = if (isActive) state.cents else 0.0,
                        frequency = state.detectedFrequency,
                        detectedNote = state.detectedNote,
                        isActive = isActive,
                        centsRange = state.centsRange,
                        tuningHint = tuningHint
                    )
                }

                TunerMode.NEEDLE -> {
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
                                color = if (isActive) accuracyColor
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                textAlign = TextAlign.Center
                            )
                            if (isActive) {
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

                    TunerGauge(
                        cents = if (isActive) state.cents else 0.0,
                        accuracy = state.tuningAccuracy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isActive) {
                        Text(
                            text = String.format("%+.1f %s", state.cents, s(StringKey.CENTS)),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = accuracyColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tuningHint,
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
                }
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

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Start/Stop button — fixed at bottom
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
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = if (state.isListening) s(StringKey.STOP) else s(StringKey.START_TUNING),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

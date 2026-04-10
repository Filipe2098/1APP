package com.guitartuner.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guitartuner.i18n.StringKey
import com.guitartuner.i18n.Strings
import com.guitartuner.model.TunerState
import com.guitartuner.ui.theme.AccentBlue
import com.guitartuner.ui.theme.TunerGreen
import com.guitartuner.ui.theme.TunerRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen(
    state: TunerState,
    onBack: () -> Unit,
    onBpmChanged: (Int) -> Unit,
    onToggleMetronome: () -> Unit,
    onBeatsChanged: (Int) -> Unit
) {
    val lang = state.language
    fun s(key: StringKey) = Strings.get(key, lang)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = s(StringKey.METRONOME),
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Beat indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until state.metronomeBeatsPerMeasure) {
                    val isCurrentBeat = state.metronomeIsPlaying && state.metronomeBeat == i
                    val isAccent = i == 0

                    val beatColor by animateColorAsState(
                        targetValue = when {
                            isCurrentBeat && isAccent -> TunerRed
                            isCurrentBeat -> AccentBlue
                            else -> MaterialTheme.colorScheme.surface
                        },
                        animationSpec = tween(80),
                        label = "beatColor"
                    )

                    Box(
                        modifier = Modifier
                            .size(if (isAccent) 28.dp else 22.dp)
                            .clip(CircleShape)
                            .background(beatColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // BPM display
            Text(
                text = "${state.metronomeBpm}",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "BPM",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BPM controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledIconButton(
                    onClick = { onBpmChanged(state.metronomeBpm - 10) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("-10", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                FilledIconButton(
                    onClick = { onBpmChanged(state.metronomeBpm - 1) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("-1", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(16.dp))

                FilledIconButton(
                    onClick = { onBpmChanged(state.metronomeBpm + 1) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("+1", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                FilledIconButton(
                    onClick = { onBpmChanged(state.metronomeBpm + 10) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("+10", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Beats per measure selector
            Text(
                text = s(StringKey.TIME_SIGNATURE),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(2, 3, 4, 6).forEach { beats ->
                    val selected = state.metronomeBeatsPerMeasure == beats
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface
                            )
                            .then(
                                Modifier.padding(0.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = { onBeatsChanged(beats) }) {
                            Text(
                                text = "$beats/4",
                                fontSize = 13.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Play/Stop button
            Button(
                onClick = onToggleMetronome,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.metronomeIsPlaying)
                        TunerRed
                    else
                        TunerGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (state.metronomeIsPlaying) s(StringKey.STOP) else s(StringKey.PLAY),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BPM range hint
            Text(
                text = "40 - 240 BPM",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
        }
    }
}

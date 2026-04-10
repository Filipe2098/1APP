package com.guitartuner.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guitartuner.model.GuitarString
import com.guitartuner.ui.theme.TunerGreen
import com.guitartuner.ui.theme.TunerRed
import com.guitartuner.ui.theme.TunerYellow
import kotlin.math.abs

@Composable
fun ChromaticDisplay(
    detectedNote: String,
    cents: Double,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val noteNames = GuitarString.ALL_NOTES
    // Extract note name without octave
    val currentNoteName = if (isActive) detectedNote.replace(Regex("\\d"), "") else ""
    val activeIndex = if (isActive) noteNames.indexOf(currentNoteName) else -1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        noteNames.forEachIndexed { index, note ->
            val isDetected = index == activeIndex

            val bgColor by animateColorAsState(
                targetValue = when {
                    !isDetected -> MaterialTheme.colorScheme.surface
                    abs(cents) <= 2.0 -> TunerGreen
                    abs(cents) <= 5.0 -> TunerYellow
                    else -> TunerRed
                },
                animationSpec = tween(150),
                label = "noteBg"
            )

            val textColor by animateColorAsState(
                targetValue = when {
                    isDetected -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                },
                animationSpec = tween(150),
                label = "noteText"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = note,
                    fontSize = 11.sp,
                    fontWeight = if (isDetected) FontWeight.Bold else FontWeight.Normal,
                    color = textColor,
                    maxLines = 1
                )
            }
        }
    }
}

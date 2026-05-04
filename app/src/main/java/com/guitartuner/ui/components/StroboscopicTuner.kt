package com.guitartuner.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guitartuner.ui.theme.TunerGreen
import com.guitartuner.ui.theme.TunerRed
import com.guitartuner.ui.theme.TunerYellow
import kotlin.math.abs

@Composable
fun StroboscopicTuner(
    cents: Double,
    frequency: Double,
    detectedNote: String,
    isActive: Boolean,
    centsRange: Double = 50.0,
    tuningHint: String = "",
    modifier: Modifier = Modifier
) {
    val barColor by animateColorAsState(
        targetValue = when {
            !isActive -> Color.Gray.copy(alpha = 0.35f)
            abs(cents) <= 3.0 -> TunerGreen
            abs(cents) <= 10.0 -> TunerYellow
            else -> TunerRed
        },
        animationSpec = tween(250),
        label = "barColor"
    )

    val animatedCents by animateFloatAsState(
        targetValue = if (isActive) cents.toFloat().coerceIn(
            -centsRange.toFloat(), centsRange.toFloat()
        ) else 0f,
        animationSpec = tween(150),
        label = "cents"
    )

    val bgColor = MaterialTheme.colorScheme.surface
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    val gridMajorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.30f)
    val centerMarkColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
    val inactiveText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val subtleText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val range = centsRange.toFloat()
                val usableHalf = w * 0.46f

                drawRect(color = bgColor)

                // Vertical grid lines (full height)
                val maxCentsInt = range.toInt()
                for (c in -maxCentsInt..maxCentsInt) {
                    val isMajor = c % 10 == 0
                    val isMedium = c % 5 == 0
                    if (!isMajor && !isMedium) continue

                    val x = w / 2f + (c.toFloat() / range) * usableHalf

                    drawLine(
                        color = if (isMajor) gridMajorColor else gridColor,
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = if (isMajor) 1.5f else 0.8f
                    )
                }

                // Horizontal center line
                drawLine(
                    color = gridColor,
                    start = Offset(0f, h / 2f),
                    end = Offset(w, h / 2f),
                    strokeWidth = 1f
                )

                // Vertical center reference (0 cents)
                drawLine(
                    color = centerMarkColor,
                    start = Offset(w / 2f, 0f),
                    end = Offset(w / 2f, h),
                    strokeWidth = 2f
                )

                // Capsule bar
                val barW = 30f * density
                val barH = h * 0.78f
                val barCenterX = w / 2f + (animatedCents / range) * usableHalf
                val barX = barCenterX - barW / 2f
                val barY = (h - barH) / 2f
                val cr = barW / 2f

                // Glow layers (outer to inner)
                val glowSpecs = floatArrayOf(14f, 8f, 4f)
                val glowAlphas = floatArrayOf(0.06f, 0.12f, 0.20f)
                for (i in glowSpecs.indices) {
                    val expand = glowSpecs[i] * density
                    drawRoundRect(
                        color = barColor.copy(alpha = glowAlphas[i]),
                        topLeft = Offset(barX - expand, barY - expand),
                        size = Size(barW + expand * 2, barH + expand * 2),
                        cornerRadius = CornerRadius(cr + expand)
                    )
                }

                // Main capsule
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(barX, barY),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(cr)
                )

                // Highlight stripe (specular reflection)
                val hlW = barW * 0.28f
                val hlX = barX + (barW - hlW) / 2f
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.22f),
                    topLeft = Offset(hlX, barY + barH * 0.08f),
                    size = Size(hlW, barH * 0.84f),
                    cornerRadius = CornerRadius(hlW / 2f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Info row: Hz | Note | Cents
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isActive && frequency > 0) String.format("%.1f Hz", frequency) else "-- Hz",
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                color = if (isActive) subtleText else inactiveText,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            Text(
                text = detectedNote,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isActive && frequency > 0) barColor else inactiveText,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isActive && frequency > 0) String.format("%+.1f", cents) else "--",
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive && frequency > 0) barColor else inactiveText,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        if (tuningHint.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tuningHint,
                fontSize = 14.sp,
                color = barColor.copy(alpha = 0.8f)
            )
        }
    }
}

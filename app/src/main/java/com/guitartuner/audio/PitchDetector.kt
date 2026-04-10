package com.guitartuner.audio

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * YIN pitch detection algorithm implementation.
 * Detects fundamental frequency with sub-cent precision for musical tuning.
 *
 * Key improvements:
 * - Larger processing window (8192 samples) for better low-frequency resolution
 * - Tuned threshold to avoid harmonic detection
 * - Parabolic interpolation for sub-sample accuracy
 */
class PitchDetector(
    private val sampleRate: Int = 44100,
    private val threshold: Double = 0.20
) {
    /**
     * Detect pitch using the YIN algorithm.
     * @param buffer Audio samples as FloatArray (should be at least 8192 samples)
     * @return Detected frequency in Hz, or -1.0 if no pitch detected
     */
    fun detectPitch(buffer: FloatArray): Double {
        val bufferSize = buffer.size
        val halfBuffer = bufferSize / 2

        // Step 1: Difference function d(tau)
        val difference = FloatArray(halfBuffer)
        for (tau in 1 until halfBuffer) {
            var sum = 0f
            for (i in 0 until halfBuffer) {
                val delta = buffer[i] - buffer[i + tau]
                sum += delta * delta
            }
            difference[tau] = sum
        }

        // Step 2: Cumulative mean normalized difference function (CMNDF)
        val cmndf = FloatArray(halfBuffer)
        cmndf[0] = 1f
        var runningSum = 0f
        for (tau in 1 until halfBuffer) {
            runningSum += difference[tau]
            cmndf[tau] = if (runningSum > 0f) {
                difference[tau] * tau / runningSum
            } else {
                1f
            }
        }

        // Step 3: Absolute threshold - find first dip below threshold, then local min
        // Guitar range: E2 (82.41Hz) to ~E5 (659Hz) with harmonics
        // tau = sampleRate / frequency
        val minTau = (sampleRate / 900.0).toInt().coerceAtLeast(2)   // Max ~900 Hz
        val maxTau = (sampleRate / 40.0).toInt().coerceAtMost(halfBuffer - 2) // Min ~40 Hz (8-string F#1)

        var tauEstimate = -1

        // First pass: find where CMNDF dips below threshold
        var tau = minTau
        while (tau <= maxTau) {
            if (cmndf[tau] < threshold) {
                // Walk to the local minimum
                while (tau + 1 <= maxTau && cmndf[tau + 1] < cmndf[tau]) {
                    tau++
                }
                tauEstimate = tau
                break
            }
            tau++
        }

        // If no dip found below threshold, find absolute minimum
        if (tauEstimate == -1) {
            var minVal = Float.MAX_VALUE
            for (t in minTau..maxTau) {
                if (cmndf[t] < minVal) {
                    minVal = cmndf[t]
                    tauEstimate = t
                }
            }
            // Reject if confidence is too low
            if (minVal > 0.45) return -1.0
        }

        // Step 4: Parabolic interpolation for sub-sample accuracy
        val betterTau = parabolicInterpolation(cmndf, tauEstimate, halfBuffer)

        val frequency = if (betterTau > 0) sampleRate.toDouble() / betterTau else -1.0

        // Reject frequencies outside guitar range
        if (frequency < 40.0 || frequency > 900.0) return -1.0

        return frequency
    }

    private fun parabolicInterpolation(array: FloatArray, tau: Int, size: Int): Double {
        if (tau < 1 || tau >= size - 1) return tau.toDouble()

        val s0 = array[tau - 1].toDouble()
        val s1 = array[tau].toDouble()
        val s2 = array[tau + 1].toDouble()

        val denominator = 2.0 * s1 - s2 - s0
        if (abs(denominator) < 1e-12) return tau.toDouble()

        val adjustment = (s2 - s0) / (2.0 * denominator)

        return if (abs(adjustment) < 1.0) tau + adjustment else tau.toDouble()
    }

    fun calculateRMS(buffer: FloatArray): Float {
        var sum = 0.0
        for (sample in buffer) {
            sum += (sample * sample).toDouble()
        }
        val rms = sqrt(sum / buffer.size)
        return (rms * 3.0).toFloat().coerceIn(0f, 1f)
    }
}

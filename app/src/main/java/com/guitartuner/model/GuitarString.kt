package com.guitartuner.model

import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.round

enum class GuitarString(
    val noteName: String,
    val frequency: Double,
    val stringNumber: Int,
    val minStrings: Int // minimum guitar type that includes this string
) {
    Fs1("F#1", 46.25, 8, 8),
    B1("B1", 61.74, 7, 7),
    E2("E2", 82.41, 6, 6),
    A2("A2", 110.00, 5, 6),
    D3("D3", 146.83, 4, 6),
    G3("G3", 196.00, 3, 6),
    B3("B3", 246.94, 2, 6),
    E4("E4", 329.63, 1, 6);

    fun frequencyWithCalibration(a4Frequency: Double): Double {
        val ratio = a4Frequency / 440.0
        return frequency * ratio
    }

    companion object {
        private val ALL_NOTE_NAMES = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

        fun stringsForType(guitarType: GuitarType): List<GuitarString> {
            return entries.filter { it.minStrings <= guitarType.stringCount }
        }

        fun findClosest(frequency: Double, a4Frequency: Double, guitarType: GuitarType = GuitarType.SIX_STRING): GuitarString {
            val available = stringsForType(guitarType)
            return available.minBy { entry ->
                val calibrated = entry.frequencyWithCalibration(a4Frequency)
                abs(1200.0 * log2(frequency / calibrated))
            }
        }

        fun findClosestNote(frequency: Double, a4Frequency: Double = 440.0): Pair<String, Double> {
            if (frequency <= 0) return "--" to 0.0

            val semitonesFromA4 = 12.0 * log2(frequency / a4Frequency)
            val roundedSemitones = round(semitonesFromA4).toInt()

            val noteIndex = Math.floorMod(roundedSemitones + 9, 12)
            val octave = 4 + Math.floorDiv(roundedSemitones + 9, 12)

            val noteName = ALL_NOTE_NAMES[noteIndex]
            val exactFrequency = a4Frequency * 2.0.pow(roundedSemitones / 12.0)

            return "$noteName$octave" to exactFrequency
        }

        fun chromaticNoteIndex(noteName: String): Int {
            val name = noteName.replace(Regex("\\d"), "")
            return ALL_NOTE_NAMES.indexOf(name)
        }

        val ALL_NOTES: Array<String> get() = ALL_NOTE_NAMES
    }
}

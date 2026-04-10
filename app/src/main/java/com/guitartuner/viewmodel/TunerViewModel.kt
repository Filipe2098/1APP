package com.guitartuner.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.guitartuner.audio.AudioProcessor
import com.guitartuner.audio.MetronomeEngine
import com.guitartuner.data.PreferencesManager
import com.guitartuner.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log2

class TunerViewModel(application: Application) : AndroidViewModel(application) {

    private val audioProcessor = AudioProcessor()
    private val metronomeEngine = MetronomeEngine()
    private val prefsManager = PreferencesManager(application)

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val _state = MutableStateFlow(prefsManager.loadState())
    val state: StateFlow<TunerState> = _state.asStateFlow()

    private var lastVibrateTime = 0L
    private val historyMaxSize = 50

    // Median filter buffer for frequency smoothing
    private val recentFrequencies = ArrayDeque<Double>(5)

    init {
        metronomeEngine.setOnBeatListener { beat ->
            _state.update { it.copy(metronomeBeat = beat) }
        }
    }

    fun startListening(): Boolean {
        val context = getApplication<Application>()
        val started = audioProcessor.start(context)
        if (started) {
            _state.update { it.copy(isListening = true) }
            recentFrequencies.clear()
            viewModelScope.launch {
                audioProcessor.audioResults.collect { result ->
                    processAudioResult(result.frequency, result.volume)
                }
            }
        }
        return started
    }

    fun stopListening() {
        audioProcessor.stop()
        recentFrequencies.clear()
        _state.update {
            it.copy(
                isListening = false,
                detectedFrequency = 0.0,
                detectedNote = "--",
                cents = 0.0,
                volume = 0f
            )
        }
    }

    fun setCalibration(a4Freq: Double) {
        val clamped = a4Freq.coerceIn(420.0, 460.0)
        _state.update { it.copy(a4Calibration = clamped) }
        prefsManager.a4Calibration = clamped
    }

    fun setThemeMode(mode: ThemeMode) {
        _state.update { it.copy(themeMode = mode) }
        prefsManager.themeMode = mode
    }

    fun setTunerMode(mode: TunerMode) {
        _state.update { it.copy(tunerMode = mode) }
        prefsManager.tunerMode = mode
    }

    fun setLanguage(language: AppLanguage) {
        _state.update { it.copy(language = language) }
        prefsManager.language = language
    }

    fun setVibrationEnabled(enabled: Boolean) {
        _state.update { it.copy(vibrationEnabled = enabled) }
        prefsManager.vibrationEnabled = enabled
    }

    fun setGuitarType(type: GuitarType) {
        _state.update { it.copy(guitarType = type) }
        prefsManager.guitarType = type
    }

    // Metronome controls
    fun setMetronomeBpm(bpm: Int) {
        val clamped = bpm.coerceIn(40, 240)
        _state.update { it.copy(metronomeBpm = clamped) }
        prefsManager.metronomeBpm = clamped
        // Restart if playing to apply new BPM
        if (_state.value.metronomeIsPlaying) {
            metronomeEngine.stop()
            metronomeEngine.start(clamped, _state.value.metronomeBeatsPerMeasure)
        }
    }

    fun toggleMetronome() {
        val current = _state.value
        if (current.metronomeIsPlaying) {
            metronomeEngine.stop()
            _state.update { it.copy(metronomeIsPlaying = false, metronomeBeat = 0) }
        } else {
            metronomeEngine.start(current.metronomeBpm, current.metronomeBeatsPerMeasure)
            _state.update { it.copy(metronomeIsPlaying = true) }
        }
    }

    fun setMetronomeBeatsPerMeasure(beats: Int) {
        val clamped = beats.coerceIn(2, 8)
        _state.update { it.copy(metronomeBeatsPerMeasure = clamped) }
        if (_state.value.metronomeIsPlaying) {
            metronomeEngine.stop()
            metronomeEngine.start(_state.value.metronomeBpm, clamped)
        }
    }

    private fun processAudioResult(frequency: Double, volume: Float) {
        if (frequency <= 0) {
            _state.update { it.copy(volume = volume) }
            return
        }

        // Median filter: collect last 5 readings, use median for stability
        recentFrequencies.addLast(frequency)
        if (recentFrequencies.size > 5) recentFrequencies.removeFirst()

        val smoothedFrequency = if (recentFrequencies.size >= 3) {
            val sorted = recentFrequencies.sorted()
            sorted[sorted.size / 2]
        } else {
            frequency
        }

        val currentState = _state.value

        // Chromatic note detection - find the closest note in the full chromatic scale
        val (chromaticNote, chromaticFreq) = GuitarString.findClosestNote(
            smoothedFrequency, currentState.a4Calibration
        )

        val cents = 1200.0 * log2(smoothedFrequency / chromaticFreq)
        val displayCents = cents.coerceIn(-50.0, 50.0)

        val newHistory = (currentState.readingsHistory + displayCents).takeLast(historyMaxSize)

        _state.update {
            it.copy(
                detectedFrequency = smoothedFrequency,
                detectedNote = chromaticNote,
                targetFrequency = chromaticFreq,
                cents = displayCents,
                volume = volume,
                readingsHistory = newHistory
            )
        }

        // Vibrate when in tune (±2 cents)
        if (currentState.vibrationEnabled && abs(cents) <= 2.0) {
            val now = System.currentTimeMillis()
            if (now - lastVibrateTime > 500) {
                lastVibrateTime = now
                try {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } catch (_: Exception) { }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioProcessor.stop()
        metronomeEngine.stop()
    }
}

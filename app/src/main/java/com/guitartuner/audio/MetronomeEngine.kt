package com.guitartuner.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.sin

class MetronomeEngine {

    private var audioTrack: AudioTrack? = null
    private var job: Job? = null
    private var isPlaying = false

    private val sampleRate = 44100

    // Pre-generate click sounds
    private val clickHigh: ShortArray = generateClick(1000.0, 0.03)  // accent beat
    private val clickLow: ShortArray = generateClick(800.0, 0.02)    // normal beat

    private var onBeat: ((Int) -> Unit)? = null

    fun setOnBeatListener(listener: (Int) -> Unit) {
        onBeat = listener
    }

    fun start(bpm: Int, beatsPerMeasure: Int = 4) {
        stop()
        isPlaying = true

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(maxOf(bufferSize, sampleRate * 2))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.play()

        job = CoroutineScope(Dispatchers.Default).launch {
            var beat = 0
            while (isActive && isPlaying) {
                val isAccent = beat == 0
                val click = if (isAccent) clickHigh else clickLow

                withContext(Dispatchers.Main) {
                    onBeat?.invoke(beat)
                }

                audioTrack?.write(click, 0, click.size)

                // Fill remaining interval with silence
                val intervalSamples = (sampleRate * 60.0 / bpm).toInt()
                val silenceSamples = intervalSamples - click.size
                if (silenceSamples > 0) {
                    val silence = ShortArray(silenceSamples)
                    audioTrack?.write(silence, 0, silence.size)
                }

                beat = (beat + 1) % beatsPerMeasure
            }
        }
    }

    fun stop() {
        isPlaying = false
        job?.cancel()
        job = null
        try {
            audioTrack?.stop()
        } catch (_: Exception) { }
        audioTrack?.release()
        audioTrack = null
    }

    private fun generateClick(frequency: Double, durationSec: Double): ShortArray {
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            // Sine wave with exponential decay envelope
            val envelope = (1.0 - t / durationSec).let { it * it }
            val sample = sin(2.0 * PI * frequency * t) * envelope * 0.8
            samples[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }

        return samples
    }
}

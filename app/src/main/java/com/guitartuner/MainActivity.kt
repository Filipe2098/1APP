package com.guitartuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.guitartuner.i18n.StringKey
import com.guitartuner.i18n.Strings
import com.guitartuner.model.ThemeMode
import com.guitartuner.ui.MetronomeScreen
import com.guitartuner.ui.SettingsScreen
import com.guitartuner.ui.TunerScreen
import com.guitartuner.ui.theme.GuitarTunerTheme
import com.guitartuner.viewmodel.TunerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TunerViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            val msg = Strings.get(StringKey.MIC_PERMISSION, viewModel.state.value.language)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.state.collectAsState()

            // Screen navigation: 0=Tuner, 1=Settings, 2=Metronome
            var currentScreen by remember { mutableStateOf(0) }

            // Resolve theme: AUTO follows system, DARK/LIGHT override
            val isSystemDark = isSystemInDarkTheme()
            val useDarkTheme = when (state.themeMode) {
                ThemeMode.AUTO -> isSystemDark
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            GuitarTunerTheme(darkTheme = useDarkTheme) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    when (currentScreen) {
                        0 -> TunerScreen(
                            state = state,
                            onStartListening = { startTuning() },
                            onStopListening = { viewModel.stopListening() },
                            onOpenSettings = { currentScreen = 1 },
                            onOpenMetronome = { currentScreen = 2 }
                        )
                        1 -> SettingsScreen(
                            state = state,
                            onBack = { currentScreen = 0 },
                            onTunerModeChanged = { viewModel.setTunerMode(it) },
                            onThemeModeChanged = { viewModel.setThemeMode(it) },
                            onLanguageChanged = { viewModel.setLanguage(it) },
                            onCalibrationChanged = { viewModel.setCalibration(it) },
                            onVibrationChanged = { viewModel.setVibrationEnabled(it) },
                            onGuitarTypeChanged = { viewModel.setGuitarType(it) }
                        )
                        2 -> MetronomeScreen(
                            state = state,
                            onBack = { currentScreen = 0 },
                            onBpmChanged = { viewModel.setMetronomeBpm(it) },
                            onToggleMetronome = { viewModel.toggleMetronome() },
                            onBeatsChanged = { viewModel.setMetronomeBeatsPerMeasure(it) }
                        )
                    }
                }
            }
        }
    }

    private fun startTuning() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.startListening()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopListening()
    }
}

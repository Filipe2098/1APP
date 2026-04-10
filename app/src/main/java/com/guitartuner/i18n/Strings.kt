package com.guitartuner.i18n

import com.guitartuner.model.AppLanguage

object Strings {
    fun get(key: StringKey, language: AppLanguage): String {
        return translations[key]?.get(language) ?: translations[key]?.get(AppLanguage.ENGLISH) ?: key.name
    }
}

enum class StringKey {
    APP_TITLE,
    START_TUNING,
    STOP,
    PLAY,
    SETTINGS,
    TUNER_MODE,
    STROBOSCOPIC,
    NEEDLE,
    THEME,
    AUTO,
    DARK,
    LIGHT,
    LANGUAGE,
    CALIBRATION,
    CALIBRATION_DESC,
    CALIBRATION_RANGE,
    VIBRATION,
    VIBRATION_DESC,
    TIGHTEN,
    LOOSEN,
    IN_TUNE,
    SIGNAL,
    HISTORY,
    CENTS,
    BACK,
    MIC_PERMISSION,
    GUITAR_TYPE,
    STRINGS,
    METRONOME,
    TIME_SIGNATURE,
}

private val translations: Map<StringKey, Map<AppLanguage, String>> = mapOf(
    StringKey.APP_TITLE to mapOf(
        AppLanguage.PORTUGUESE to "Afinador",
        AppLanguage.ENGLISH to "Guitar Tuner",
        AppLanguage.POLISH to "Tuner gitarowy",
        AppLanguage.SPANISH to "Afinador",
        AppLanguage.FRENCH to "Accordeur",
        AppLanguage.GERMAN to "Stimmgerät",
        AppLanguage.CHINESE to "吉他调音器",
        AppLanguage.JAPANESE to "ギターチューナー",
        AppLanguage.RUSSIAN to "Тюнер"
    ),
    StringKey.START_TUNING to mapOf(
        AppLanguage.PORTUGUESE to "INICIAR",
        AppLanguage.ENGLISH to "START",
        AppLanguage.POLISH to "START",
        AppLanguage.SPANISH to "INICIAR",
        AppLanguage.FRENCH to "DÉMARRER",
        AppLanguage.GERMAN to "STARTEN",
        AppLanguage.CHINESE to "开始",
        AppLanguage.JAPANESE to "開始",
        AppLanguage.RUSSIAN to "СТАРТ"
    ),
    StringKey.STOP to mapOf(
        AppLanguage.PORTUGUESE to "PARAR",
        AppLanguage.ENGLISH to "STOP",
        AppLanguage.POLISH to "STOP",
        AppLanguage.SPANISH to "PARAR",
        AppLanguage.FRENCH to "ARRÊTER",
        AppLanguage.GERMAN to "STOPP",
        AppLanguage.CHINESE to "停止",
        AppLanguage.JAPANESE to "停止",
        AppLanguage.RUSSIAN to "СТОП"
    ),
    StringKey.PLAY to mapOf(
        AppLanguage.PORTUGUESE to "TOCAR",
        AppLanguage.ENGLISH to "PLAY",
        AppLanguage.POLISH to "GRAJ",
        AppLanguage.SPANISH to "TOCAR",
        AppLanguage.FRENCH to "JOUER",
        AppLanguage.GERMAN to "SPIELEN",
        AppLanguage.CHINESE to "播放",
        AppLanguage.JAPANESE to "再生",
        AppLanguage.RUSSIAN to "ИГРАТЬ"
    ),
    StringKey.SETTINGS to mapOf(
        AppLanguage.PORTUGUESE to "Definições",
        AppLanguage.ENGLISH to "Settings",
        AppLanguage.POLISH to "Ustawienia",
        AppLanguage.SPANISH to "Ajustes",
        AppLanguage.FRENCH to "Paramètres",
        AppLanguage.GERMAN to "Einstellungen",
        AppLanguage.CHINESE to "设置",
        AppLanguage.JAPANESE to "設定",
        AppLanguage.RUSSIAN to "Настройки"
    ),
    StringKey.TUNER_MODE to mapOf(
        AppLanguage.PORTUGUESE to "Modo do afinador",
        AppLanguage.ENGLISH to "Tuner mode",
        AppLanguage.POLISH to "Tryb tunera",
        AppLanguage.SPANISH to "Modo del afinador",
        AppLanguage.FRENCH to "Mode de l'accordeur",
        AppLanguage.GERMAN to "Tuner-Modus",
        AppLanguage.CHINESE to "调音模式",
        AppLanguage.JAPANESE to "チューナーモード",
        AppLanguage.RUSSIAN to "Режим тюнера"
    ),
    StringKey.STROBOSCOPIC to mapOf(
        AppLanguage.PORTUGUESE to "Estroboscópico",
        AppLanguage.ENGLISH to "Stroboscopic",
        AppLanguage.POLISH to "Stroboskopowy",
        AppLanguage.SPANISH to "Estroboscópico",
        AppLanguage.FRENCH to "Stroboscopique",
        AppLanguage.GERMAN to "Stroboskopisch",
        AppLanguage.CHINESE to "频闪",
        AppLanguage.JAPANESE to "ストロボ",
        AppLanguage.RUSSIAN to "Стробоскоп"
    ),
    StringKey.NEEDLE to mapOf(
        AppLanguage.PORTUGUESE to "Agulha",
        AppLanguage.ENGLISH to "Needle",
        AppLanguage.POLISH to "Wskazówka",
        AppLanguage.SPANISH to "Aguja",
        AppLanguage.FRENCH to "Aiguille",
        AppLanguage.GERMAN to "Nadel",
        AppLanguage.CHINESE to "指针",
        AppLanguage.JAPANESE to "ニードル",
        AppLanguage.RUSSIAN to "Стрелка"
    ),
    StringKey.THEME to mapOf(
        AppLanguage.PORTUGUESE to "Tema",
        AppLanguage.ENGLISH to "Theme",
        AppLanguage.POLISH to "Motyw",
        AppLanguage.SPANISH to "Tema",
        AppLanguage.FRENCH to "Thème",
        AppLanguage.GERMAN to "Design",
        AppLanguage.CHINESE to "主题",
        AppLanguage.JAPANESE to "テーマ",
        AppLanguage.RUSSIAN to "Тема"
    ),
    StringKey.AUTO to mapOf(
        AppLanguage.PORTUGUESE to "Auto",
        AppLanguage.ENGLISH to "Auto",
        AppLanguage.POLISH to "Auto",
        AppLanguage.SPANISH to "Auto",
        AppLanguage.FRENCH to "Auto",
        AppLanguage.GERMAN to "Auto",
        AppLanguage.CHINESE to "自动",
        AppLanguage.JAPANESE to "自動",
        AppLanguage.RUSSIAN to "Авто"
    ),
    StringKey.DARK to mapOf(
        AppLanguage.PORTUGUESE to "Escuro",
        AppLanguage.ENGLISH to "Dark",
        AppLanguage.POLISH to "Ciemny",
        AppLanguage.SPANISH to "Oscuro",
        AppLanguage.FRENCH to "Sombre",
        AppLanguage.GERMAN to "Dunkel",
        AppLanguage.CHINESE to "深色",
        AppLanguage.JAPANESE to "ダーク",
        AppLanguage.RUSSIAN to "Тёмная"
    ),
    StringKey.LIGHT to mapOf(
        AppLanguage.PORTUGUESE to "Claro",
        AppLanguage.ENGLISH to "Light",
        AppLanguage.POLISH to "Jasny",
        AppLanguage.SPANISH to "Claro",
        AppLanguage.FRENCH to "Clair",
        AppLanguage.GERMAN to "Hell",
        AppLanguage.CHINESE to "浅色",
        AppLanguage.JAPANESE to "ライト",
        AppLanguage.RUSSIAN to "Светлая"
    ),
    StringKey.LANGUAGE to mapOf(
        AppLanguage.PORTUGUESE to "Idioma",
        AppLanguage.ENGLISH to "Language",
        AppLanguage.POLISH to "Język",
        AppLanguage.SPANISH to "Idioma",
        AppLanguage.FRENCH to "Langue",
        AppLanguage.GERMAN to "Sprache",
        AppLanguage.CHINESE to "语言",
        AppLanguage.JAPANESE to "言語",
        AppLanguage.RUSSIAN to "Язык"
    ),
    StringKey.CALIBRATION to mapOf(
        AppLanguage.PORTUGUESE to "Calibração A4",
        AppLanguage.ENGLISH to "A4 Calibration",
        AppLanguage.POLISH to "Kalibracja A4",
        AppLanguage.SPANISH to "Calibración A4",
        AppLanguage.FRENCH to "Calibration A4",
        AppLanguage.GERMAN to "A4 Kalibrierung",
        AppLanguage.CHINESE to "A4 校准",
        AppLanguage.JAPANESE to "A4 キャリブレーション",
        AppLanguage.RUSSIAN to "Калибровка A4"
    ),
    StringKey.CALIBRATION_DESC to mapOf(
        AppLanguage.PORTUGUESE to "Frequência de referência",
        AppLanguage.ENGLISH to "Reference frequency",
        AppLanguage.POLISH to "Częstotliwość referencyjna",
        AppLanguage.SPANISH to "Frecuencia de referencia",
        AppLanguage.FRENCH to "Fréquence de référence",
        AppLanguage.GERMAN to "Referenzfrequenz",
        AppLanguage.CHINESE to "参考频率",
        AppLanguage.JAPANESE to "基準周波数",
        AppLanguage.RUSSIAN to "Эталонная частота"
    ),
    StringKey.CALIBRATION_RANGE to mapOf(
        AppLanguage.PORTUGUESE to "Padrão: 440 Hz (420-460 Hz)",
        AppLanguage.ENGLISH to "Standard: 440 Hz (420-460 Hz)",
        AppLanguage.POLISH to "Standard: 440 Hz (420-460 Hz)",
        AppLanguage.SPANISH to "Estándar: 440 Hz (420-460 Hz)",
        AppLanguage.FRENCH to "Standard : 440 Hz (420-460 Hz)",
        AppLanguage.GERMAN to "Standard: 440 Hz (420-460 Hz)",
        AppLanguage.CHINESE to "标准：440 Hz（420-460 Hz）",
        AppLanguage.JAPANESE to "標準：440 Hz（420-460 Hz）",
        AppLanguage.RUSSIAN to "Стандарт: 440 Гц (420-460 Гц)"
    ),
    StringKey.VIBRATION to mapOf(
        AppLanguage.PORTUGUESE to "Vibração",
        AppLanguage.ENGLISH to "Vibration",
        AppLanguage.POLISH to "Wibracje",
        AppLanguage.SPANISH to "Vibración",
        AppLanguage.FRENCH to "Vibration",
        AppLanguage.GERMAN to "Vibration",
        AppLanguage.CHINESE to "振动",
        AppLanguage.JAPANESE to "バイブレーション",
        AppLanguage.RUSSIAN to "Вибрация"
    ),
    StringKey.VIBRATION_DESC to mapOf(
        AppLanguage.PORTUGUESE to "Vibrar ao afinar corretamente",
        AppLanguage.ENGLISH to "Vibrate when in tune",
        AppLanguage.POLISH to "Wibruj po nastrojeniu",
        AppLanguage.SPANISH to "Vibrar al afinar correctamente",
        AppLanguage.FRENCH to "Vibrer à l'accord",
        AppLanguage.GERMAN to "Vibrieren bei korrekter Stimmung",
        AppLanguage.CHINESE to "调准时振动",
        AppLanguage.JAPANESE to "チューニング時にバイブ",
        AppLanguage.RUSSIAN to "Вибрация при настройке"
    ),
    StringKey.TIGHTEN to mapOf(
        AppLanguage.PORTUGUESE to "Aperte mais",
        AppLanguage.ENGLISH to "Tighten",
        AppLanguage.POLISH to "Dokręć",
        AppLanguage.SPANISH to "Aprieta más",
        AppLanguage.FRENCH to "Serrez plus",
        AppLanguage.GERMAN to "Fester drehen",
        AppLanguage.CHINESE to "拧紧",
        AppLanguage.JAPANESE to "締める",
        AppLanguage.RUSSIAN to "Затяните"
    ),
    StringKey.LOOSEN to mapOf(
        AppLanguage.PORTUGUESE to "Afrouxe",
        AppLanguage.ENGLISH to "Loosen",
        AppLanguage.POLISH to "Poluzuj",
        AppLanguage.SPANISH to "Afloja",
        AppLanguage.FRENCH to "Desserrez",
        AppLanguage.GERMAN to "Lockern",
        AppLanguage.CHINESE to "松开",
        AppLanguage.JAPANESE to "緩める",
        AppLanguage.RUSSIAN to "Ослабьте"
    ),
    StringKey.IN_TUNE to mapOf(
        AppLanguage.PORTUGUESE to "Afinado!",
        AppLanguage.ENGLISH to "In tune!",
        AppLanguage.POLISH to "Nastrojone!",
        AppLanguage.SPANISH to "¡Afinado!",
        AppLanguage.FRENCH to "Accordé !",
        AppLanguage.GERMAN to "Gestimmt!",
        AppLanguage.CHINESE to "已调准！",
        AppLanguage.JAPANESE to "チューニング完了！",
        AppLanguage.RUSSIAN to "Настроено!"
    ),
    StringKey.SIGNAL to mapOf(
        AppLanguage.PORTUGUESE to "SINAL",
        AppLanguage.ENGLISH to "SIGNAL",
        AppLanguage.POLISH to "SYGNAŁ",
        AppLanguage.SPANISH to "SEÑAL",
        AppLanguage.FRENCH to "SIGNAL",
        AppLanguage.GERMAN to "SIGNAL",
        AppLanguage.CHINESE to "信号",
        AppLanguage.JAPANESE to "信号",
        AppLanguage.RUSSIAN to "СИГНАЛ"
    ),
    StringKey.HISTORY to mapOf(
        AppLanguage.PORTUGUESE to "HISTÓRICO",
        AppLanguage.ENGLISH to "HISTORY",
        AppLanguage.POLISH to "HISTORIA",
        AppLanguage.SPANISH to "HISTORIAL",
        AppLanguage.FRENCH to "HISTORIQUE",
        AppLanguage.GERMAN to "VERLAUF",
        AppLanguage.CHINESE to "历史",
        AppLanguage.JAPANESE to "履歴",
        AppLanguage.RUSSIAN to "ИСТОРИЯ"
    ),
    StringKey.CENTS to mapOf(
        AppLanguage.PORTUGUESE to "cents",
        AppLanguage.ENGLISH to "cents",
        AppLanguage.POLISH to "centów",
        AppLanguage.SPANISH to "cents",
        AppLanguage.FRENCH to "cents",
        AppLanguage.GERMAN to "Cent",
        AppLanguage.CHINESE to "音分",
        AppLanguage.JAPANESE to "セント",
        AppLanguage.RUSSIAN to "центов"
    ),
    StringKey.BACK to mapOf(
        AppLanguage.PORTUGUESE to "Voltar",
        AppLanguage.ENGLISH to "Back",
        AppLanguage.POLISH to "Wstecz",
        AppLanguage.SPANISH to "Volver",
        AppLanguage.FRENCH to "Retour",
        AppLanguage.GERMAN to "Zurück",
        AppLanguage.CHINESE to "返回",
        AppLanguage.JAPANESE to "戻る",
        AppLanguage.RUSSIAN to "Назад"
    ),
    StringKey.MIC_PERMISSION to mapOf(
        AppLanguage.PORTUGUESE to "Permissão do microfone necessária",
        AppLanguage.ENGLISH to "Microphone permission required",
        AppLanguage.POLISH to "Wymagane uprawnienie mikrofonu",
        AppLanguage.SPANISH to "Permiso de micrófono requerido",
        AppLanguage.FRENCH to "Permission du microphone requise",
        AppLanguage.GERMAN to "Mikrofonberechtigung erforderlich",
        AppLanguage.CHINESE to "需要麦克风权限",
        AppLanguage.JAPANESE to "マイクの許可が必要です",
        AppLanguage.RUSSIAN to "Требуется разрешение микрофона"
    ),
    StringKey.GUITAR_TYPE to mapOf(
        AppLanguage.PORTUGUESE to "Tipo de guitarra",
        AppLanguage.ENGLISH to "Guitar type",
        AppLanguage.POLISH to "Typ gitary",
        AppLanguage.SPANISH to "Tipo de guitarra",
        AppLanguage.FRENCH to "Type de guitare",
        AppLanguage.GERMAN to "Gitarrentyp",
        AppLanguage.CHINESE to "吉他类型",
        AppLanguage.JAPANESE to "ギタータイプ",
        AppLanguage.RUSSIAN to "Тип гитары"
    ),
    StringKey.STRINGS to mapOf(
        AppLanguage.PORTUGUESE to "cordas",
        AppLanguage.ENGLISH to "strings",
        AppLanguage.POLISH to "strun",
        AppLanguage.SPANISH to "cuerdas",
        AppLanguage.FRENCH to "cordes",
        AppLanguage.GERMAN to "Saiten",
        AppLanguage.CHINESE to "弦",
        AppLanguage.JAPANESE to "弦",
        AppLanguage.RUSSIAN to "струн"
    ),
    StringKey.METRONOME to mapOf(
        AppLanguage.PORTUGUESE to "Metrónomo",
        AppLanguage.ENGLISH to "Metronome",
        AppLanguage.POLISH to "Metronom",
        AppLanguage.SPANISH to "Metrónomo",
        AppLanguage.FRENCH to "Métronome",
        AppLanguage.GERMAN to "Metronom",
        AppLanguage.CHINESE to "节拍器",
        AppLanguage.JAPANESE to "メトロノーム",
        AppLanguage.RUSSIAN to "Метроном"
    ),
    StringKey.TIME_SIGNATURE to mapOf(
        AppLanguage.PORTUGUESE to "Compasso",
        AppLanguage.ENGLISH to "Time signature",
        AppLanguage.POLISH to "Metrum",
        AppLanguage.SPANISH to "Compás",
        AppLanguage.FRENCH to "Mesure",
        AppLanguage.GERMAN to "Taktart",
        AppLanguage.CHINESE to "拍号",
        AppLanguage.JAPANESE to "拍子",
        AppLanguage.RUSSIAN to "Размер"
    ),
)

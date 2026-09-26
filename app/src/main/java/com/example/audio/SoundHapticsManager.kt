package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundHapticsManager(private val context: Context) {

    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (e: Exception) {
        null
    }

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (e: Exception) {
            toneGenerator = null
        }
    }

    fun playTick(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        }
        if (vibrateEnabled) {
            vibrate(30)
        }
    }

    fun playWarningTick(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE, 80)
        }
        if (vibrateEnabled) {
            vibrate(60)
        }
    }

    fun playDramaticReveal(isSpy: Boolean, soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            if (isSpy) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 250)
            } else {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
            }
        }
        if (vibrateEnabled) {
            vibrate(if (isSpy) 150 else 80)
        }
    }

    fun playVictoryChime(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
        }
        if (vibrateEnabled) {
            vibrate(200)
        }
    }

    fun playDefeatChime(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        if (soundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_ERROR, 350)
        }
        if (vibrateEnabled) {
            vibrate(300)
        }
    }

    private fun vibrate(millis: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(millis)
            }
        } catch (e: Exception) {
            // Ignore vibration errors gracefully
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (e: Exception) {
            // Ignore
        }
    }
}

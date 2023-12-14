package com.example.coopt1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() , OnInitListener {


    private lateinit var textToSpeech: TextToSpeech // declares a variable of type TextToSpeech
    private lateinit var textToSpeak: TextView
    private lateinit var speakButton: Button
    private lateinit var seekBarPitch: SeekBar
    private lateinit var seekBarSpeed: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textToSpeech = TextToSpeech(this, this) // initiate TextToSpeech
        textToSpeak = findViewById(R.id.textToSpeak) // refers to the TextView with id "textToSpeak"
        speakButton = findViewById(R.id.speakButton)

        seekBarPitch = findViewById(R.id.seek_bar_pitch) // refers to SeekBar of Pitch
        seekBarSpeed = findViewById(R.id.seek_bar_speed) // refers to SeekBar of Speed

        // Set the speech rate(0.7)
        //val speechRate = 0.7f
        var pitch: Float = seekBarPitch.progress / 50f
        if (pitch < 0.1f) pitch = 0.1f

        var speed: Float = seekBarSpeed.progress / 50f
        if (speed < 0.1f) speed = 0.1f

        textToSpeech.setPitch(pitch)
        textToSpeech.setSpeechRate(speed)

        speakButton.setOnClickListener {
            val text = textToSpeak.text.toString() // Get text from TextView with id "textToSpeak"
            if (text.isNotEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // QUEUE_FLUSH, stops the ongoing speech and replaces new one (can use QUEUE_ADD to add new one to line)
            }
        }


        // Set OnSeekBarChangeListener for pitch SeekBar
        seekBarPitch.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pitch = progress / 50f
                textToSpeech.setPitch(pitch)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // No specific action needed when starting to track touch
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // No specific action needed when stopping to track touch
            }
        })

        // Set OnSeekBarChangeListener for speed SeekBar
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speed = progress / 50f
                textToSpeech.setSpeechRate(speed)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // No specific action needed when starting to track touch
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // No specific action needed when stopping to track touch
            }
        })

    }

    // override of the onInit method from the TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language as English
            val result = textToSpeech.setLanguage(Locale.US)

            // the language data is missing OR not supported
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Text to Speech not supported on this device.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Text to Speech failed.", Toast.LENGTH_SHORT).show()
        }
    }

    // overrides the onDestroy method from the Activity class
    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown() // turns off textToSpeech feature
        super.onDestroy()
    }
}


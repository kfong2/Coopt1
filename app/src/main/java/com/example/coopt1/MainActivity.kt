package com.example.coopt1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() , OnInitListener {

    private lateinit var textToSpeech: TextToSpeech // declares a variable of type TextToSpeech
    private lateinit var textToSpeak: TextView
    private lateinit var speakButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textToSpeech = TextToSpeech(this, this) // initiate TextToSpeech
        textToSpeak = findViewById(R.id.textToSpeak) // refers to the TextView with id "textToSpeak"
        speakButton = findViewById(R.id.speakButton)

        // Set the speech rate(0.7)
        val speechRate = 0.7f
        textToSpeech.setSpeechRate(speechRate)

        speakButton.setOnClickListener {
            val text = textToSpeak.text.toString() // Get text from TextView with id "textToSpeak"
            if (text.isNotEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // QUEUE_FLUSH, stops the ongoing speech and replaces new one (can use QUEUE_ADD to add new one to line)
            }
        }
    }

    // override of the onInit method from the TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
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
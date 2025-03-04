package com.example.m08_projecta2

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class Splash : AppCompatActivity() {

    private val duracio: Long = 3000
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        // Iniciamos el audio
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)
        mediaPlayer?.start()

        canviarActivity()
    }

    private fun canviarActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            mediaPlayer?.release() // Liberamos el audio al cambiar de pantalla
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, duracio)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}

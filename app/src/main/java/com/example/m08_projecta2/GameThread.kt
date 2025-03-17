package com.example.m08_projecta2

import android.os.Bundle
import android.view.SurfaceHolder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameThread(private val holder: SurfaceHolder, private val gameView: GameView) : Thread() {
    var running = false
    override fun run() {
        while (running) {
            val canvas = holder.lockCanvas()
            synchronized(holder) {
                gameView.update()
                gameView.draw(canvas)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
package com.example.m08_projecta2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class Pipe(offsetX: Int = 0) {
    var x = 1000f + offsetX
    private val gap = 300f
    private val height = Random.nextInt(300, 800).toFloat()
    private val paint = Paint().apply { color = Color.GREEN }

    fun update() {
        x -= 10f
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(x, 0f, x + WIDTH, height, paint)
        canvas.drawRect(x, height + gap, x + WIDTH, 1600f, paint)
    }

    fun checkCollision(bird: Bird): Boolean {
        val birdBounds = bird.getBounds()
        return (birdBounds.right > x && birdBounds.left < x + WIDTH &&
                (birdBounds.top < height || birdBounds.bottom > height + gap))
    }

    companion object {
        const val WIDTH = 200f
    }
}

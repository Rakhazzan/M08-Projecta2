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

class Pipe(offsetX: Int = 0, private val gap: Float = 400f) {
    var x = 1000f + offsetX
    private val topHeight = Random.nextInt(200, 600).toFloat()
    private val bottomHeight = topHeight + gap
    private val paint = Paint().apply { color = Color.GREEN }

    fun update() {
        x -= 10f
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(x, 0f, x + WIDTH, topHeight, paint)
        canvas.drawRect(x, bottomHeight, x + WIDTH, 1600f, paint)
    }

    fun checkCollision(bird: Bird): Boolean {
        val birdBounds = bird.getBounds()
        return (birdBounds.right > x && birdBounds.left < x + WIDTH &&
                (birdBounds.top < topHeight || birdBounds.bottom > bottomHeight))
    }

    companion object {
        const val WIDTH = 200f
    }
}

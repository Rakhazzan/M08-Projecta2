package com.example.m08_projecta2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Bird {
    private var x = 200f
    private var y = 500f
    private var velocity = 0f
    private val paint = Paint().apply { color = Color.YELLOW }
    private val radius = 50f

    fun update() {
        velocity += 1.5f
        y += velocity
        if (y > 1500) velocity = 0f
    }

    fun jump() {
        velocity = -20f
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
    }

    fun getBounds(): RectF {
        return RectF(x - radius, y - radius, x + radius, y + radius)
    }
}

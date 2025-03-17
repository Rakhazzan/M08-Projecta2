package com.example.m08_projecta2


import android.content.Context
import android.graphics.*

import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class  GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: GameThread
    private val bird = Bird()
    private val pipes = mutableListOf<Pipe>()
    private var score = 0

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        spawnPipes()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.running = false
        thread.join()
    }

    override fun onTouchEvent(event:    MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            bird.jump()
        }
        return true
    }

    fun update() {
        bird.update()
        for (pipe in pipes) {
            pipe.update()
            if (pipe.checkCollision(bird)) {
                thread.running = false // Fin del juego
            }
        }
        if (pipes.first().x + Pipe.WIDTH < 0) {
            pipes.removeAt(0)
            pipes.add(Pipe())
            score++
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.CYAN)
        bird.draw(canvas)
        for (pipe in pipes) {
            pipe.draw(canvas)
        }
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 100f
        }
        canvas.drawText("Score: $score", 50f, 100f, paint)
    }

    private fun spawnPipes() {
        for (i in 0..3) {
            pipes.add(Pipe(i * 600))
        }
    }
}
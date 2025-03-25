package com.example.m08_projecta2

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: GameThread
    private val bird = Bird()
    private val pipes = mutableListOf<Pipe>()
    private var score = 0
    private val pipeGap = 400f
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance("https://montserratak-3a32a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("JUGADORS")

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
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
                thread.running = false
                saveScore()
                onGameOver()
                return
            }
        }
        if (pipes.first().x + Pipe.WIDTH < 0) {
            pipes.removeAt(0)
            pipes.add(Pipe(1800, pipeGap))
            score++
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.CYAN)
        bird.draw(canvas)
        pipes.forEach { it.draw(canvas) }

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 100f
        }
        canvas.drawText("Score: $score", 50f, 100f, paint)
    }

    private fun spawnPipes() {
        for (i in 0..3) {
            pipes.add(Pipe(i * 600, pipeGap))
        }
    }

    private fun onGameOver() {
        post {
            val intent = Intent(context, Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    private fun saveScore() {
        val user = auth.currentUser ?: return
        val userId = user.uid
        val userRef = database.child(userId)

        userRef.child("Puntuacio").get().addOnSuccessListener { snapshot ->
            val bestScore = snapshot.getValue(Long::class.java) ?: 0L

            if (score > bestScore) {
                userRef.child("Puntuacio").setValue(score)
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Error al obtener la puntuación", it)
        }
    }
}

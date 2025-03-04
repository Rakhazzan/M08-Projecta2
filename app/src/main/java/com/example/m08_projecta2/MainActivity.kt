package com.example.m08_projecta2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btmlogin = findViewById<Button>(R.id.BTMLOGIN)
        val btmregistro = findViewById<Button>(R.id.BTMREGISTRO)

        btmlogin.setOnClickListener {
            Toast.makeText(this, "Click en botón Login", Toast.LENGTH_LONG).show()
        }

        btmregistro.setOnClickListener {
            Toast.makeText(this, "Redirigiendo a Registro...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Registro::class.java)  // Llama a la actividad Registro
            startActivity(intent)  // Inicia la nueva actividad
        }
    }
}
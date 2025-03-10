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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
lateinit var auth: FirebaseAuth
var user: FirebaseUser? = null;
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val btmlogin = findViewById<Button>(R.id.BTMLOGIN)
        val btmregistro = findViewById<Button>(R.id.BTMREGISTRO)

        btmlogin.setOnClickListener {
            val intent= Intent(this, Login::class.java)
            startActivity(intent)
        }

        btmregistro.setOnClickListener {
            Toast.makeText(this, "Redirigiendo a Registro...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Registro::class.java)  // Llama a la actividad Registro
            startActivity(intent)  // Inicia la nueva actividad
        }
    }
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
    private fun usuariLogejat() {
        if (user !=null)
        {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
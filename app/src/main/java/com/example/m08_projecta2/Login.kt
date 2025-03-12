package com.example.m08_projecta2

import android.graphics.Typeface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    private lateinit var correoLogin: EditText
    private lateinit var passLogin: EditText
    private lateinit var BtnLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tf = Typeface.createFromAsset(assets, "fonts/mars.ttf")

        correoLogin = findViewById(R.id.correoLogin)
        passLogin = findViewById(R.id.passLogin)
        BtnLogin = findViewById(R.id.BtnLogin)
        auth = FirebaseAuth.getInstance()

        // Asignamos la fuente personalizada a los campos de texto y botón
        correoLogin.typeface = tf
        passLogin.typeface = tf
        BtnLogin.typeface = tf

        BtnLogin.setOnClickListener {
            val email = correoLogin.text.toString()
            val passw = passLogin.text.toString()

            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> correoLogin.error = "Invalid Mail"
                passw.length < 6 -> passLogin.error = "Password less than 6 chars"
                else -> LogindeJugador(email, passw)
            }
        }
    }

    private fun LogindeJugador(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val tx = "Benvingut $email"
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "ERROR Autentificació", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "ERROR: User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}

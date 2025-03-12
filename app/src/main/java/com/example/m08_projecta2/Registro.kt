package com.example.m08_projecta2

import android.content.Intent
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {

    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nombreEt: EditText
    private lateinit var fechaTxt: TextView
    private lateinit var Registrar: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val tf = Typeface.createFromAsset(assets, "fonts/mars.ttf")

        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaTxt = findViewById(R.id.fechaTxt)
        Registrar = findViewById(R.id.Registrar)
        auth = FirebaseAuth.getInstance()

        // Asignamos la fuente personalizada a los campos de texto y botón
        correoEt.typeface = tf
        passEt.typeface = tf
        nombreEt.typeface = tf
        fechaTxt.typeface = tf
        Registrar.typeface = tf

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formattedDate = formatter.format(date)
        fechaTxt.text = formattedDate

        Registrar.setOnClickListener {
            val email = correoEt.text.toString()
            val pass = passEt.text.toString()

            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> correoEt.error = "Invalid Mail"
                pass.length < 6 -> passEt.error = "Password less than 6 chars"
                else -> registrarUsuario(email, pass)
            }
        }
    }

    private fun registrarUsuario(email: String, passw: String) {
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)

                    val uidString = user?.uid ?: ""
                    val puntuacion = 0

                    val dadesJugador = hashMapOf(
                        "Uid" to uidString,
                        "Email" to email,
                        "Password" to passw,
                        "Nom" to nombreEt.text.toString(),
                        "Data" to fechaTxt.text.toString(),
                        "Puntuacio" to puntuacion.toString()
                    )

                    val database = FirebaseDatabase.getInstance(
                        "https://montserratak-3a32a-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    val reference = database.getReference("JUGADORS")

                    reference.child(uidString).setValue(dadesJugador)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado correctamente en BD", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar en BD", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(baseContext, "Error en la autenticación", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "ERROR: User creation failed", Toast.LENGTH_SHORT).show()
        }
    }
}

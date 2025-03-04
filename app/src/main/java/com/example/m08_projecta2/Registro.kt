package com.example.m08_projecta2

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {
    lateinit var correoEt: EditText
    lateinit var passEt: EditText
    lateinit var nombreEt: EditText
    lateinit var fechaTxt: TextView
    lateinit var Registrar: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaTxt = findViewById(R.id.fechaTxt)
        Registrar = findViewById(R.id.Registrar)
        auth = FirebaseAuth.getInstance()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formattedDate = formatter.format(date)
        fechaTxt.text = formattedDate

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

    fun registrarUsuario(email: String, passw: String) {
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)

                    // Obtener UID del usuario registrado
                    val uidString = user?.uid ?: ""
                    val puntuacio = 0

                    // Crear HashMap con los datos del jugador
                    val dadesJugador = hashMapOf(
                        "Uid" to uidString,
                        "Email" to email,
                        "Password" to passw,
                        "Nom" to nombreEt.text.toString(),
                        "Data" to fechaTxt.text.toString(),
                        "Puntuacio" to puntuacio.toString()
                    )

                    // Conectar a Firebase Realtime Database
                    val database = FirebaseDatabase.getInstance(
                        "https://montserratak-3a32a-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    val reference = database.getReference("JUGADORS")

                    // Guardar los datos en la base de datos
                    reference.child(uidString).setValue(dadesJugador)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado correctamente en BD", Toast.LENGTH_SHORT).show()
                            finish() // Cierra la actividad después del registro
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar en BD", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(baseContext, "Error en la autenticación", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val puntuacion = 0
            val uidString = user.uid
            val correoString = correoEt.text.toString()
            val passString = passEt.text.toString()
            val nombreString = nombreEt.text.toString()
            val fechaString = fechaTxt.text.toString()
        } else {
            mostrarError()
        }

    }

    fun mostrarError() {
        Toast.makeText(this, "ERROR: User creation failed", Toast.LENGTH_SHORT).show()
    }
}
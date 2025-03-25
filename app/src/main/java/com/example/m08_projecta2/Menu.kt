package com.example.m08_projecta2

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Menu : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tancarSessio: Button
    private var user: FirebaseUser? = null
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView

    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        val tf = Typeface.createFromAsset(assets, "fonts/mars.ttf")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tancarSessio = findViewById(R.id.tancarSessio)
        CreditsBtn = findViewById(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById(R.id.PuntuacionsBtn)
        jugarBtn = findViewById(R.id.jugarBtn)
        auth = FirebaseAuth.getInstance()
        miPuntuaciotxt = findViewById(R.id.miPuntuaciotxt)
        puntuacio = findViewById(R.id.puntuacio)
        uid = findViewById(R.id.uid)
        correo = findViewById(R.id.correo)
        nom = findViewById(R.id.nom)

        user = auth.currentUser

        tancarSessio.setOnClickListener {
            tancalaSessio()
        }
        CreditsBtn.setOnClickListener {
            Toast.makeText(this, "Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener {
            Toast.makeText(this, "Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener {
            val intent = Intent(this, FlappyBirdActivity::class.java)
            startActivity(intent)
        }

        // Aplicar fuente a los textos y botones
        listOf(miPuntuaciotxt, puntuacio, uid, correo, nom, tancarSessio, CreditsBtn, PuntuacionsBtn, jugarBtn)
            .forEach { it.typeface = tf }

        consulta()
    }

    private fun consulta() {
        val database = FirebaseDatabase.getInstance("https://montserratak-3a32a-default-rtdb.europe-west1.firebasedatabase.app/")
        val bdreference = database.getReference("JUGADORS")

        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DEBUG", "Datos recibidos: ${snapshot.value}")

                var trobat = false
                for (ds in snapshot.children) {
                    val emailDb = ds.child("Email").getValue(String::class.java)
                    Log.i("DEBUG", "Comparando ${emailDb} con ${user?.email}")

                    if (emailDb == user?.email) {
                        trobat = true
                        val puntuacionValor = ds.child("Puntuacio").getValue(Long::class.java) ?: 0L
                        puntuacio.text = puntuacionValor.toString()
                        uid.text = ds.child("Uid").getValue(String::class.java) ?: "N/A"
                        correo.text = emailDb ?: "N/A"
                        nom.text = ds.child("Nom").getValue(String::class.java) ?: "N/A"
                        Log.i("DEBUG", "Usuario encontrado: ${nom.text}, Puntuación: ${puntuacio.text}")
                        break
                    }
                }
                if (!trobat) {
                    Log.e("ERROR", "Usuario no encontrado en la base de datos")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "Error en la base de datos: ${error.message}")
            }
        })
    }

    private fun tancalaSessio() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        Usuarilogejat()
    }

    private fun Usuarilogejat() {
        if (user != null) {
            Toast.makeText(this, "Jugador logejat", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        consulta()
    }
}
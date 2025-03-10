package com.example.m08_projecta2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Menu : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    lateinit var tancarSessio: Button
    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        auth= FirebaseAuth.getInstance()
        user =auth.currentUser
        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }
    }
    private fun Usuarilogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onStart() {
        Usuarilogejat()
        super.onStart()
    }
    private fun tancalaSessio() {
        auth.signOut()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
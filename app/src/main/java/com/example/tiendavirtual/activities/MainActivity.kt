package com.example.tiendavirtual.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tiendavirtual.R
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_introduction)
        val starLogin = findViewById<Button>(R.id.startLogin)
        starLogin.setOnClickListener {
            val actividadRegistrar = Intent(this,Login::class.java)
            startActivity(actividadRegistrar)
        }
    }
}
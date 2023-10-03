package com.example.tiendavirtual.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tiendavirtual.R
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Analytic event
        FirebaseApp.initializeApp(this)
        val analytic = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","La conexion es exitosa")
        analytic.logEvent("InitedScreen",bundle)
        val btnLogin = findViewById<Button>(R.id.login)
        val user = findViewById<EditText>(R.id.usuario)
        val userPass = findViewById<EditText>(R.id.passwordUser)
        val registrar = findViewById<TextView>(R.id.registrarText)

        btnLogin.setOnClickListener {
            if (!(user.text.isNullOrEmpty() && userPass.text.isNullOrEmpty())) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(user.text.toString(),
                        userPass.text.toString()).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val panelUsuario = Intent(this, CodigoAcceso::class.java)
                            startActivity(panelUsuario)
                        } else {
                            Toast.makeText(this, "El usuario y la contraseña no son correctas", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "El usuario y la contraseña no pueden ir vacíos", Toast.LENGTH_SHORT).show()
            }
        }
        registrar.setOnClickListener {
            val registro = Intent(this, Register::class.java)
            startActivity(registro)
        }


    }
}
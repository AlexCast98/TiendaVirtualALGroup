package com.example.tiendavirtual.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendavirtual.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar Firebase Authentication
        FirebaseApp.initializeApp(this)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        val name = findViewById<EditText>(R.id.name)
        val user = findViewById<EditText>(R.id.email)
        val userPass = findViewById<EditText>(R.id.password)

        // Configurar el botón de registro
        registerButton.setOnClickListener {
            if (!(user.text.isNullOrEmpty() && userPass.text.isNullOrEmpty())) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(user.text.toString(),
                        userPass.text.toString()).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            // El registro fue exitoso, obtén la instancia actual del usuario
                            val firebaseUser = FirebaseAuth.getInstance().currentUser

                            // Actualiza los datos adicionales (metadata) del usuario
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name.text.toString()) // Establece el nombre del usuario
                                .build()

                            firebaseUser?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        // Datos adicionales (metadata) actualizados con éxito
                                        val panelUsuario = Intent(this, Login::class.java)
                                        startActivity(panelUsuario)
                                    } else {
                                        // Error al actualizar los datos adicionales (metadata)
                                        Toast.makeText(this, "Error al actualizar el nombre del usuario", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // Hubo un error durante el registro, muestra un mensaje de error
                            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "El usuario y la contraseña no pueden ir vacíos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el enlace para iniciar sesión
        btnLogin.setOnClickListener {
            finish()
        }
    }
}
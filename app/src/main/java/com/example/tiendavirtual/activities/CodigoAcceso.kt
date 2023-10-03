package com.example.tiendavirtual.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.TextView
import com.example.tiendavirtual.R
import com.google.firebase.ktx.Firebase

import java.util.Random

class CodigoAcceso : AppCompatActivity(){
    private lateinit var timer: CountDownTimer
    private var generatedCode: String = ""

    // Definición de variables faltantes
    private lateinit var codeEditText: EditText
    private lateinit var verifyButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var codeTextView: TextView
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo_acceso)




        // Inicialización de las variables faltantes
        codeEditText = findViewById(R.id.pinUser)
        verifyButton = findViewById(R.id.btn_codigo)
        timerTextView = findViewById(R.id.contador)
        codeTextView = findViewById(R.id.pinAleatorio)
        resultTextView = findViewById(R.id.pin_valid)

        generateCode()

        verifyButton.setOnClickListener {
            val enteredCode = codeEditText.text.toString()
            if (enteredCode == generatedCode) {
                timer.cancel()
                resultTextView.text = "Código Correcto"
                // Iniciar la nueva actividad cuando el código es correcto
                val intent = Intent(this, panelUsuario::class.java)
                startActivity(intent)
            } else {
                generateCode()
                resultTextView.text = "Código Incorrecto"
            }

            // Detener el temporizador actual
            timer.cancel()

            // Iniciar un nuevo temporizador
            timer = object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    timerTextView.text = "Tiempo restante: $secondsRemaining segundos"
                }

                override fun onFinish() {
                    generateCode()
                    timer.start()
                }
            }.start()
        }

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Tiempo restante: $secondsRemaining segundos"
            }

            override fun onFinish() {
                generateCode()
                timer.start()
            }
        }.start()
    }

    private fun generateCode() {
        val random = Random()
        val code = StringBuilder()
        for (i in 0 until 5) {
            code.append(random.nextInt(10))
        }
        generatedCode = code.toString()
        codeTextView.text = generatedCode
        resultTextView.text = ""

    }
}
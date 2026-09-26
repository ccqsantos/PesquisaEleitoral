// app/src/main/java/com/example/pesquisaeleitoral/LoginActivity.kt
package com.example.pesquisaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inLogin = findViewById<EditText>(R.id.inLogin)
        val inSenha = findViewById<EditText>(R.id.inSenha)

        findViewById<Button>(R.id.btLogin).setOnClickListener {
            val usuario = inLogin.text.toString().trim()
            val senha = inSenha.text.toString().trim()

            when {
                usuario == "entrevistador" && senha == "entrevistador" -> {
                    startActivity(Intent(this, PesquisaAbertaActivity::class.java))
                }
                usuario == "admin" && senha == "admin" -> {
                    startActivity(Intent(this, ResultadosActivity::class.java))
                }
                else -> {
                    Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btFinalizar).setOnClickListener {
            finish()
        }
    }
}
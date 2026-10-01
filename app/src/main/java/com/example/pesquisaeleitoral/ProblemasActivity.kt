package com.example.pesquisaeleitoral

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProblemasActivity : AppCompatActivity() {

    private val LIMITE_PROBLEMAS = 3

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_problemas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mapeia cada CheckBox ao seu rótulo
        val checkboxes = mapOf(
            findViewById<CheckBox>(R.id.cbSaude)          to "Saúde",
            findViewById<CheckBox>(R.id.cbSeguranca)      to "Segurança",
            findViewById<CheckBox>(R.id.cbEducacao)       to "Educação",
            findViewById<CheckBox>(R.id.cbTurismo)        to "Turismo",
            findViewById<CheckBox>(R.id.cbAgricultura)    to "Agricultura",
            findViewById<CheckBox>(R.id.cbPecuaria)       to "Pecuária",
            findViewById<CheckBox>(R.id.cbExtrativismo)   to "Extrativismo"
        )

        // Impede marcar mais de 3: se já tem 3 marcados, desmarca o novo
        checkboxes.keys.forEach { cb ->
            cb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && checkboxes.keys.count { it.isChecked } > LIMITE_PROBLEMAS) {
                    cb.isChecked = false
                    Toast.makeText(
                        this,
                        "Selecione no máximo $LIMITE_PROBLEMAS problemas.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        findViewById<Button>(R.id.btConfirmar).setOnClickListener {
            val selecionados = checkboxes
                .filter { it.key.isChecked }
                .map { it.value }

            if (selecionados.size != LIMITE_PROBLEMAS) {
                Toast.makeText(
                    this,
                    "Selecione exatamente $LIMITE_PROBLEMAS problemas.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val intent = Intent(this, UserData::class.java).apply {
                putExtra("voto_aberto", getIntent().getStringExtra("voto_aberto"))
                putExtra("voto_estimulado", getIntent().getStringExtra("voto_estimulado"))
                putStringArrayListExtra("problemas", ArrayList(selecionados))
            }
            startActivity(intent)
        }
    }
}
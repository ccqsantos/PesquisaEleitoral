package com.example.pesquisaeleitoral

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaEspecificaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_especifica)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerCandidatos = findViewById<Spinner>(R.id.spinnerCandidatos)
        val opcoes = arrayOf("Julio Campos", "João Campos", "Fábio Pecorito", "Guilherme Lollos", "José Saramago", "Júlia Pacata", "Mário Andrade", "João Pedro da Penha", "Beatriz Sousa", "Maciel Marcio Tulio", "Jorge José", "Branco", "Nulo", "Não Sei")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCandidatos.adapter = adapter

        val intent = Intent(this, ProblemasActivity::class.java)
        intent.putExtra("voto_aberto", getIntent().getStringExtra("voto_aberto"))
        intent.putExtra("voto_estimulado", spinnerCandidatos.selectedItem.toString())
        startActivity(intent)


    }
}
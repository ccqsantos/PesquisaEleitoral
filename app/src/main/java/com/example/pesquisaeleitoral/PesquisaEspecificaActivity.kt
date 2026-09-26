// app/src/main/java/com/example/pesquisaeleitoral/PesquisaEspecificaActivity.kt
package com.example.pesquisaeleitoral

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pesquisaeleitoral.data.Candidato
import com.example.pesquisaeleitoral.data.CandidatosMock

class PesquisaEspecificaActivity : AppCompatActivity() {

    private var candidatoSelecionado: Candidato? = null

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

        val recycler = findViewById<RecyclerView>(R.id.listaCandidatos)
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = CandidatoAdapter(CandidatosMock.listar()) { candidato ->
            candidatoSelecionado = candidato
        }
        recycler.adapter = adapter

        findViewById<Button>(R.id.btEnviar2).setOnClickListener {
            val selecionado = candidatoSelecionado
            if (selecionado == null) {
                Toast.makeText(this, "Selecione um candidato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, ProblemasActivity::class.java)
            intent.putExtra("voto_aberto", getIntent().getStringExtra("voto_aberto"))
            intent.putExtra("voto_estimulado", selecionado.nome)
            startActivity(intent)
        }
    }
}
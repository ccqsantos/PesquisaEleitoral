// app/src/main/java/com/example/pesquisaeleitoral/DadosActivity.kt
package com.example.pesquisaeleitoral

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import kotlinx.coroutines.launch

class DadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val totalEntrevistados = findViewById<TextView>(R.id.totalEntrevistados)
        val dadosPesquisa = findViewById<TextView>(R.id.dadosPesquisa)

        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(this@DadosActivity).entrevistadoDao()
            totalEntrevistados.text = dao.contarTotal().toString()
            val votos = dao.contarVotosPorCandidato()
            dadosPesquisa.text = votos.joinToString("\n") { "${it.candidato}: ${it.votos}" }
        }
    }
}
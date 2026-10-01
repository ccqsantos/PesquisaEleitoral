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

    private val problemas = listOf(
        "Saúde", "Segurança", "Educação", "Turismo",
        "Agricultura", "Pecuária", "Extrativismo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val totalEntrevistados =
            findViewById<TextView>(R.id.totalEntrevistados)

        val dadosPesquisa =
            findViewById<TextView>(R.id.dadosPesquisa)

        val dadosVotoAberto =
            findViewById<TextView>(R.id.dadosVotoAberto)

        val dadosProblemas =
            findViewById<TextView>(R.id.dadosProblemas)

        lifecycleScope.launch {

            val dao = AppDatabase
                .getInstance(this@DadosActivity)
                .entrevistadoDao()

            val total = dao.contarTotal()

            totalEntrevistados.text = total.toString()

            val votosEstimulado = dao.contarVotosPorCandidato()

            dadosPesquisa.text = if (votosEstimulado.isEmpty()) {
                "Nenhum voto registrado"

            } else {

                votosEstimulado.joinToString("\n") { item ->
                    val pct =
                        if (total > 0) {
                            item.votos * 100.0 / total
                        } else {
                            0.0
                        }
                    "${item.candidato}: ${item.votos} (%.1f%%)"
                        .format(pct)
                }
            }

            val votosAberto = dao.contarVotosAbertoPorCandidato()

            dadosVotoAberto.text = if (votosAberto.isEmpty()) {
                "Nenhum voto registrado"

            } else {
                votosAberto.joinToString("\n") { item ->
                    val pct =
                        if (total > 0) {
                            item.votos * 100.0 / total
                        } else {
                            0.0
                        }
                    "${item.candidato}: ${item.votos} (%.1f%%)"
                        .format(pct)
                }
            }
            val contagens = mutableListOf<Pair<String, Int>>()

            for (p in problemas) {
                val qtd = dao.contarPorProblema(p)
                if (qtd > 0) {
                    contagens.add(p to qtd)
                }
            }

            // Ordena do problema mais votado para o menos votado
            contagens.sortByDescending { it.second }

            dadosProblemas.text = if (contagens.isEmpty()) {
                "Nenhum problema registrado"

            } else {
                contagens.joinToString("\n") { (problema, votos) ->
                    "$problema: $votos "
                }
            }
        }
    }
}

package com.example.pesquisaeleitoral

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt

class DadosActivity : AppCompatActivity() {

    private val problemas = listOf(
        "Saúde", "Segurança", "Educação", "Turismo",
        "Agricultura", "Pecuária", "Extrativismo"
    )

    // Paleta fixa para os candidatos — cores bem distintas
    private val coresCandidatos = intArrayOf(
        "#1E88E5".toColorInt(), // azul
        "#E53935".toColorInt(), // vermelho
        "#43A047".toColorInt(), // verde
        "#FB8C00".toColorInt(), // laranja
        "#8E24AA".toColorInt(), // roxo
        "#00ACC1".toColorInt(), // ciano
        "#FDD835".toColorInt(), // amarelo
        "#6D4C41".toColorInt(), //marrom
        "#3949AB".toColorInt(), //indigo
        "#D81B60".toColorInt(), // rosa
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

        val totalEntrevistados = findViewById<TextView>(R.id.totalEntrevistados)
        val dadosPesquisa = findViewById<TextView>(R.id.dadosPesquisa)
        val dadosVotoAberto = findViewById<TextView>(R.id.dadosVotoAberto)
        val dadosProblemas = findViewById<TextView>(R.id.dadosProblemas)
        val graficoVotos = findViewById<PieChart>(R.id.graficoVotos)

        lifecycleScope.launch {
            val dao = AppDatabase
                .getInstance(this@DadosActivity)
                .entrevistadoDao()

            val total = dao.contarTotal()
            totalEntrevistados.text = total.toString()
            val votosEstimulado = dao.contarVotosPorCandidato()
            configurarGrafico(graficoVotos, votosEstimulado)
            dadosPesquisa.text = if (votosEstimulado.isEmpty()) {
                "Nenhum voto registrado"
            } else {
                votosEstimulado.joinToString("\n") { item ->
                    val pct = if (total > 0) item.votos * 100.0 / total else 0.0
                    "${item.candidato}: ${item.votos} (%.1f%%)".format(pct)
                }
            }
            val votosAberto = dao.contarVotosAbertoPorCandidato()
            dadosVotoAberto.text = if (votosAberto.isEmpty()) {
                "Nenhum voto registrado"
            } else {
                votosAberto.joinToString("\n") { item ->
                    val pct = if (total > 0) item.votos * 100.0 / total else 0.0
                    "${item.candidato}: ${item.votos} (%.1f%%)".format(pct)
                }
            }

            val contagens = mutableListOf<Pair<String, Int>>()
            for (p in problemas) {
                val qtd = dao.contarPorProblema(p)
                if (qtd > 0) contagens.add(p to qtd)
            }
            contagens.sortByDescending { it.second }

            dadosProblemas.text = if (contagens.isEmpty()) {
                "Nenhum problema registrado"
            } else {
                contagens.joinToString("\n") { (problema, votos) ->
                    "$problema: $votos"
                }
            }
        }
    }
    private fun configurarGrafico(
        grafico: PieChart,
        votos: List<com.example.pesquisaeleitoral.data.VotoCount>
    ) {
        if (votos.isEmpty()) {
            grafico.clear()
            grafico.invalidate()
            return
        }

        // Uma entrada por candidato
        val entradas = votos.map { item ->
            PieEntry(item.votos.toFloat(), item.candidato)
        }

        val dataSet = PieDataSet(entradas, "Votos").apply {
            // Cor distinta para cada fatia (cicla a paleta se houver mais candidatos que cores)
            colors = votos.mapIndexed { index, _ ->
                coresCandidatos[index % coresCandidatos.size]
            }
            sliceSpace = 3f
            setDrawValues(true)
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter(grafico)
        }

        val pieData = PieData(dataSet).apply {
            setValueTextSize(12f)
        }

        grafico.apply {
            data = pieData
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 45f
            transparentCircleRadius = 50f
            setUsePercentValues(true)
            centerText = "Votos"
            setCenterTextSize(16f)
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            legend.isEnabled = true
            legend.textSize = 12f
            legend.isWordWrapEnabled = true
            setDrawEntryLabels(true)
            invalidate()
        }
    }
}
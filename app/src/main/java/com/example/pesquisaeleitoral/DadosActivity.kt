package com.example.pesquisaeleitoral

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import com.example.pesquisaeleitoral.data.VotoCount
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch

class DadosActivity : AppCompatActivity() {

    private val problemas = listOf(
        "Saúde", "Segurança", "Educação", "Turismo",
        "Agricultura", "Pecuária", "Extrativismo"
    )

    // Paleta fixa — cores bem distintas (usada em candidatos e problemas)
    private val paleta = intArrayOf(
        "#1E88E5".toColorInt(), // azul
        "#E53935".toColorInt(), // vermelho
        "#43A047".toColorInt(), // verde
        "#FB8C00".toColorInt(), // laranja
        "#8E24AA".toColorInt(), // roxo
        "#00ACC1".toColorInt(), // ciano
        "#FDD835".toColorInt(), // amarelo
        "#6D4C41".toColorInt(), // marrom
        "#3949AB".toColorInt(), // indigo
        "#D81B60".toColorInt(), // rosa
    )

    private fun corDe(index: Int) = paleta[index % paleta.size]

    private fun Int.dp() = (this * resources.displayMetrics.density).toInt()

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
        val containerCandidatos = findViewById<LinearLayout>(R.id.containerCandidatos)
        val containerVotoAberto = findViewById<LinearLayout>(R.id.containerVotoAberto)
        val containerProblemas = findViewById<LinearLayout>(R.id.containerProblemas)
        val graficoVotos = findViewById<PieChart>(R.id.graficoVotos)
        val graficoProblemas = findViewById<HorizontalBarChart>(R.id.graficoProblemas)

        lifecycleScope.launch {
            val dao = AppDatabase
                .getInstance(this@DadosActivity)
                .entrevistadoDao()

            val total = dao.contarTotal()
            totalEntrevistados.text = total.toString()

            // Votos estimulados (gráfico + lista com cores)
            val votosEstimulado = dao.contarVotosPorCandidato()
            configurarGrafico(graficoVotos, votosEstimulado)
            containerCandidatos.removeAllViews()
            if (votosEstimulado.isEmpty()) {
                adicionarLinha(containerCandidatos, "Nenhum voto registrado", "", null)
            } else {
                votosEstimulado.forEachIndexed { index, item ->
                    val pct = if (total > 0) item.votos * 100.0 / total else 0.0
                    adicionarLinha(
                        containerCandidatos,
                        item.candidato,
                        "${item.votos} (%.1f%%)".format(pct),
                        corDe(index)
                    )
                }
            }

            // Votos em aberto (sem cor)
            val votosAberto = dao.contarVotosAbertoPorCandidato()
            containerVotoAberto.removeAllViews()
            if (votosAberto.isEmpty()) {
                adicionarLinha(containerVotoAberto, "Nenhum voto registrado", "", null)
            } else {
                votosAberto.forEach { item ->
                    val pct = if (total > 0) item.votos * 100.0 / total else 0.0
                    adicionarLinha(
                        containerVotoAberto,
                        item.candidato,
                        "${item.votos} (%.1f%%)".format(pct),
                        null
                    )
                }
            }

            // Problemas (gráfico + lista com cores)
            val contagens = mutableListOf<Pair<String, Int>>()
            for (p in problemas) {
                val qtd = dao.contarPorProblema(p)
                if (qtd > 0) contagens.add(p to qtd)
            }
            contagens.sortByDescending { it.second }
            configurarGraficoProblemas(graficoProblemas, contagens)

            containerProblemas.removeAllViews()
            if (contagens.isEmpty()) {
                adicionarLinha(containerProblemas, "Nenhum problema registrado", "", null)
            } else {
                contagens.forEachIndexed { index, (problema, qtd) ->
                    adicionarLinha(containerProblemas, problema, qtd.toString(), corDe(index))
                }
            }
        }
    }

    // Linha: [bolinha colorida] nome ............ valor (negrito)
    private fun adicionarLinha(container: LinearLayout, nome: String, valor: String, cor: Int?) {
        val linha = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 6.dp(), 0, 6.dp())
        }

        if (cor != null) {
            val bolinha = View(this).apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(cor)
                }
                layoutParams = LinearLayout.LayoutParams(14.dp(), 14.dp()).apply {
                    marginEnd = 12.dp()
                }
            }
            linha.addView(bolinha)
        }

        val txtNome = TextView(this).apply {
            text = nome
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            )
        }

        val txtValor = TextView(this).apply {
            text = valor
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
        }

        linha.addView(txtNome)
        linha.addView(txtValor)
        container.addView(linha)
    }

    private fun configurarGrafico(grafico: PieChart, votos: List<VotoCount>) {
        if (votos.isEmpty()) {
            grafico.clear()
            grafico.invalidate()
            return
        }

        val entradas = votos.map { item ->
            PieEntry(item.votos.toFloat(), item.candidato)
        }

        val dataSet = PieDataSet(entradas, "Votos").apply {
            colors = votos.mapIndexed { index, _ -> corDe(index) }
            sliceSpace = 3f
            setDrawValues(true)
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter(grafico)
        }

        grafico.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 45f
            transparentCircleRadius = 50f
            setUsePercentValues(true)
            centerText = "Votos"
            setCenterTextSize(16f)
            setDrawEntryLabels(false)
            legend.isEnabled = false // a legenda é a lista colorida abaixo do gráfico
            invalidate()
        }
    }

    private fun configurarGraficoProblemas(
        grafico: HorizontalBarChart,
        contagens: List<Pair<String, Int>>
    ) {
        if (contagens.isEmpty()) {
            grafico.clear()
            grafico.invalidate()
            return
        }

        // No gráfico horizontal o índice 0 fica embaixo; inverte para o mais citado ficar no topo
        val ordenado = contagens.reversed()
        val cores = contagens.indices.map { corDe(it) }.reversed()

        val entradas = ordenado.mapIndexed { i, (_, qtd) ->
            BarEntry(i.toFloat(), qtd.toFloat())
        }

        val dataSet = BarDataSet(entradas, "Problemas").apply {
            colors = cores
            valueTextSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float) = value.toInt().toString()
            }
        }

        val maximo = contagens.maxOf { it.second }

        grafico.apply {
            data = BarData(dataSet).apply { barWidth = 0.6f }
            description.isEnabled = false
            legend.isEnabled = false // nomes ficam na lista colorida abaixo
            setScaleEnabled(false)
            setDrawGridBackground(false)
            xAxis.isEnabled = false // sem rótulos laterais, evita corte
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = maximo + 0.6f // folga para o número no fim da barra
                isEnabled = false
            }
            axisRight.isEnabled = false
            invalidate()
        }
    }
}
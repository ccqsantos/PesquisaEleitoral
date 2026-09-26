// app/src/main/java/com/example/pesquisaeleitoral/EleitoresActivity.kt
package com.example.pesquisaeleitoral

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EleitoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eleitores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lista = findViewById<ListView>(R.id.listaEleitores)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

        lifecycleScope.launch {
            val entrevistados = AppDatabase.getInstance(this@EleitoresActivity).entrevistadoDao().listarTodos()
            val linhas = entrevistados.map {
                "Nome: ${it.nome ?: "Não informado"}\n" +
                        "Celular: ${it.telefone ?: "-"}\n" +
                        "Gênero: ${it.genero ?: "-"}\n" +
                        "Data: ${sdf.format(Date(it.dataHora))}\n" +
                        "Localização: ${it.latitude ?: "-"}, ${it.longitude ?: "-"}"
            }
            lista.adapter = ArrayAdapter(this@EleitoresActivity, android.R.layout.simple_list_item_1, linhas)
        }
    }
}
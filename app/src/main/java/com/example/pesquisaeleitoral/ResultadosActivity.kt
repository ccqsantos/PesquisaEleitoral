// app/src/main/java/com/example/pesquisaeleitoral/ResultadosActivity.kt
package com.example.pesquisaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import kotlinx.coroutines.launch

class ResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btEleitores).setOnClickListener {
            startActivity(Intent(this, EleitoresActivity::class.java))
        }

        findViewById<Button>(R.id.btDados).setOnClickListener {
            startActivity(Intent(this, DadosActivity::class.java))
        }

        findViewById<Button>(R.id.btLimpar).setOnClickListener {
            lifecycleScope.launch {
                AppDatabase.getInstance(this@ResultadosActivity).entrevistadoDao().limparTudo()
                Toast.makeText(this@ResultadosActivity, "Dados apagados", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btLimpar).setOnClickListener {
            lifecycleScope.launch {
                AppDatabase.getInstance(this@ResultadosActivity).entrevistadoDao().limparTudo()
            }
        }
    }
}
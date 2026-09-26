// app/src/main/java/com/example/pesquisaeleitoral/DadosdoUserActivity.kt
package com.example.pesquisaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pesquisaeleitoral.data.AppDatabase
import com.example.pesquisaeleitoral.data.Entrevistado
import kotlinx.coroutines.launch

class UserData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val votoAberto = intent.getStringExtra("voto_aberto") ?: ""
        val votoEstimulado = intent.getStringExtra("voto_estimulado") ?: ""
        val problemas = intent.getStringArrayListExtra("problemas") ?: arrayListOf()

        findViewById<Button>(R.id.btSim).setOnClickListener {
            val i = Intent(this, CadastroEntrevistado::class.java)
            i.putExtra("voto_aberto", votoAberto)
            i.putExtra("voto_estimulado", votoEstimulado)
            i.putStringArrayListExtra("problemas", problemas)
            startActivity(i)
            finish()
        }

        findViewById<Button>(R.id.btNao).setOnClickListener {
            val entrevistado = Entrevistado(
                votoAberto = votoAberto,
                votoEstimulado = votoEstimulado,
                problemas = problemas.joinToString(","),
                nome = null,
                telefone = null,
                genero = null,
                latitude = null,
                longitude = null
            )
            lifecycleScope.launch {
                AppDatabase.getInstance(this@UserData).entrevistadoDao().inserir(entrevistado)
                startActivity(Intent(this@UserData, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
        }
    }
}
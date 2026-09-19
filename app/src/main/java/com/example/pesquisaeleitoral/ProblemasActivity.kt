package com.example.pesquisaeleitoral

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProblemasActivity : AppCompatActivity() {
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

        val spinnerProblema = findViewById<Spinner>(R.id.spinnerProblema);
        val opcoes = arrayOf("Saúde", "Segurança", "Educação", "Turismo", "Agricultura", "Pecuária", "Extrativismo");
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProblema.adapter = adapter

        findViewById<Button>(R.id.btEnviar3).setOnClickListener {
            val intent = Intent(this, DadosdoUserActivity::class.java)
            intent.putExtra("voto_aberto", getIntent().getStringExtra("voto_aberto"))
            intent.putExtra("voto_estimulado", getIntent().getStringExtra("voto_estimulado"))
            intent.putStringArrayListExtra(
                "problemas",
                arrayListOf(spinnerProblema.selectedItem.toString())
            )
            startActivity(intent)
        }
    }
}
package com.example.pesquisaeleitoral

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.pesquisaeleitoral.data.AppDatabase
import com.example.pesquisaeleitoral.data.Entrevistado
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter

class CadastroEntrevistado : AppCompatActivity() {

    private var latitude: Double? = null
    private var longitude: Double? = null

    private val pedirPermissao = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedida ->
        if (concedida) obterLocalizacao()
    }

    private fun verificarLocalizacao() {
        val ok = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (ok) obterLocalizacao() else pedirPermissao.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun obterLocalizacao() {
        LocationServices.getFusedLocationProviderClient(this)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    latitude = loc.latitude
                    longitude = loc.longitude
                }
            }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_entrevistado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        verificarLocalizacao()

        val spinnerGenero = findViewById<Spinner>(R.id.spinnerGenero)
        val opcoesGenero = arrayOf("Feminino", "Masculino", "Outro", "Prefiro não informar")
        spinnerGenero.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesGenero)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val inNome = findViewById<EditText>(R.id.inNome)
        val inTelefone = findViewById<EditText>(R.id.inTelefone)

        val votoAberto = intent.getStringExtra("voto_aberto") ?: ""
        val votoEstimulado = intent.getStringExtra("voto_estimulado") ?: ""
        val problemas = intent.getStringArrayListExtra("problemas") ?: arrayListOf()

        findViewById<Button>(R.id.btConfirmar).setOnClickListener {
            salvarEFinalizar(votoAberto, votoEstimulado, problemas, inNome.text.toString(), inTelefone.text.toString(), spinnerGenero.selectedItem.toString())
        }
    }

    private fun salvarEFinalizar(
        votoAberto: String, votoEstimulado: String, problemas: List<String>,
        nome: String, telefone: String, genero: String
    ) {
        val entrevistado = Entrevistado(
            votoAberto = votoAberto,
            votoEstimulado = votoEstimulado,
            problemas = problemas.joinToString(","),
            nome = nome.ifBlank { null },
            telefone = telefone.ifBlank { null },
            genero = genero,
            latitude = latitude,
            longitude = longitude
        )
        lifecycleScope.launch {
            AppDatabase.getInstance(this@CadastroEntrevistado).entrevistadoDao().inserir(entrevistado)
            startActivity(Intent(this@CadastroEntrevistado, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }
}
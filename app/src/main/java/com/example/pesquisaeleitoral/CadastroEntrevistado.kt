package com.example.pesquisaeleitoral

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.renderscript.RenderScript
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.jar.Manifest

class CadastroEntrevistado : AppCompatActivity() {

    private var latitude: Double? = null
    private var longitude: Double? = null

    private val pedirPermissao = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedida ->
        if (concedida) obterLocalizacao()
        else Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
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
            .getCurrentLocation(RenderScript.Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    latitude = loc.latitude
                    longitude = loc.longitude
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_entrevistado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
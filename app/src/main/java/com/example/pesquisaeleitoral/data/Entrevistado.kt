package com.example.pesquisaeleitoral.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrevistados")
data class Entrevistado(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val votoAberto: String,
    val votoEstimulado: String,
    val problemas: String,       // problemas separados por vírgula
    val nome: String?,           // null se o entrevistado não quis informar
    val telefone: String?,
    val genero: String?,
    val latitude: Double?,
    val longitude: Double?,
    val dataHora: Long = System.currentTimeMillis()
)
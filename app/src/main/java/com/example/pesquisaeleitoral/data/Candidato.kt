package com.example.pesquisaeleitoral.data

data class Candidato(
    val nome: String,
    val partido: String,
    val fotoResId: Int? = null // null = usa placeholder
)
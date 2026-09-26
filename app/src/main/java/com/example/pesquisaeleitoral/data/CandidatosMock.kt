// app/src/main/java/com/example/pesquisaeleitoral/data/CandidatosMock.kt
package com.example.pesquisaeleitoral.data

object CandidatosMock {
    fun listar(): List<Candidato> = listOf(
        Candidato("Julio Campos", "Partido A"),
        Candidato("João Campos", "Partido B"),
        Candidato("Fábio Pecorito", "Partido C"),
        Candidato("Guilherme Lollos", "Partido D"),
        Candidato("José Saramago", "Partido E"),
        Candidato("Júlia Pacata", "Partido F"),
        Candidato("Mário Andrade", "Partido G"),
        Candidato("João Pedro da Penha", "Partido H"),
        Candidato("Beatriz Sousa", "Partido I"),
        Candidato("Maciel Marcio Tulio", "Partido J"),
        Candidato("Jorge José", "Partido K"),
        Candidato("Branco", ""),
        Candidato("Nulo", ""),
        Candidato("Não Sei", "")
    )
}
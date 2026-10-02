// app/src/main/java/com/example/pesquisaeleitoral/data/CandidatosMock.kt
package com.example.pesquisaeleitoral.data
import com.example.pesquisaeleitoral.R
object CandidatosMock {
    fun listar(): List<Candidato> = listOf(
        Candidato("Julio Campos", "Partido A", R.drawable.candidato1),
        Candidato("João Campos", "Partido B",R.drawable.candidato2),
        Candidato("Fábio Pecorito", "Partido C",R.drawable.candidato3),
        Candidato("Guilherme Lollos", "Partido D", R.drawable.candidato5),
        Candidato("José Saramago", "Partido E", R.drawable.candidato6),
        Candidato("Júlia Pacata", "Partido F", R.drawable.candidato4),
        Candidato("Mário Andrade", "Partido G", R.drawable.candidato9),
        Candidato("João Pedro da Penha", "Partido H", R.drawable.candidato8),
        Candidato("Beatriz Sousa", "Partido I", R.drawable.candidato7),
        Candidato("Maciel Marcio Tulio", "Partido J", R.drawable.candidato10),
        Candidato("Jorge José", "Partido K", R.drawable.candidato11),
        Candidato("Branco", "", R.drawable.blank),
        Candidato("Nulo", "", R.drawable.blank),
        Candidato("Não Sei", "", R.drawable.blank)
    )
}
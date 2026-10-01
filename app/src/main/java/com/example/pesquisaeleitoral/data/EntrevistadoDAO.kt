package com.example.pesquisaeleitoral.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class VotoCount(val candidato: String, val votos: Int)

@Dao
interface EntrevistadoDAO {
    @Insert
    suspend fun inserir(e: Entrevistado)

    @Query("SELECT * FROM entrevistados ORDER BY dataHora DESC")
    suspend fun listarTodos(): List<Entrevistado>

    @Query("SELECT COUNT(*) FROM entrevistados")
    suspend fun contarTotal(): Int

    @Query("SELECT votoEstimulado as candidato, COUNT(*) as votos FROM entrevistados GROUP BY votoEstimulado ORDER BY votos DESC")
    suspend fun contarVotosPorCandidato(): List<VotoCount>

    @Query("""
        SELECT votoAberto AS candidato, COUNT(*) AS votos 
        FROM entrevistados 
        GROUP BY votoAberto 
        ORDER BY votos DESC""")
    suspend fun contarVotosAbertoPorCandidato(): List<VotoCount>

    @Query("""
    SELECT COUNT(*)
    FROM entrevistados
    WHERE problemas LIKE '%' || :problema || '%'
""")
    suspend fun contarPorProblema(problema: String): Int

    @Query("DELETE FROM entrevistados")
    suspend fun limparTudo()
}
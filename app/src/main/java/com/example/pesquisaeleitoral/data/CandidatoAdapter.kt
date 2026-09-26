// app/src/main/java/com/example/pesquisaeleitoral/CandidatoAdapter.kt
package com.example.pesquisaeleitoral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesquisaeleitoral.data.Candidato

class CandidatoAdapter(
    private val candidatos: List<Candidato>,
    private val onSelecionar: (Candidato) -> Unit
) : RecyclerView.Adapter<CandidatoAdapter.CandidatoViewHolder>() {

    private var posicaoSelecionada = -1

    inner class CandidatoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFoto: ImageView = view.findViewById(R.id.imgFoto)
        val txtNome: TextView = view.findViewById(R.id.txtNome)
        val txtPartido: TextView = view.findViewById(R.id.txtPartido)
        val checkSelecionado: CheckBox = view.findViewById(R.id.checkSelecionado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_candidato, parent, false)
        return CandidatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidatoViewHolder, position: Int) {
        val candidato = candidatos[position]
        holder.txtNome.text = candidato.nome
        holder.txtPartido.text = candidato.partido
        holder.checkSelecionado.isChecked = position == posicaoSelecionada
        candidato.fotoResId?.let { holder.imgFoto.setImageResource(it) }

        holder.itemView.setOnClickListener {
            val posicaoAnterior = posicaoSelecionada
            posicaoSelecionada = holder.bindingAdapterPosition
            notifyItemChanged(posicaoAnterior)
            notifyItemChanged(posicaoSelecionada)
            onSelecionar(candidato)
        }
    }

    override fun getItemCount() = candidatos.size

    fun getSelecionado(): Candidato? =
        if (posicaoSelecionada in candidatos.indices) candidatos[posicaoSelecionada] else null
}
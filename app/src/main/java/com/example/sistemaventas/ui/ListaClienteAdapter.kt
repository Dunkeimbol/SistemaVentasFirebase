package com.example.sistemaventas.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.R
import com.example.sistemaventas.data.Cliente

class ListaClienteAdapter(private val data: List<Cliente>) :
    RecyclerView.Adapter<ListaClienteAdapter.ClienteVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteVH(view)
    }

    override fun onBindViewHolder(holder: ClienteVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ClienteVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgmigenero: ImageView = itemView.findViewById(R.id.imgMiGenero)
        private val lblminombre: TextView = itemView.findViewById(R.id.lblMiNombre)
        private val lblmiedad: TextView = itemView.findViewById(R.id.lblMiEdad)

        fun bind(item: Cliente) {
            lblminombre.text = item.nombre
            lblmiedad.text = (item.edad ?: 0).toString()
            val resId = when (item.genero?.uppercase()) {
                "M" -> R.drawable.hombre
                else -> R.drawable.mujer
            }
            imgmigenero.setImageResource(resId)
        }
    }
}



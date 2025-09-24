package com.example.sistemaventas.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.R
import com.example.sistemaventas.data.VentaItem

class ListaVentaAdapter(private val data: List<VentaItem>) :
    RecyclerView.Adapter<ListaVentaAdapter.VentaVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaVH(view)
    }

    override fun onBindViewHolder(holder: VentaVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class VentaVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        private val lblNombre: TextView = itemView.findViewById(R.id.lblMinombre)
        private val lblTalla: TextView = itemView.findViewById(R.id.lblMitalla)
        private val lblNumPares: TextView = itemView.findViewById(R.id.lblMinumpares)
        private val lblTotal: TextView = itemView.findViewById(R.id.lblMiventatotal)

        fun bind(item: VentaItem) {
            lblNombre.text = item.nombreCliente
            lblTalla.text = "Talla ${item.talla ?: 0}"
            lblNumPares.text = "${item.numpares} pares"
            lblTotal.text = String.format("%.2f soles", item.total)
            imgLogo.setImageResource(
                when (item.logoMarca.lowercase()) {
                    "nike" -> R.drawable.nike_logo
                    "adidas" -> R.drawable.adidas
                    else -> R.drawable.fila
                }
            )
        }
    }
}




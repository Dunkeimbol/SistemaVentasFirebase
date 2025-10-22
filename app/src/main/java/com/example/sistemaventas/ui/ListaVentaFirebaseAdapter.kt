package com.example.sistemaventas.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.R
import com.example.sistemaventas.data.VentaItemFirebase
import android.os.Handler
import android.os.Looper

class ListaVentaFirebaseAdapter(
    private val data: List<VentaItemFirebase>,
    private val onClick: (VentaItemFirebase) -> Unit,
    private val onLongClick: (VentaItemFirebase) -> Unit
) : RecyclerView.Adapter<ListaVentaFirebaseAdapter.VentaVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaVH(view, onClick, onLongClick)
    }
    override fun onBindViewHolder(holder: VentaVH, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size
    class VentaVH(
        itemView: View,
        private val onClick: (VentaItemFirebase) -> Unit,
        private val onLongClick: (VentaItemFirebase) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        private val lblNombre: TextView = itemView.findViewById(R.id.lblMinombre)
        private val lblTalla: TextView = itemView.findViewById(R.id.lblMitalla)
        private val lblNumPares: TextView = itemView.findViewById(R.id.lblMinumpares)
        private val lblTotal: TextView = itemView.findViewById(R.id.lblMiventatotal)
        private var bound: VentaItemFirebase? = null
        private val handler = Handler(Looper.getMainLooper())
        private var longTriggered = false
        fun bind(item: VentaItemFirebase) {
            bound = item
            lblNombre.text = item.nombreCliente
            lblTalla.text = "Talla ${item.talla ?: 0}"
            lblNumPares.text = "${item.numpares} pares"
            lblTotal.text = String.format("%.2f soles", item.total)
            imgLogo.setImageResource(
                when (item.marca.lowercase()) {
                    "nike" -> R.drawable.nike_logo
                    "adidas" -> R.drawable.adidas
                    else -> R.drawable.fila
                }
            )
            itemView.setOnTouchListener { _, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        longTriggered = false
                        handler.postDelayed({
                            longTriggered = true
                            bound?.let(onLongClick)
                        }, 3000)
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        handler.removeCallbacksAndMessages(null)
                        if (!longTriggered) {
                            bound?.let(onClick)
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }
}


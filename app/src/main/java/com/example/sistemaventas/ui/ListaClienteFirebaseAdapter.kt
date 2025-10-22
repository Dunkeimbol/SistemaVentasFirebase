package com.example.sistemaventas.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.R
import com.example.sistemaventas.data.ClienteFirebase

class ListaClienteFirebaseAdapter(
    private val data: List<ClienteFirebase>,
    private val onClick: (ClienteFirebase) -> Unit,
    private val onLongPress: (ClienteFirebase) -> Unit
) : RecyclerView.Adapter<ListaClienteFirebaseAdapter.ClienteVH>() {

    init {
        println("=== ADAPTADOR INICIALIZADO ===")
        println("ListaClienteFirebaseAdapter: Datos recibidos: ${data.size} clientes")
        data.forEachIndexed { index, cliente ->
            println("Cliente $index: ID=${cliente.idcliente}, Nombre=${cliente.nombre}, Género=${cliente.genero}, Edad=${cliente.edad}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteVH {
        println("ListaClienteFirebaseAdapter: Creando ViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteVH(view, onClick, onLongPress)
    }

    override fun onBindViewHolder(holder: ClienteVH, position: Int) {
        println("ListaClienteFirebaseAdapter: BindViewHolder posición $position con cliente: ${data[position].nombre}")
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        println("ListaClienteFirebaseAdapter: getItemCount() = ${data.size}")
        return data.size
    }

    class ClienteVH(
        itemView: View,
        private val onClick: (ClienteFirebase) -> Unit,
        private val onLongPress: (ClienteFirebase) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imgmigenero: ImageView = itemView.findViewById(R.id.imgMiGenero)
        private val lblminombre: TextView = itemView.findViewById(R.id.lblMiNombre)
        private val lblmiedad: TextView = itemView.findViewById(R.id.lblMiEdad)
        private var bound: ClienteFirebase? = null
        private val handler = Handler(Looper.getMainLooper())
        private var longTriggered = false

        fun bind(item: ClienteFirebase) {
            println("ListaClienteFirebaseAdapter: Bind cliente - Nombre: ${item.nombre}, Edad: ${item.edad}, Género: ${item.genero}")
            bound = item
            lblminombre.text = item.nombre
            lblmiedad.text = (item.edad ?: 0).toString()
            val resId = when (item.genero?.uppercase()) {
                "M" -> R.drawable.hombre
                else -> R.drawable.mujer
            }
            imgmigenero.setImageResource(resId)
            println("ListaClienteFirebaseAdapter: Datos configurados en la vista")

            itemView.setOnTouchListener { _, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        longTriggered = false
                        handler.postDelayed({
                            longTriggered = true
                            bound?.let(onLongPress)
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


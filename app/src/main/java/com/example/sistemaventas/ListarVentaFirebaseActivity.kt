package com.example.sistemaventas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.data.ProductoFirebaseRepository
import com.example.sistemaventas.ui.ListaVentaFirebaseAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListarVentaFirebaseActivity : AppCompatActivity() {
    private lateinit var productoRepository: ProductoFirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_venta)

        productoRepository = ProductoFirebaseRepository()

        val recycler = findViewById<RecyclerView>(R.id.recyclerVentas)
        recycler.layoutManager = LinearLayoutManager(this)

        cargarVentas(recycler)
    }

    private fun cargarVentas(recycler: RecyclerView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data = productoRepository.listarVentas()
                recycler.adapter = ListaVentaFirebaseAdapter(
                    data,
                    onClick = { venta ->
                        val intent = android.content.Intent(this@ListarVentaFirebaseActivity, AgregarVentaFirebaseActivity::class.java)
                        intent.putExtra("edit_idproducto", venta.idproducto)
                        intent.putExtra("edit_idcliente", venta.idcliente)
                        intent.putExtra("edit_marca", venta.marca)
                        intent.putExtra("edit_talla", venta.talla ?: 0)
                        intent.putExtra("edit_precio", venta.precio)
                        intent.putExtra("edit_numpares", venta.numpares)
                        startActivity(intent)
                    },
                    onLongClick = { venta ->
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val success = productoRepository.deleteById(venta.idproducto)
                                if (success) {
                                    Toast.makeText(this@ListarVentaFirebaseActivity, "Venta eliminada", Toast.LENGTH_SHORT).show()
                                    cargarVentas(recycler) // Recargar la lista
                                } else {
                                    Toast.makeText(this@ListarVentaFirebaseActivity, "Error al eliminar venta", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(this@ListarVentaFirebaseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@ListarVentaFirebaseActivity, "Error al cargar ventas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

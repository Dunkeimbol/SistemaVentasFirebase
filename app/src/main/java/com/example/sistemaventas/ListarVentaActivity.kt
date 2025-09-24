package com.example.sistemaventas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.data.ProductoRepository
import com.example.sistemaventas.ui.ListaVentaAdapter

class ListarVentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_venta)

        val recycler = findViewById<RecyclerView>(R.id.recyclerVentas)
        recycler.layoutManager = LinearLayoutManager(this)

        val repo = ProductoRepository(this)
        val data = repo.listarVentas()
        recycler.adapter = ListaVentaAdapter(
            data,
            onClick = { venta ->
                val intent = android.content.Intent(this, AgregarVentaActivity::class.java)
                intent.putExtra("edit_idproducto", venta.idproducto)
                intent.putExtra("edit_idcliente", venta.idcliente)
                intent.putExtra("edit_marca", venta.marca)
                intent.putExtra("edit_talla", venta.talla ?: 0)
                intent.putExtra("edit_precio", venta.precio)
                intent.putExtra("edit_numpares", venta.numpares)
                startActivity(intent)
            },
            onLongClick = { venta ->
                repo.deleteById(venta.idproducto)
                recreate()
            }
        )
    }
}





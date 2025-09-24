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
        recycler.adapter = ListaVentaAdapter(data)
    }
}




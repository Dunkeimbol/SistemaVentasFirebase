package com.example.sistemaventas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.data.ClienteRepository
import com.example.sistemaventas.ui.ListaClienteAdapter

class ListarClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cliente)

        val recycler = findViewById<RecyclerView>(R.id.recyclerClientes)
        recycler.layoutManager = LinearLayoutManager(this)

        val repo = ClienteRepository(this)
        val data = repo.getAll()
        recycler.adapter = ListaClienteAdapter(data)
    }
}




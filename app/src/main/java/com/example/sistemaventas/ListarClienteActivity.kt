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
        recycler.adapter = ListaClienteAdapter(
            data,
            onClick = { cliente ->
                val i = android.content.Intent(this, AgregarClienteActivity::class.java)
                i.putExtra("edit_idcliente", cliente.idcliente ?: -1)
                i.putExtra("edit_nombre", cliente.nombre)
                i.putExtra("edit_genero", cliente.genero)
                i.putExtra("edit_edad", cliente.edad ?: -1)
                startActivity(i)
            },
            onLongPress = { cliente ->
                cliente.idcliente?.let { repo.deleteById(it) }
                recreate()
            }
        )
    }
}




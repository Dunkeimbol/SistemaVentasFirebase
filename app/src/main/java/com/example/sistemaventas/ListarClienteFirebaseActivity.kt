package com.example.sistemaventas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaventas.data.ClienteFirebaseRepository
import com.example.sistemaventas.ui.ListaClienteFirebaseAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListarClienteFirebaseActivity : AppCompatActivity() {
    private lateinit var clienteRepository: ClienteFirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cliente)

        clienteRepository = ClienteFirebaseRepository()

        val recycler = findViewById<RecyclerView>(R.id.recyclerClientes)
        recycler.layoutManager = LinearLayoutManager(this)

        cargarClientes(recycler)
    }

    private fun cargarClientes(recycler: RecyclerView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                println("=== INICIANDO CARGA DE CLIENTES ===")
                println("ListarClienteFirebaseActivity: Cargando clientes desde Firebase...")
                val data = clienteRepository.getAll()
                println("ListarClienteFirebaseActivity: Clientes obtenidos: ${data.size}")
                
                if (data.isEmpty()) {
                    println("ListarClienteFirebaseActivity: No hay clientes - mostrando mensaje")
                    Toast.makeText(this@ListarClienteFirebaseActivity, "No hay clientes registrados", Toast.LENGTH_SHORT).show()
                } else {
                    println("ListarClienteFirebaseActivity: Configurando adaptador con ${data.size} clientes")
                    data.forEachIndexed { index, cliente ->
                        println("Cliente $index: ID=${cliente.idcliente}, Nombre=${cliente.nombre}, Género=${cliente.genero}, Edad=${cliente.edad}")
                    }
                }
                
                // Verificar que el RecyclerView esté configurado correctamente
                println("ListarClienteFirebaseActivity: RecyclerView configurado - LayoutManager: ${recycler.layoutManager != null}")
                println("ListarClienteFirebaseActivity: RecyclerView configurado - Adapter será: ${data.size} items")
                
                recycler.adapter = ListaClienteFirebaseAdapter(
                    data,
                    onClick = { cliente ->
                        println("Cliente clickeado: ${cliente.nombre}")
                        val i = android.content.Intent(this@ListarClienteFirebaseActivity, AgregarClienteFirebaseActivity::class.java)
                        i.putExtra("edit_idcliente", cliente.idcliente ?: "")
                        i.putExtra("edit_nombre", cliente.nombre)
                        i.putExtra("edit_genero", cliente.genero)
                        i.putExtra("edit_edad", cliente.edad ?: -1L)
                        startActivity(i)
                    },
                    onLongPress = { cliente ->
                        println("Cliente presionado largo: ${cliente.nombre}")
                        cliente.idcliente?.let { id ->
                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    val success = clienteRepository.deleteById(id)
                                    if (success) {
                                        Toast.makeText(this@ListarClienteFirebaseActivity, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                                        cargarClientes(recycler) // Recargar la lista
                                    } else {
                                        Toast.makeText(this@ListarClienteFirebaseActivity, "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(this@ListarClienteFirebaseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                )
                println("=== ADAPTADOR CONFIGURADO ===")
            } catch (e: Exception) {
                Toast.makeText(this@ListarClienteFirebaseActivity, "Error al cargar clientes: ${e.message}", Toast.LENGTH_LONG).show()
                println("ListarClienteFirebaseActivity: Error al cargar clientes: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

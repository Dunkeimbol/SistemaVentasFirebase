package com.example.sistemaventas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemaventas.data.ClienteFirebaseRepository
import com.example.sistemaventas.data.ProductoFirebase
import com.example.sistemaventas.data.ProductoFirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarVentaFirebaseActivity : AppCompatActivity() {
    private lateinit var clienteRepository: ClienteFirebaseRepository
    private lateinit var productoRepository: ProductoFirebaseRepository
    private var clientes = listOf<com.example.sistemaventas.data.ClienteFirebase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_venta)

        clienteRepository = ClienteFirebaseRepository()
        productoRepository = ProductoFirebaseRepository()

        val spnCliente = findViewById<Spinner>(R.id.spnCliente)
        val spnMarca = findViewById<Spinner>(R.id.spnMarca)
        val spnTalla = findViewById<Spinner>(R.id.spnTalla)
        val txtNumPares = findViewById<EditText>(R.id.txtNumPares)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarVenta)
        val btnListar = findViewById<Button>(R.id.btnListarVenta2)
        val btnInicio = findViewById<Button>(R.id.btnInicio1)

        // Configurar spinners
        val marcas = listOf("Nike", "Adidas", "Fila")
        val tallas = listOf(38, 40, 42)
        spnMarca.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)
        spnTalla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tallas)

        // Cargar clientes y configurar modo edición después
        cargarClientesYConfigurar(spnCliente, marcas, tallas, txtNumPares, btnAgregar)

        btnAgregar.setOnClickListener {
            if (clientes.isEmpty()) {
                Toast.makeText(this, "Primero agregue clientes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idx = spnCliente.selectedItemPosition
            val clienteId = clientes[idx].idcliente ?: run {
                Toast.makeText(this, "Cliente inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val marca = spnMarca.selectedItem.toString()
            val talla = spnTalla.selectedItem.toString().toLongOrNull()
            val numPares = txtNumPares.text.toString().toLongOrNull() ?: 0L

            if (numPares <= 0) {
                Toast.makeText(this, "Ingrese nº de pares", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val precio = getPrecioPorMarcaTalla(marca, talla?.toInt() ?: 0)

            // Obtener datos de edición
            val editId = intent.getStringExtra("edit_idproducto")

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (!editId.isNullOrEmpty()) {
                        // Modo edición
                        val producto = ProductoFirebase(
                            idproducto = editId,
                            idcliente = clienteId,
                            marca = marca,
                            talla = talla,
                            precio = precio,
                            numpares = numPares
                        )
                        val success = productoRepository.update(producto)
                        if (success) {
                            Toast.makeText(this@AgregarVentaFirebaseActivity, "Venta modificada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AgregarVentaFirebaseActivity, "Error al modificar venta", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Modo inserción
                        val producto = ProductoFirebase(
                            idcliente = clienteId,
                            marca = marca,
                            talla = talla,
                            precio = precio,
                            numpares = numPares
                        )
                        val id = productoRepository.insert(producto)
                        if (id.isNotEmpty()) {
                            Toast.makeText(this@AgregarVentaFirebaseActivity, "Venta registrada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AgregarVentaFirebaseActivity, "Error al registrar venta", Toast.LENGTH_SHORT).show()
                        }
                    }

                    txtNumPares.text.clear()
                } catch (e: Exception) {
                    Toast.makeText(this@AgregarVentaFirebaseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnListar.setOnClickListener {
            startActivity(android.content.Intent(this, ListarVentaFirebaseActivity::class.java))
        }
        btnInicio.setOnClickListener {
            startActivity(android.content.Intent(this, MainActivity::class.java))
        }
    }

    private fun cargarClientesYConfigurar(spnCliente: Spinner, marcas: List<String>, tallas: List<Int>, txtNumPares: EditText, btnAgregar: Button) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                clientes = clienteRepository.getAll()
                if (clientes.isEmpty()) {
                    Toast.makeText(this@AgregarVentaFirebaseActivity, "No hay clientes. Agregue clientes primero.", Toast.LENGTH_LONG).show()
                    return@launch
                }
                val nombres = clientes.map { it.nombre }
                spnCliente.adapter = ArrayAdapter(this@AgregarVentaFirebaseActivity, android.R.layout.simple_spinner_dropdown_item, nombres)
                configurarModoEdicion(spnCliente, marcas, tallas, txtNumPares, btnAgregar)
            } catch (e: Exception) {
                Toast.makeText(this@AgregarVentaFirebaseActivity, "Error al cargar clientes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarModoEdicion(spnCliente: Spinner, marcas: List<String>, tallas: List<Int>, txtNumPares: EditText, btnAgregar: Button) {
        val editId = intent.getStringExtra("edit_idproducto")
        val editIdCliente = intent.getStringExtra("edit_idcliente")
        val editMarca = intent.getStringExtra("edit_marca")
        val editTalla = intent.getLongExtra("edit_talla", 0L)
        val editNumPares = intent.getLongExtra("edit_numpares", 0L)

        if (!editId.isNullOrEmpty()) {
            btnAgregar.text = "Modificar"
            val idxCliente = clientes.indexOfFirst { it.idcliente == editIdCliente }
            if (idxCliente >= 0) spnCliente.setSelection(idxCliente)
            val idxMarca = marcas.indexOf(editMarca)
            if (idxMarca >= 0) findViewById<Spinner>(R.id.spnMarca).setSelection(idxMarca)
            val idxTalla = tallas.indexOf(editTalla.toInt())
            if (idxTalla >= 0) findViewById<Spinner>(R.id.spnTalla).setSelection(idxTalla)
            if (editNumPares > 0) txtNumPares.setText(editNumPares.toString())
        }
    }

    private fun getPrecioPorMarcaTalla(marca: String, talla: Int): Double {
        return when (marca.lowercase()) {
            "nike" -> when (talla) {
                38 -> 150.0
                40 -> 160.0
                42 -> 160.0
                else -> 160.0
            }
            "adidas" -> when (talla) {
                38 -> 140.0
                40 -> 150.0
                42 -> 150.0
                else -> 150.0
            }
            "fila" -> when (talla) {
                38 -> 80.0
                40 -> 85.0
                42 -> 90.0
                else -> 90.0
            }
            else -> 0.0
        }
    }
}
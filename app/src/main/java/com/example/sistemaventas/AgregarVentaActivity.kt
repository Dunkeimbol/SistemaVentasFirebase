package com.example.sistemaventas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemaventas.data.ClienteRepository
import com.example.sistemaventas.data.Producto
import com.example.sistemaventas.data.ProductoRepository

class AgregarVentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_venta)
        val spnCliente = findViewById<Spinner>(R.id.spnCliente)
        val spnMarca = findViewById<Spinner>(R.id.spnMarca)
        val spnTalla = findViewById<Spinner>(R.id.spnTalla)
        val txtNumPares = findViewById<EditText>(R.id.txtNumPares)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarVenta)
        val btnListar = findViewById<Button>(R.id.btnListarVenta2)
        val btnInicio = findViewById<Button>(R.id.btnInicio1)
        val clienteRepo = ClienteRepository(this)
        val clientes = clienteRepo.getAll()
        val nombres = clientes.map { it.nombre }
        spnCliente.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombres)
        val marcas = listOf("Nike", "Adidas", "Fila")
        val tallas = listOf(38, 40, 42)
        spnMarca.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)
        spnTalla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tallas)
        val productoRepo = ProductoRepository(this)
        // Detectar modo edición
        val editId = intent.getLongExtra("edit_idproducto", -1L).takeIf { it > 0 }
        val editIdCliente = intent.getLongExtra("edit_idcliente", -1L)
        val editMarca = intent.getStringExtra("edit_marca")
        val editTalla = intent.getIntExtra("edit_talla", 0)
        val editNumPares = intent.getIntExtra("edit_numpares", 0)

        if (editId != null) {
            btnAgregar.text = "Modificar"
            val idxCliente = clientes.indexOfFirst { it.idcliente == editIdCliente }
            if (idxCliente >= 0) spnCliente.setSelection(idxCliente)
            val idxMarca = marcas.indexOf(editMarca)
            if (idxMarca >= 0) spnMarca.setSelection(idxMarca)
            val idxTalla = tallas.indexOf(editTalla)
            if (idxTalla >= 0) spnTalla.setSelection(idxTalla)
            if (editNumPares > 0) txtNumPares.setText(editNumPares.toString())
        }
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
            val talla = spnTalla.selectedItem.toString().toInt()
            val numPares = txtNumPares.text.toString().toIntOrNull() ?: 0
            if (numPares <= 0) {
                Toast.makeText(this, "Ingrese nº de pares", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val precio = getPrecioPorMarcaTalla(marca, talla)
            if (editId != null) {
                productoRepo.update(
                    Producto(
                        idproducto = editId,
                        idcliente = clienteId,
                        marca = marca,
                        talla = talla,
                        precio = precio,
                        numpares = numPares
                    )
                )
                Toast.makeText(this, "Venta modificada", Toast.LENGTH_SHORT).show()
            } else {
                productoRepo.insert(
                    Producto(
                        idcliente = clienteId,
                        marca = marca,
                        talla = talla,
                        precio = precio,
                        numpares = numPares
                    )
                )
                Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show()
            }
            txtNumPares.text.clear()
        }
        btnListar.setOnClickListener {
            startActivity(android.content.Intent(this, ListarVentaActivity::class.java))
        }
        btnInicio.setOnClickListener {
            startActivity(android.content.Intent(this, MainActivity::class.java))
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



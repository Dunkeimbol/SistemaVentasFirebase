package com.example.sistemaventas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemaventas.data.Cliente
import com.example.sistemaventas.data.ClienteRepository

class AgregarClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val spnGenero = findViewById<Spinner>(R.id.spnGenero)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarCliente)
        val btnListar = findViewById<Button>(R.id.btnListarCliente)
        val btnInicio = findViewById<Button>(R.id.btnInicio2)

        spnGenero.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("M", "F")
        )

        val repo = ClienteRepository(this)

        btnAgregar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val edadText = txtEdad.text.toString().trim()
            val genero = spnGenero.selectedItem?.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val edad = edadText.toIntOrNull()

            repo.insert(Cliente(nombre = nombre, genero = genero, edad = edad))
            Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show()
            txtNombre.text.clear()
            txtEdad.text.clear()
            spnGenero.setSelection(0)
        }

        btnListar.setOnClickListener {
            startActivity(android.content.Intent(this, ListarClienteActivity::class.java))
        }

        btnInicio.setOnClickListener {
            startActivity(android.content.Intent(this, MainActivity::class.java))
        }
    }
}




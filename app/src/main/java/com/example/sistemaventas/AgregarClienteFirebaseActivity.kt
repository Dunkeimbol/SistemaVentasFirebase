package com.example.sistemaventas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemaventas.data.ClienteFirebase
import com.example.sistemaventas.data.ClienteFirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarClienteFirebaseActivity : AppCompatActivity() {
    private lateinit var clienteRepository: ClienteFirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        clienteRepository = ClienteFirebaseRepository()

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

        // CORREGIDO: Usar getLongExtra para leer la edad
        val editId = intent.getStringExtra("edit_idcliente")
        val editNombre = intent.getStringExtra("edit_nombre")
        val editGenero = intent.getStringExtra("edit_genero")
        val editEdad = intent.getLongExtra("edit_edad", -1L)

        if (!editId.isNullOrEmpty()) {
            btnAgregar.text = "Modificar"
            txtNombre.setText(editNombre ?: "")
            if (editEdad >= 0) txtEdad.setText(editEdad.toString())
            val idxGen = listOf("M", "F").indexOf(editGenero)
            if (idxGen >= 0) spnGenero.setSelection(idxGen)
        }

        btnAgregar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val edadText = txtEdad.text.toString().trim()
            val genero = spnGenero.selectedItem?.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // CORREGIDO: Convertir el texto a Long?
            val edad = edadText.toLongOrNull()

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (!editId.isNullOrEmpty()) {
                        val cliente = ClienteFirebase(
                            idcliente = editId,
                            nombre = nombre,
                            genero = genero,
                            edad = edad // Ahora la asignación es correcta (Long?)
                        )
                        val success = clienteRepository.update(cliente)
                        if (success) {
                            Toast.makeText(this@AgregarClienteFirebaseActivity, "Cliente modificado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AgregarClienteFirebaseActivity, "Error al modificar cliente", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val cliente = ClienteFirebase(
                            nombre = nombre,
                            genero = genero,
                            edad = edad // Ahora la asignación es correcta (Long?)
                        )
                        val id = clienteRepository.insert(cliente)
                        if (id.isNotEmpty()) {
                            Toast.makeText(this@AgregarClienteFirebaseActivity, "Cliente agregado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AgregarClienteFirebaseActivity, "Error al agregar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }

                    txtNombre.text.clear()
                    txtEdad.text.clear()
                    spnGenero.setSelection(0)
                } catch (e: Exception) {
                    Toast.makeText(this@AgregarClienteFirebaseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnListar.setOnClickListener {
            startActivity(android.content.Intent(this, ListarClienteFirebaseActivity::class.java))
        }

        btnInicio.setOnClickListener {
            startActivity(android.content.Intent(this, MainActivity::class.java))
        }
    }
}
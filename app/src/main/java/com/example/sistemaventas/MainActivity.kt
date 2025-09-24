package com.example.sistemaventas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<android.widget.Button>(R.id.btnAgregarCliente).setOnClickListener {
            startActivity(Intent(this, AgregarClienteActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnAgregarVenta).setOnClickListener {
            startActivity(Intent(this, AgregarVentaActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnListarCliente).setOnClickListener {
            startActivity(Intent(this, ListarClienteActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnListarVenta).setOnClickListener {
            startActivity(Intent(this, ListarVentaActivity::class.java))
        }
    }
}
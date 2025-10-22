package com.example.sistemaventas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verificar conexión a Firebase
        val db = FirebaseFirestore.getInstance()
        db.enableNetwork().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Conectado a Firebase", Toast.LENGTH_SHORT).show()
                println("Firebase conectado exitosamente")
                
                // Probar escritura en Firebase
                probarFirebase()
            } else {
                Toast.makeText(this, "Error de conexión a Firebase: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                println("Error de Firebase: ${task.exception?.message}")
            }
        }

        findViewById<android.widget.Button>(R.id.btnAgregarCliente).setOnClickListener {
            startActivity(Intent(this, AgregarClienteFirebaseActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnAgregarVenta).setOnClickListener {
            startActivity(Intent(this, AgregarVentaFirebaseActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnListarCliente).setOnClickListener {
            startActivity(Intent(this, ListarClienteFirebaseActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnListarVenta).setOnClickListener {
            startActivity(Intent(this, ListarVentaFirebaseActivity::class.java))
        }
    }
    
    private fun probarFirebase() {
        val db = FirebaseFirestore.getInstance()
        val testData = hashMapOf(
            "test" to "conexion",
            "timestamp" to System.currentTimeMillis()
        )
        
        db.collection("test").add(testData)
            .addOnSuccessListener { documentReference ->
                println("Firebase: Prueba exitosa - Documento creado con ID: ${documentReference.id}")
                Toast.makeText(this, "Prueba Firebase exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                println("Firebase: Error en prueba: ${e.message}")
                Toast.makeText(this, "Error en prueba Firebase: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
package com.example.sistemaventas.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

data class ClienteFirebase(
    var idcliente: String? = null,
    var nombre: String = "",
    var genero: String? = null,
    var edad: Long? = null
)

data class ProductoFirebase(
    var idproducto: String? = null,
    var idcliente: String = "",
    var marca: String = "",
    var talla: Long? = null,
    var precio: Double = 0.0,
    var numpares: Long = 0
)

data class VentaItemFirebase(
    var idproducto: String = "",
    var idcliente: String = "",
    var marca: String = "",
    var nombreCliente: String = "",
    var talla: Long? = null,
    var precio: Double = 0.0,
    var numpares: Long = 0,
    var total: Double = 0.0
)

class ClienteFirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = "clientes"

    suspend fun insert(cliente: ClienteFirebase): String {
        return try {
            val docRef = db.collection(collection).add(cliente).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun update(cliente: ClienteFirebase): Boolean {
        return try {
            requireNotNull(cliente.idcliente)
            db.collection(collection).document(cliente.idcliente!!).set(cliente).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteById(idcliente: String): Boolean {
        return try {
            db.collection(collection).document(idcliente).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getById(id: String): ClienteFirebase? {
        return try {
            val document = db.collection(collection).document(id).get().await()
            if (document.exists()) {
                document.toObject(ClienteFirebase::class.java)?.copy(idcliente = id)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAll(): List<ClienteFirebase> {
        return try {
            val documents = db.collection(collection)
                .orderBy("nombre", Query.Direction.ASCENDING)
                .get().await()
            documents.mapNotNull { document ->
                document.toObject(ClienteFirebase::class.java)?.copy(idcliente = document.id)
            }
        } catch (e: Exception) {
            println("Firebase: Error al obtener clientes: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}

class ProductoFirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = "productos"

    suspend fun insert(producto: ProductoFirebase): String {
        return try {
            val docRef = db.collection(collection).add(producto).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun update(producto: ProductoFirebase): Boolean {
        return try {
            requireNotNull(producto.idproducto)
            db.collection(collection).document(producto.idproducto!!).set(producto).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteById(idproducto: String): Boolean {
        return try {
            db.collection(collection).document(idproducto).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun listarVentas(): List<VentaItemFirebase> {
        return try {
            val productos = db.collection(collection)
                .orderBy("idproducto", Query.Direction.DESCENDING)
                .get().await()

            val ventas = mutableListOf<VentaItemFirebase>()
            
            for (productoDoc in productos) {
                val producto = productoDoc.toObject(ProductoFirebase::class.java)
                    .copy(idproducto = productoDoc.id)
                
                val clienteDoc = db.collection("clientes")
                    .document(producto.idcliente)
                    .get().await()
                
                if (clienteDoc.exists()) {
                    val cliente = clienteDoc.toObject(ClienteFirebase::class.java)
                    val total = producto.precio * producto.numpares

                    ventas.add(
                        VentaItemFirebase(
                            idproducto = producto.idproducto!!,
                            idcliente = producto.idcliente,
                            marca = producto.marca,
                            nombreCliente = cliente?.nombre ?: "Cliente no encontrado",
                            talla = producto.talla,
                            precio = producto.precio,
                            numpares = producto.numpares,
                            total = total
                        )
                    )
                }
            }
            
            ventas
        } catch (e: Exception) {
            emptyList()
        }
    }
}

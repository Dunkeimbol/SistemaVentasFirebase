package com.example.sistemaventas.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class Cliente(
    val idcliente: Long? = null,
    val nombre: String,
    val genero: String?,
    val edad: Int?
)

data class Producto(
    val idproducto: Long? = null,
    val idcliente: Long,
    val marca: String,
    val talla: Int?,
    val precio: Double,
    val numpares: Int
)

data class VentaItem(
    val logoMarca: String,
    val nombreCliente: String,
    val talla: Int?,
    val numpares: Int,
    val total: Double
)
class ClienteRepository(context: Context) {
    private val db = DbHelper(context).writableDatabase

    fun insert(cliente: Cliente): Long {
        val values = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("genero", cliente.genero)
            put("edad", cliente.edad)
        }
        return db.insert("Cliente", null, values)
    }
    fun getById(id: Long): Cliente? {
        val c: Cursor = db.query("Cliente", null, "idcliente=?", arrayOf(id.toString()), null, null, null)
        c.use {
            if (it.moveToFirst()) {
                return Cliente(
                    idcliente = it.getLong(it.getColumnIndexOrThrow("idcliente")),
                    nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                    genero = it.getString(it.getColumnIndexOrThrow("genero")),
                    edad = it.getInt(it.getColumnIndexOrThrow("edad"))
                )
            }
        }
        return null
    }
    fun getAll(): List<Cliente> {
        val cursor = db.query("Cliente", null, null, null, null, null, "idcliente DESC")
        val list = mutableListOf<Cliente>()
        cursor.use { c ->
            while (c.moveToNext()) {
                list.add(
                    Cliente(
                        idcliente = c.getLong(c.getColumnIndexOrThrow("idcliente")),
                        nombre = c.getString(c.getColumnIndexOrThrow("nombre")),
                        genero = c.getString(c.getColumnIndexOrThrow("genero")),
                        edad = if (c.isNull(c.getColumnIndexOrThrow("edad"))) null else c.getInt(c.getColumnIndexOrThrow("edad"))
                    )
                )
            }
        }
        return list
    }
}
class ProductoRepository(context: Context) {
    private val db = DbHelper(context).writableDatabase
    fun insert(producto: Producto): Long {
        val values = ContentValues().apply {
            put("idcliente", producto.idcliente)
            put("marca", producto.marca)
            put("talla", producto.talla)
            put("precio", producto.precio)
            put("numpares", producto.numpares)
        }
        return db.insert("Producto", null, values)
    }
    fun listarVentas(): List<VentaItem> {
        val sql = """
            SELECT p.marca, c.nombre, p.talla, p.numpares, (p.precio * p.numpares) AS total
            FROM Producto p
            INNER JOIN Cliente c ON c.idcliente = p.idcliente
            ORDER BY p.idproducto DESC
        """.trimIndent()
        val cursor = db.rawQuery(sql, null)
        val data = mutableListOf<VentaItem>()
        cursor.use { c ->
            while (c.moveToNext()) {
                data.add(
                    VentaItem(
                        logoMarca = c.getString(0),
                        nombreCliente = c.getString(1),
                        talla = if (c.isNull(2)) null else c.getInt(2),
                        numpares = c.getInt(3),
                        total = c.getDouble(4)
                    )
                )
            }
        }
        return data
    }
}



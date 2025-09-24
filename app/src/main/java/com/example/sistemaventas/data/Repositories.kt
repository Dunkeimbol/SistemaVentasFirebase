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
    val idproducto: Long,
    val idcliente: Long,
    val marca: String,
    val nombreCliente: String,
    val talla: Int?,
    val precio: Double,
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

    fun update(cliente: Cliente): Int {
        requireNotNull(cliente.idcliente)
        val values = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("genero", cliente.genero)
            put("edad", cliente.edad)
        }
        return db.update("Cliente", values, "idcliente=?", arrayOf(cliente.idcliente.toString()))
    }

    fun deleteById(idcliente: Long): Int {
        return db.delete("Cliente", "idcliente=?", arrayOf(idcliente.toString()))
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
    fun update(producto: Producto): Int {
        requireNotNull(producto.idproducto)
        val values = ContentValues().apply {
            put("idcliente", producto.idcliente)
            put("marca", producto.marca)
            put("talla", producto.talla)
            put("precio", producto.precio)
            put("numpares", producto.numpares)
        }
        return db.update("Producto", values, "idproducto=?", arrayOf(producto.idproducto.toString()))
    }
    fun deleteById(idproducto: Long): Int {
        return db.delete("Producto", "idproducto=?", arrayOf(idproducto.toString()))
    }
    fun listarVentas(): List<VentaItem> {
        val sql = """
            SELECT p.idproducto, p.idcliente, p.marca, c.nombre, p.talla, p.precio, p.numpares,
                   (p.precio * p.numpares) AS total
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
                        idproducto = c.getLong(0),
                        idcliente = c.getLong(1),
                        marca = c.getString(2),
                        nombreCliente = c.getString(3),
                        talla = if (c.isNull(4)) null else c.getInt(4),
                        precio = c.getDouble(5),
                        numpares = c.getInt(6),
                        total = c.getDouble(7)
                    )
                )
            }
        }
        return data
    }
}



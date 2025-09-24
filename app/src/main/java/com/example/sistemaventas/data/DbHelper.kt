package com.example.sistemaventas.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Cliente (
                idcliente INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                genero TEXT,
                edad INTEGER
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Producto (
                idproducto INTEGER PRIMARY KEY AUTOINCREMENT,
                idcliente INTEGER NOT NULL,
                marca TEXT NOT NULL,
                talla INTEGER,
                precio REAL NOT NULL,
                numpares INTEGER NOT NULL,
                FOREIGN KEY(idcliente) REFERENCES Cliente(idcliente)
            );
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Producto")
            db.execSQL("DROP TABLE IF EXISTS Cliente")
            onCreate(db)
        }
    }

    companion object {
        private const val DATABASE_NAME = "BDZAPATILLA.db"
        private const val DATABASE_VERSION = 2
    }
}



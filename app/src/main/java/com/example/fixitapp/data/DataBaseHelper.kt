package com.example.fixitapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fixitapp.model.Service

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "FixItApp.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de usuarios
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)"
        )

        // Tabla de servicios
        db.execSQL(
            "CREATE TABLE services (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombreCliente TEXT NOT NULL," +
                    "tipoServicio TEXT NOT NULL," +
                    "fecha TEXT NOT NULL," +
                    "descripcion TEXT NOT NULL," +
                    "estado TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS services (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombreCliente TEXT NOT NULL," +
                        "tipoServicio TEXT NOT NULL," +
                        "fecha TEXT NOT NULL," +
                        "descripcion TEXT NOT NULL," +
                        "estado TEXT NOT NULL)"
            )
        }
    }

    // ------------------- USUARIOS -------------------

    fun registerUser(nombre: String, email: String, password: String): Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE email = ?",
            arrayOf(email)
        )

        val exists = cursor.count > 0
        cursor.close()

        if (exists) return false

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("email", email)
            put("password", password)
        }

        val result = db.insert("usuarios", null, values)
        return result != -1L
    }


    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ------------------- SERVICIOS -------------------

    fun addService(service: Service): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("nombreCliente", service.nombreCliente)
            put("tipoServicio", service.tipoServicio)
            put("fecha", service.fecha)
            put("descripcion", service.descripcion)
            put("estado", service.estado)
        }

        return db.insert("services", null, values) != -1L
    }


    fun getAllServices(): List<Service> {
        val serviceList = mutableListOf<Service>()
        val db = readableDatabase

        val cursor: Cursor = db.rawQuery("SELECT * FROM services", null)

        if (cursor.moveToFirst()) {
            do {
                serviceList.add(
                    Service(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        nombreCliente = cursor.getString(cursor.getColumnIndexOrThrow("nombreCliente")),
                        tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow("tipoServicio")),
                        fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                        descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return serviceList
    }


    fun getServiceById(id: Int): Service? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM services WHERE id = ?",
            arrayOf(id.toString())
        )

        var service: Service? = null

        if (cursor.moveToFirst()) {
            service = Service(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombreCliente = cursor.getString(cursor.getColumnIndexOrThrow("nombreCliente")),
                tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow("tipoServicio")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
            )
        }

        cursor.close()
        return service
    }


    fun deleteService(id: Int): Boolean {
        val db = writableDatabase
        return db.delete("services", "id=?", arrayOf(id.toString())) > 0
    }

    // IMPORTANTE: NO cerramos la DB manualmente, Android la gestiona.
}

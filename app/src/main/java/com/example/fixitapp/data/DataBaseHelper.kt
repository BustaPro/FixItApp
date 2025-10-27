package com.example.fixitapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fixitapp.model.Service

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "FixItApp.db", null, 2) { // 👈 Versión 2 (añade tabla services)

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

    // ---------- USUARIOS ----------
    fun registerUser(nombre: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", arrayOf(email))
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()

        val values = ContentValues()
        values.put("nombre", nombre)
        values.put("email", email)
        values.put("password", password)

        val result = db.insert("usuarios", null, values)
        db.close()
        return result != -1L
    }

    fun validateUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // ---------- SERVICIOS ----------
    fun addService(service: Service): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("nombreCliente", service.nombreCliente)
        values.put("tipoServicio", service.tipoServicio)
        values.put("fecha", service.fecha)
        values.put("descripcion", service.descripcion)
        values.put("estado", service.estado)

        val result = db.insert("services", null, values)
        db.close()
        return result != -1L
    }

    fun getAllServices(): List<Service> {
        val serviceList = mutableListOf<Service>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM services", null)

        if (cursor.moveToFirst()) {
            do {
                val service = Service(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombreCliente = cursor.getString(cursor.getColumnIndexOrThrow("nombreCliente")),
                    tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow("tipoServicio")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                )
                serviceList.add(service)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return serviceList
    }

    fun getServiceById(id: Int): Service? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM services WHERE id = ?", arrayOf(id.toString()))
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
        db.close()
        return service
    }
}




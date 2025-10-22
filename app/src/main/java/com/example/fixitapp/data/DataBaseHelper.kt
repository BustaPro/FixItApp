package com.example.fixitapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "FixItApp.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun registerUser(nombre: String, email: String, password: String): Boolean {
        val db = this.writableDatabase

        // Revisar si el correo ya existe
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

}



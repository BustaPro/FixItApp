package com.example.fixitapp

import androidx.test.core.app.ApplicationProvider
import com.example.fixitapp.data.DatabaseHelper
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatabaseTest {

    @Test
    fun dbInstanciadaCorrectamente() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        assertNotNull(db)
        db.close()
    }
}

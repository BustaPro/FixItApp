package com.example.fixitapp

import com.example.fixitapp.model.Service
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {

    @Test
    fun `validar atributos del modelo Service`() {
        val service = Service(
            id = 1,
            nombreCliente = "Juan Pérez",
            tipoServicio = "Reparación",
            fecha = "01/01/2025",
            descripcion = "Televisor no enciende",
            estado = "Pendiente",
            isExternal = false
        )

        assertEquals("Juan Pérez", service.nombreCliente)
        assertEquals("Reparación", service.tipoServicio)
        assertEquals("Pendiente", service.estado)
    }
}

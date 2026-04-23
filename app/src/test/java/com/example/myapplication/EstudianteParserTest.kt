package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test

class EstudianteParserTest {

    @Test
    fun `parseResponse deberia generar lista de estudiantes correcta`() {
        // Arrange
        val jsonEx = """
            [
              {
                "id": "1",
                "nombre": "Juan",
                "apellido": "Perez",
                "email": "juan@test.com"
              },
              {
                "id": "2",
                "nombre": "Maria",
                "apellido": "Gomez",
                "email": "maria@test.com"
              }
            ]
        """.trimIndent()

        // Act
        val estudiantes = ApiService.parseResponse(jsonEx)

        // Assert
        assertEquals(2, estudiantes.size)
        
        assertEquals("1", estudiantes[0].id)
        assertEquals("Juan", estudiantes[0].nombre)
        assertEquals("Perez", estudiantes[0].apellido)
        assertEquals("juan@test.com", estudiantes[0].email)

        assertEquals("2", estudiantes[1].id)
        assertEquals("Maria", estudiantes[1].nombre)
        assertEquals("Gomez", estudiantes[1].apellido)
        assertEquals("maria@test.com", estudiantes[1].email)
    }
}

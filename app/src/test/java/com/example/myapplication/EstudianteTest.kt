package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test

class EstudianteTest {
    
    @Test
    fun verificarCreacionEstudiante_altaExitosa() {
        val alumno = Estudiante(id = "1", nombre = "Juan", apellido = "Lopez", email = "juan@mail.com")
        
        assertEquals("1", alumno.id)
        assertEquals("Juan", alumno.nombre)
        assertEquals("juan@mail.com", alumno.email)
    }
    
    @Test
    fun verificarModificarEstudiante_cambiaNombreCorrectamente() {
        val alumno = Estudiante(id = "2", nombre = "Maria", apellido = "Gomez", email = "maria@mail.com")
        
        alumno.nombre = "Marianela" 
        
        assertEquals("Marianela", alumno.nombre)
    }
}

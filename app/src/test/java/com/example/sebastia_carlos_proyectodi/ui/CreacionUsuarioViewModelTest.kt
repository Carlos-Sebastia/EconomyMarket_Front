package com.example.sebastia_carlos_proyectodi.ui

import com.example.sebastia_carlos_proyectodi.domain.repository.FakeUsuarioRepository
import com.example.sebastia_carlos_proyectodi.ui.creacion_usuario.CreacionUsuarioViewModel
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertTrue

class CreacionUsuarioViewModelTest {
    private lateinit var viewModel: CreacionUsuarioViewModel
    private lateinit var fakeRepository: FakeUsuarioRepository

    @Before
    fun setup() {
        fakeRepository = FakeUsuarioRepository()
        viewModel = CreacionUsuarioViewModel(fakeRepository)
    }

    @Test
    fun verificaCreacionUsuarioDatosCorrectos() = runTest {
        fakeRepository.creacionUsuarioDeberiaSerCorrecto = true
        val resultado = viewModel.crearUsuario(
            "12345678A",
            "Miguel",
            "García Monforte",
            "25-01-2003",
            "miguel@gmail.com",
            "Toby",
            "contraseña"
        )
        assertTrue(resultado)
    }

    @Test
    fun verificaCreacionUsuarioDatosIncorrectos() = runTest {
        fakeRepository.creacionUsuarioDeberiaSerCorrecto = false
        val resultado = viewModel.crearUsuario(
            "12345678A",
            "Miguel",
            "García Monforte",
            "25-01-2003",
            "miguel@gmail.com",
            "Toby",
            "contraseña"
        )
        assertFalse(resultado)
    }
}
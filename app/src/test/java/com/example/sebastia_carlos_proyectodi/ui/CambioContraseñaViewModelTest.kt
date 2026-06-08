package com.example.sebastia_carlos_proyectodi.ui

import com.example.sebastia_carlos_proyectodi.domain.repository.FakeUsuarioRepository
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.CambioContraseñaViewModel
import com.example.sebastia_carlos_proyectodi.ui.login.LoginViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class CambioContraseñaViewModelTest {
    private lateinit var viewModel: CambioContraseñaViewModel
    private lateinit var fakeRepository: FakeUsuarioRepository

    @Before
    fun setup() {
        fakeRepository = FakeUsuarioRepository()
        viewModel = CambioContraseñaViewModel(fakeRepository)
    }

    @Test
    fun verificaValidacionMascotaDatosCorrectos() = runTest {
        fakeRepository.mascotaDeberiaSerCorrecta = true
        val resultado = viewModel.validarMascota("12345678A", "Toby")
        assertTrue(resultado)
    }

    @Test
    fun verificaValidacionMascotaDatosIncorrectos() = runTest {
        fakeRepository.mascotaDeberiaSerCorrecta = false
        val resultado = viewModel.validarMascota("12345678A", "Toby")
        assertFalse(resultado)
    }

    @Test
    fun verificaCambioContrasenaDatosCorrectos() = runTest {
        fakeRepository.cambioContrasenaDeberiaSerCorrecto = true
        val resultado = viewModel.cambiarContrasena("nuevaContraseña")
        assertTrue(resultado)
    }

    @Test
    fun verificaCambioContrasenaDatosIncorrectos() = runTest {
        fakeRepository.cambioContrasenaDeberiaSerCorrecto = false
        val resultado = viewModel.cambiarContrasena("nuevaContraseña")
        assertFalse(resultado)
    }
}
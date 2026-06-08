package com.example.sebastia_carlos_proyectodi.ui

import com.example.sebastia_carlos_proyectodi.domain.repository.FakeUsuarioRepository
import com.example.sebastia_carlos_proyectodi.ui.login.LoginViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeRepository: FakeUsuarioRepository

    @Before
    fun setup() {
        fakeRepository = FakeUsuarioRepository()
        viewModel = LoginViewModel(fakeRepository)
    }

    @Test
    fun verificaLoginUsuarioCredencialesCorrectas() = runTest {
        fakeRepository.loginDeberiaSerCorrecto = true
        val resultado = viewModel.validarUsuario("12345678A", "contraseña")
        assertTrue(resultado)
    }

    @Test
    fun verificaLoginUsuarioCredencialesIncorrectas() = runTest {
        fakeRepository.loginDeberiaSerCorrecto = false
        val resultado = viewModel.validarUsuario("12345678A", "contraseña")
        assertFalse(resultado)
    }
}

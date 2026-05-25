package com.example.sebastia_carlos_proyectodi.ui.productos

import com.example.sebastia_carlos_proyectodi.data.FakeProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductosViewModelTest {

    private lateinit var viewModel: ProductosViewModel
    private lateinit var fakeRepository: FakeProductoRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProductoRepository()
        viewModel = ProductosViewModel(fakeRepository)
    }

    @Test
    fun productosViewModel_actualizarProductoCogido_setContieneId() = runTest {
        // Arrange
        val productoId = 1L

        // Act
        viewModel.actualizarProductoCogido(productoId)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.idsProductosListaCogidos.contains(productoId))
    }

    @Test
    fun productosViewModel_actualizarCategoria_categoriaSeleccionadaActualizada() = runTest {
        // Arrange
        val categoriaEsperada = "Carne"

        // Act
        viewModel.actualizarCategoria(categoriaEsperada)
        advanceUntilIdle()

        // Assert
        assertEquals(categoriaEsperada, viewModel.uiState.value.categoriaSeleccionada)
    }
}
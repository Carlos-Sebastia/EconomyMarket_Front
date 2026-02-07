package com.example.sebastia_carlos_proyectodi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sebastia_carlos_proyectodi.ui.productos.ProductosViewModel

@Composable
fun PantallaLista(
    navController: NavHostController,
    viewModel: ProductosViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Filtramos los productos que el usuario ha añadido a su lista
    val productosEnLista = uiState.productos.filter { it.id in uiState.idsEnLista }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productosEnLista) { producto ->
                // Comprobamos si este producto específico está en el set de "cogidos"
                val estaCogido = producto.id in uiState.idsProductosListaCogidos

                ItemLista(
                    nombre = producto.nombre,
                    estaCogido = estaCogido,
                    onCheckedChange = {
                        // Llamamos a la función del ViewModel que creamos antes
                        viewModel.actualizarProductoCogido(producto.id)
                    }
                )
                Divider()
            }
        }

        Button(
            onClick = { viewModel.vaciarLista() },
            modifier = Modifier.padding(17.dp),
        ) {
            Text(
                "Eliminar lista",
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun ItemLista(
    nombre: String,
    estaCogido: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    // Ya no usamos rememberSaveable aquí. El estado fluye desde el ViewModel.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Checkbox(
            checked = estaCogido,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = nombre,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge,
            // Aplicamos efectos visuales basados en el estado que viene de arriba
            textDecoration = if (estaCogido) TextDecoration.LineThrough else TextDecoration.None,
            color = if (estaCogido) Color.DarkGray else Color.Black
        )
    }
}
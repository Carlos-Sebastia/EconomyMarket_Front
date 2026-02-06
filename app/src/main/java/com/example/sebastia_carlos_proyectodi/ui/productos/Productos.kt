package com.example.sebastia_carlos_proyectodi.ui.productos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.KebabDining
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.domain.model.Producto


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PantallaProductos(
    navController : NavHostController,
    viewModel: ProductosViewModel
) {
    // Obtenemos los colores del tema
    val colores = MaterialTheme.colorScheme

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estructura principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .background(colores.background) // Fondo según tema
    ) {
        FilaChips(
            categoriaSeleccionada = uiState.categoriaSeleccionada,
            onCategoriaChange = { nueva ->
                viewModel.actualizarCategoria(nueva)
            }
        )

        val listaFiltrada = if (uiState.categoriaSeleccionada == null) {
            uiState.productos
        } else {
            uiState.productos.filter { it.categoria == uiState.categoriaSeleccionada }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }                }
                uiState.error != null -> {
                    PantallaErrorDatos(
                        mensaje = uiState.error ?: "Error desconocido",
                        onReintentar = { viewModel.cargarProductos() } // Debes tener esta función en el VM
                    )                }
                else -> {
                    AnimatedContent(targetState = listaFiltrada) { listaAMostrar ->
                        ListaProductosFiltrada(listaAMostrar)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipFiltro(opcion : String) {
    val colores = MaterialTheme.colorScheme
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text(opcion)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done Icon",
                    modifier = Modifier
                        .size(FilterChipDefaults.IconSize)
                )
            }
        } else null,

        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colores.tertiary,
            selectedLabelColor = colores.onSecondary,
            selectedLeadingIconColor = colores.onSecondary,
            containerColor = Color.White,
            labelColor = colores.onSecondary
        )
    )
}

@Composable
fun FilaChips(
    categoriaSeleccionada: String?,
    onCategoriaChange : (String) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val chipList : List<String> = listOf(
        "Carne",
        "Pescado",
        "Vino",
        "Fruta y Verdura",
    )
    val nombre : String
    var selected by remember { mutableStateOf("") }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        items(chipList) { opcion ->
            FilterChip(
                selected = (opcion == categoriaSeleccionada),
                onClick = { onCategoriaChange(opcion) },
                label = { Text(opcion) },
                leadingIcon = if (opcion == categoriaSeleccionada) {
                    {
                        Icon(
                            imageVector = when (opcion) {
                                "Carne" -> Icons.Default.KebabDining
                                "Pescado" -> Icons.Default.SetMeal
                                "Vino" -> Icons.Default.WineBar
                                "Fruta y Verdura" -> Icons.Default.Eco
                                else -> Icons.Default.Label
                            }
                            ,
                            tint = colores.onTertiary,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colores.tertiary,
                    selectedLabelColor = colores.onTertiary
                )
            )
        }
    }
}

@Composable
fun ItemProducto(
    producto : Producto
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        producto.imagen, "drawable", context.packageName
    )
    // Acceso al esquema de colores
    val colores = MaterialTheme.colorScheme

    // Card que contiene toda la información del producto
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = colores.onSecondary
        ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(producto.imagen)
                    .crossfade(true)
                    .build(),

                //AÑADIR IMÁGENES DE CARGA Y ERROR!!!
                placeholder = painterResource(R.drawable.home_mesa),
                error = painterResource(R.drawable.ic_launcher_foreground),
                //AÑADIR IMÁGENES DE CARGA Y ERROR!!!

                contentDescription = producto.nombre,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop

            )

            // Nombre del producto
            Text(
                producto.nombre,
                modifier = Modifier.padding(8.dp),
            )

            // Precio del producto
            Text(
                producto.nombre,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun ListaProductosFiltrada(lista : List<Producto>) {
    // Grid de 2 columnas
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize(),
        content = {
            items(lista) { producto ->
                // Mostramos un card por producto
                ItemProducto(producto = producto)
            }
        }
    )
}

@Composable
fun PantallaErrorDatos(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(

            //AÑADIR IMÁGENES DE CARGA Y ERROR!!!
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            //AÑADIR IMÁGENES DE CARGA Y ERROR!!!

            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = mensaje,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        androidx.compose.material3.Button(onClick = onReintentar) {
            Text("Reintentar conexión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductosPreview() {
    PantallaProductos(navController = rememberNavController(), viewModel())
}

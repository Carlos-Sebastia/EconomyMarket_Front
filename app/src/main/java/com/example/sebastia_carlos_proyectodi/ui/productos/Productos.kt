package com.example.sebastia_carlos_proyectodi.ui.productos

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.domain.model.Producto

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PantallaProductos(
    navController : NavHostController,
    viewModel: ProductosViewModel
) {
    val colores = MaterialTheme.colorScheme
    // Estado del ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .background(colores.background) // Fondo según tema
    ) {
        //Filtros para seleccionar categoría
        FilaChips(
            categoriaSeleccionada = uiState.categoriaSeleccionada,
            onCategoriaChange = { nueva ->
                viewModel.actualizarCategoria(nueva)
            }
        )

        // Lista de productos según categoría seleccionada
        val productosAMostrar = uiState.productos

        Box(modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
        ) {
            //Se muestra una vista diferente según el estado de carga de la pantalla
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }                }
                uiState.error != null -> {
                    PantallaErrorDatos(
                        mensaje = uiState.error ?: "Error desconocido",
                        onReintentar = { viewModel.cargarProductos() }
                    )                }
                else -> {
                    //Si se carga la pantalla correctamente, se muestran los productos
                    ListaProductosFiltrada(
                        lista = productosAMostrar,
                        idsEnLista = uiState.idsEnLista,
                        categoriaSeleccionada = uiState.categoriaSeleccionada,
                        onUpdateItem = { producto, isInList ->
                            viewModel.updateListaItems(producto, isInList) }
                    )
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
    onCategoriaChange : (String?) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    //Lista de categorías de productos
    val chipList : List<String> = listOf(
        "Carne",
        "Pescado",
        "Vino",
        "Fruta y Verdura",
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        items(chipList) { opcion ->
            val estaSeleccionado = (opcion == categoriaSeleccionada)
            FilterChip(
                selected = (opcion == categoriaSeleccionada),
                onClick = {
                    //Al pulsar categoría seleccionada, se anula el filtro
                    if (estaSeleccionado) {
                    onCategoriaChange(null)
                    } else { onCategoriaChange(opcion) }},
                label = { Text(opcion) },
                leadingIcon = if (opcion == categoriaSeleccionada) {
                    {
                        Icon(
                            //Según la categoía, se muestra un icono en cada chip
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
    producto : Producto,
    estaEnLista: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colores = MaterialTheme.colorScheme

    val context = LocalContext.current
    //Se busca el ID del recurso por nombre. Remember permite recordar el valor y no volver a buscarlo al visualizarlo de nuevo
    val resId = remember(producto.imagen) {
        if (producto.imagen.isNullOrEmpty()) {
            R.drawable.broken_image
        } else {
            val id = context.resources.getIdentifier(
                producto.imagen.trim(),
                "drawable",
                context.packageName
            )
            if (id != 0) id else R.drawable.broken_image
        }
    }

    // Card que contiene toda la información del producto
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )

                // Nombre del producto
                Text(
                    producto.nombre,
                    modifier = Modifier.padding(8.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        producto.precio,
                        modifier = Modifier.padding(8.dp),
                    )

                    //Cambia el icono dependiendo de si el producto está o no en la lista
                    IconButton(
                        onClick = { onCheckedChange(estaEnLista) },
                    ) {
                        Icon(
                            imageVector = if (estaEnLista) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (estaEnLista) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

            }

    }
}

@Composable
fun ListaProductosFiltrada(
    lista : List<Producto>,
    idsEnLista: List<Long>,
    categoriaSeleccionada: String?,
    onUpdateItem: (Producto, Boolean) -> Unit
    ) {

    //Estado de la tabla de productos para recordar su posición del scroll
    val gridState = rememberLazyGridState()
    //Volver arriba de la columna al cambiar de categoría de productos
    LaunchedEffect(categoriaSeleccionada) {
        gridState.animateScrollToItem(0)
    }

    LazyVerticalGrid(
        state = gridState, //Se añade el estado de la tabla de productos
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize(),
        content = {
            //Dibuja los elementos visibles
            items(lista) { producto ->
                val estaEnLista = producto.id in idsEnLista
                ItemProducto(
                    producto = producto,
                    estaEnLista = estaEnLista,
                    onCheckedChange = { marcado ->
                        onUpdateItem(producto, marcado)
                    }
                )
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
            painter = painterResource(id = R.drawable.broken_image),
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

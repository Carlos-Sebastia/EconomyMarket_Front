package com.example.sebastia_carlos_proyectodi.ui.tiendas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import com.example.sebastia_carlos_proyectodi.ui.productos.PantallaErrorDatos

@Composable
fun PantallaTiendas(
    navController: NavHostController,
    viewModel: TiendasViewModel
    ) {
    // Estado del ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        //Diferentes pantallas según estado de carga
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }            }
            uiState.error != null -> {
                PantallaErrorDatos(
                    mensaje = uiState.error ?: "Error al cargar tiendas",
                    onReintentar = { viewModel.cargarTiendas() }
                )            }
            else -> {
                //Si está bien, muestra las tiendas
                ListaTiendas(uiState.tiendas)
            }
        }
    }
}

//Diseño de cada tarjeta de tienda
@Composable
fun ItemTienda(tienda: Tienda) {
    val colores = MaterialTheme.colorScheme

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = colores.onSecondary
        ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Se cargan imágenes de internet
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tienda.imagenUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading_image), //Imagen mientras se descarga
                error = painterResource(R.drawable.broken_image), //Imagen de error de descarga
                contentDescription = "Foto tienda ${tienda.ciudad}",
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            //Datos de la tienda
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    tienda.ciudad,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(bottom = 15.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Divider()

                Text(
                    tienda.direccion,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 15.dp),
                    color = Color.Black

                )
            }
        }
    }
}

@Composable
fun ListaTiendas(lista: List<Tienda>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(lista) { tienda ->
            ItemTienda(tienda)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PantallaTiendasPreview() {
    PantallaTiendas(navController = rememberNavController(), viewModel())
}
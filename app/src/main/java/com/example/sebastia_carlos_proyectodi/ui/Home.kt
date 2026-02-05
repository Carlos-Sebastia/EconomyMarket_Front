package com.example.sebastia_carlos_proyectodi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.ui.productos.ProductosViewModel
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val mensajeBanner : String = "Hasta un 30% de descuento en productos seleccionados",
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { HomeViewModel() }
        }
    }
}

@Composable
fun PantallaPrincipal(
    navController : NavHostController,
    viewModel : HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colores.background)
    ) {

        //Primer card con foto y texto
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(R.drawable.home_mesa),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Altura fija para la imagen
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cenas inolvidables",
                        style = MaterialTheme.typography.titleLarge, // Usa estilos del tema
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Descubre los productos que protagonizarán tu mesa.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = colores.onSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Segundo card (descuentos)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colores.secondary,
                contentColor = colores.onSecondary),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = uiState.mensajeBanner,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        // Tercer card (socio)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(R.drawable.home_socio),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp), // Altura fija para la imagen
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¡Hazte socio!",
                        style = MaterialTheme.typography.titleLarge, // Usa estilos del tema
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Y empieza a disfrutar de las ventajas de ser Economy.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = colores.onSecondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaPrincipal(navController = rememberNavController())
    }
}
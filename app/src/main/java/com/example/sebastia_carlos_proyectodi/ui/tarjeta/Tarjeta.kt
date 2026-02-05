package com.example.sebastia_carlos_proyectodi.ui.tarjeta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme

data class TarjetaUiState (
    val ticketDigital : Boolean = false,
    val economyPay : Boolean = true
)

@Composable
fun PantallaTarjeta(
    navController : NavHostController,
    viewModel : TarjetaViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme

    var mostrarAvisoLegal by rememberSaveable { mutableStateOf(true) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background),
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(colores.secondary)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.End),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "botón cerrar pantalla tarjeta",
                        tint = colores.onSecondary,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }

                Text(
                    text = "Tarjeta Economy Plus",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = colores.onSecondary,
                )

                Text(
                    text = "Muestra el QR en caja para disfrutar de las ventajas.",
                    fontSize = 15.sp,
                    color = colores.onSecondary

                )
            }

            // Tarjeta socio
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height(200.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo tarjeta",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = "Carlos Sebastiá\n20924519C",
                            textAlign = TextAlign.Center,
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.tarjeta_qr),
                        contentDescription = "qr tarjeta",
                        modifier = Modifier
                            .size(140.dp)
                            .weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            if(mostrarAvisoLegal) {
                Card(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colores.secondary
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Text(
                            text = "Al usar la tarjeta aceptas las condiciones legales.",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            fontSize = 12.sp
                        )
                        IconButton(
                            onClick = { mostrarAvisoLegal = false },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar mensaje"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SwitchCard(
                Icons.Filled.Wallet,
                "Pagar con Economy Pay",
            uiState.economyPay,
                onCheckedChange = { viewModel.onEconomyPay(it) }
            )

            SwitchCard(
                Icons.Default.Park,
                "Solo ticket digital",
                state = uiState.ticketDigital,
                onCheckedChange = { viewModel.onTicketDigitalChanged(it) }
            )
        }
    }
}

@Composable
fun SwitchCard(
    icono : ImageVector,
    texto : String,
    state : Boolean,
    onCheckedChange : (Boolean) -> Unit
    ) {
    var state by rememberSaveable { mutableStateOf(false) }
    val colores = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(75.dp),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                )
                Text(texto)
            }
            Switch(
                checked = state,
                onCheckedChange =  onCheckedChange,
                thumbContent = if (state) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Thumb check",
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.Black
                        )
                    }
                } else null,
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.White,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray,
                    checkedTrackColor = colores.secondary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaTarjeta(
            navController = rememberNavController(),
            viewModel())
    }
}

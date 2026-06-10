package com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoDato
import kotlinx.coroutines.launch

@Composable
fun PantallaValidacionCambioContraseña(
    navController : NavHostController,
    viewModel : CambioContraseñaViewModel = viewModel(factory = CambioContraseñaViewModel.Factory)
) {
    BackHandler(enabled = true) { }
    val colores = MaterialTheme.colorScheme
    val context = androidx.compose.ui.platform.LocalContext.current

    // Estados para los campos de texto
    var dni by remember { mutableStateOf("") }
    var nombreMascota by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Cambio de contraseña",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Por favor, introduce tu DNI y el nombre de tu primera mascota.",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reutilización de componente para los campos

        CampoDato(value = dni, onValueChange = { dni = it.uppercase() }, placeholder = "DNI")
        Spacer(modifier = Modifier.height(20.dp))


        CampoDato(value = nombreMascota, onValueChange = { nombreMascota = it }, placeholder = "Nombre de tu primera mascota", keyboardCapitalization = KeyboardCapitalization.Words)
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val scope = rememberCoroutineScope()
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    scope.launch {
                        val esValido = viewModel.validarMascota(dni.trim(), nombreMascota.trim())
                        if (esValido) {
                            navController.navigate("cambio_contraseña")
                        } else {
                            nombreMascota = ""
                            Toast.makeText(context,"Credenciales incorrectas", Toast.LENGTH_SHORT).show()

                        }
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary
                )
            ) {
                Text(text = "Validar")
            }

            Text(text = "Volver atrás",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.popBackStack()

                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValidacionCambioContraseñaPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaValidacionCambioContraseña(navController = rememberNavController())
    }
}

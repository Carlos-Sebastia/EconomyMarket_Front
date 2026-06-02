package com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoContraseña
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

fun esContrasenaSegura(contrasena: String): Boolean {
    val longitud = contrasena.length >= 8
    val mayuscula = contrasena.any { it.isUpperCase() }
    val minuscula = contrasena.any { it.isLowerCase() }
    val numero = contrasena.any { it.isDigit() }
    val simbolo = contrasena.any { !it.isLetterOrDigit() }

    return longitud && mayuscula && minuscula && numero && simbolo
}

@Composable
fun PantallaCambioContraseña(
    navController : NavHostController,
    viewModel : CambioContraseñaViewModel = viewModel(factory = CambioContraseñaViewModel.Factory)
) {
    BackHandler(enabled = true) { }
    val colores = MaterialTheme.colorScheme
    val context = androidx.compose.ui.platform.LocalContext.current

    // Estados para los campos de texto
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVerificacion by remember { mutableStateOf("") }

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
            text = "Por favor, introduce la nueva contraseña.",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reutilización de componente para los campos

        CampoContraseña(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = "Nueva contraseña"
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoContraseña(
            value = contrasenaVerificacion,
            onValueChange = { contrasenaVerificacion = it },
            placeholder = "Verificación de la  nueva contraseña"
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "* La contraseña debe tener como mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.",
            fontSize = 12.sp,
        )

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
                        if (contrasena == contrasenaVerificacion) {
                            if (esContrasenaSegura(contrasena)) {
                                val esValido = viewModel.cambiarContrasena(contrasena)
                                    if (esValido) {
                                        navController.navigate("login") { popUpTo("login") { inclusive = true } }
                                        Toast.makeText(context, "Contraseña cambiada correctamente.",Toast.LENGTH_LONG).show()
                                    }
                                }
                                else {
                                    contrasena = ""
                                    contrasenaVerificacion = ""
                                    Toast.makeText(context,"La contraseña no cumple los requisitos de seguridad.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            contrasena = ""
                            contrasenaVerificacion = ""
                            Toast.makeText(context,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()

                        }
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary
                )
            ) {
                Text(text = "Cambiar contraseña")
            }

            Text(text = "Cancelar",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.popBackStack("validacion_cambio_contraseña", inclusive = true)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CambioContraseñaPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaCambioContraseña(navController = rememberNavController())
    }
}

package com.example.sebastia_carlos_proyectodi.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.sebastia_carlos_proyectodi.R
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoContraseña
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoDato
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun PantallaLogin(
    navController : NavHostController,
    viewModel : LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val colores = MaterialTheme.colorScheme
    val context = androidx.compose.ui.platform.LocalContext.current

    // Estados para los campos de texto
    var dni by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Logo de la empresa
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo para el DNI
        CampoDato(value = dni,
            onValueChange = { dni = it },
            placeholder = "DNI",
            icon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(20.dp))

        //Campo para la contraseña
        CampoContraseña(value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = "Contraseña",
            icon = Icons.Default.Lock
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "¿Olvidaste la contraseña?",
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("validacion_cambio_contraseña")
                }
        )

        Spacer(modifier = Modifier.height(50.dp))


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
                        val esValido = viewModel.validarUsuario(dni, contrasena)
                        if (esValido) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            contrasena = ""
                            Toast.makeText(context,"Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary
                )
            ) {
                Text(text = "Iniciar sesión")
            }

            Text(text = "Crear una cuenta",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("creacion_usuario")
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaLogin(navController = rememberNavController())
    }
}

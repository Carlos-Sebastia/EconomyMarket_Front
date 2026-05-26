package com.example.sebastia_carlos_proyectodi.ui.creacion_usuario

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
    val scrollState = rememberScrollState()

    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var mascota by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var contraseñaVerificacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(scrollState)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Alta nuevo usuario",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Rellena los campos con tu información personal, por favor.",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reutilización de componente para los campos

        CampoDato(value = dni, onValueChange = { dni = it }, placeholder = "DNI")
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = nombre, onValueChange = { nombre = it }, placeholder = "Nombre")
        Spacer(modifier = Modifier.height(20.dp))
        
        CampoDato(value = apellidos, onValueChange = { apellidos = it }, placeholder = "Apellidos")
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, placeholder = "Fecha de nacimiento (dd/mm/aaaa)")
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = email, onValueChange = { email = it }, placeholder = "Correo electrónico")
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = mascota, onValueChange = { mascota = it }, placeholder = "Nombre de tu primera mascota")
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "* Este dato será usado en caso de olvido de la contraseña.",
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoContraseña(
            value = contraseña,
            onValueChange = { contraseña = it }, 
            placeholder = "Contraseña"
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoContraseña(
            value = contraseñaVerificacion, 
            onValueChange = { contraseñaVerificacion = it }, 
            placeholder = "Verificación de la contraseña"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var terminosAceptados by remember { mutableStateOf(false) }

            Checkbox(
                checked = terminosAceptados,
                onCheckedChange = { terminosAceptados = !terminosAceptados }
            )
            Text(
                text = "Acepto los términos y condiciones.",
                fontSize = 14.sp,
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary
                )
            ) {
                Text(text = "Crear cuenta")
            }

            Text(text = "¿Ya tienes cuenta? Inicia sesión",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun CampoDato(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Composable
fun CampoContraseña(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String) {

    var contraseñaVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    //.fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                visualTransformation = if (contraseñaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            IconButton(
                onClick = { contraseñaVisible = !contraseñaVisible }
            ) {
                Icon(
                    imageVector = if (contraseñaVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "Icono ver contraseña",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreacionUsarioPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaPrincipal(navController = rememberNavController())
    }
}

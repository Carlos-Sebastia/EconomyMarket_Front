package com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

data class ValidacionCambioContraseñaUiState(
    val mensajeBanner : String = "Hasta un 30% de descuento en productos seleccionados",
)

class ValidacionCambioContraseñaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ValidacionCambioContraseñaUiState())
    val uiState : StateFlow<ValidacionCambioContraseñaUiState> = _uiState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ValidacionCambioContraseñaViewModel() }
        }
    }
}

@Composable
fun PantallaValidacionCambioContraseña(
    navController : NavHostController,
    viewModel : ValidacionCambioContraseñaViewModel = viewModel()
) {
    BackHandler(enabled = true) { }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()

    // Estados para los campos de texto
    var nombreMascota by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(scrollState)
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
            text = "Por favor, introduce el nombre de tu primera mascota.",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reutilización de componente para los campos

        CampoDato(value = nombreMascota, onValueChange = { nombreMascota = it }, placeholder = "Nombre de tu primera mascota")
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
                Text(text = "Validar")
            }

            Text(text = "Cancelar",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("login") {
                            popUpTo("validacion_cambio_contraseña") { inclusive = true }
                        }
                    }
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

@Preview(showBackground = true)
@Composable
fun ValidacionCambioContraseñaPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaValidacionCambioContraseña(navController = rememberNavController())
    }
}

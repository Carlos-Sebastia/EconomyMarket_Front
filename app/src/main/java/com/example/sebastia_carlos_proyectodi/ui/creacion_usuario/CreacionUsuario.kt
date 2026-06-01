package com.example.sebastia_carlos_proyectodi.ui.creacion_usuario

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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.esContrasenaSegura
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoDato
import com.example.sebastia_carlos_proyectodi.ui.componentes.CampoContraseña
import kotlinx.coroutines.launch


fun esEmailValido(email: String): Boolean {
    val partes = email.split("@")
    if (partes.size !=2) return false

    val nombre = partes[0]
    val dominio = partes[1]
    if (nombre.isEmpty() || dominio.isEmpty()) return false
    if (!dominio.contains(".")) return false

    return true
}

fun esFechaValida(fecha: String): Boolean {
    if (fecha.length != 10) return false
    if (fecha[2] != '-' || fecha[5] != '-') return false
    for (i in fecha.indices) {
        if (i == 2 || i == 5) continue
        if (!fecha[i].isDigit()) return false
    }

    val dia = fecha.substring(0,2).toIntOrNull() ?: return false
    val mes = fecha.substring(3,5).toIntOrNull() ?: return false
    val anyo = fecha.substring(6,10).toIntOrNull() ?: return false

    if (dia !in 1..31) return false
    if (mes !in 1..12) return false

    val anyoActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    if (anyo < 1900 || anyo > anyoActual) return false

    return true
}


@Composable
fun PantallaCreacionUsuario(
    navController : NavHostController,
    viewModel : CreacionUsuarioViewModel = viewModel(factory = CreacionUsuarioViewModel.Factory),

) {
    BackHandler(enabled = true) { }
    val colores = MaterialTheme.colorScheme
    val context = androidx.compose.ui.platform.LocalContext.current
    val scrollState = rememberScrollState()
    var terminosAceptados by remember { mutableStateOf(false) }


    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var mascota by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVerificacion by remember { mutableStateOf("") }

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

        CampoDato(value = dni, onValueChange = { dni = it.uppercase().trim() }, placeholder = "DNI")
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = nombre, onValueChange = { nombre = it }, placeholder = "Nombre", keyboardCapitalization = KeyboardCapitalization.Words)
        Spacer(modifier = Modifier.height(20.dp))
        
        CampoDato(value = apellidos, onValueChange = { apellidos = it }, placeholder = "Apellidos", keyboardCapitalization = KeyboardCapitalization.Words)
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, placeholder = "Fecha de nacimiento (dd/mm/aaaa)", keyboardType = KeyboardType.Number)
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = email, onValueChange = { email = it }, placeholder = "Correo electrónico", keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(20.dp))

        CampoDato(value = mascota, onValueChange = { mascota = it }, placeholder = "Nombre de tu primera mascota", keyboardCapitalization = KeyboardCapitalization.Words)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "* Este dato será usado en caso de olvido de la contraseña.",
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoContraseña(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = "Contraseña"
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoContraseña(
            value = contrasenaVerificacion,
            onValueChange = { contrasenaVerificacion = it },
            placeholder = "Verificación de la contraseña"
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "* La contraseña debe tener como mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.",
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = terminosAceptados,
                onCheckedChange = { terminosAceptados = it }
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
            val scope = rememberCoroutineScope()
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (dni.isBlank() || nombre.isBlank() || apellidos.isBlank() || fechaNacimiento.isBlank() || email.isBlank() || mascota.isBlank() || contrasena.isBlank()) {
                        Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_LONG).show()
                    } else if (!terminosAceptados) {
                        Toast.makeText(context, "Por favor, acepta los términos y condiciones", Toast.LENGTH_LONG).show()
                    } else if (!esEmailValido(email)) {
                        Toast.makeText(context, "El email no es válido", Toast.LENGTH_LONG).show()
                    } else if (!esFechaValida(fechaNacimiento)) {
                        Toast.makeText(context, "La fecha de nacimiento no es válida", Toast.LENGTH_LONG).show()
                    }
                    else {
                        scope.launch {
                            if (contrasena != contrasenaVerificacion) {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                            } else if (!esContrasenaSegura(contrasena)) {
                                Toast.makeText(context, "La contraseña no cumple los requisitos de seguridad", Toast.LENGTH_LONG).show()
                            } else {
                                val esValido = viewModel.crearUsuario(
                                    dni.trim(),
                                    nombre.trim(),
                                    apellidos.trim(),
                                    fechaNacimiento.trim(),
                                    email.trim().lowercase(),
                                    mascota.trim(),
                                    contrasena
                                )
                                if (esValido) {
                                    Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
                                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                                } else {
                                    Toast.makeText(context, "El DNI ya está registrado", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                },
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
                    .clickable {
                        navController.navigate("login")
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreacionUsarioPreview() {
    Sebastia_carlos_proyectoDITheme {
        PantallaCreacionUsuario(navController = rememberNavController())
    }
}

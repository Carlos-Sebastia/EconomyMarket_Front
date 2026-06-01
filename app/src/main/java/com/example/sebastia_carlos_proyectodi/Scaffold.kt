package com.example.sebastia_carlos_proyectodi

import android.icu.number.Scale.none
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sebastia_carlos_proyectodi.ui.HomeViewModel
import com.example.sebastia_carlos_proyectodi.ui.PantallaLista
import com.example.sebastia_carlos_proyectodi.ui.PantallaPrincipal
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.CambioContraseñaViewModel
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.PantallaCambioContraseña
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.PantallaValidacionCambioContraseña
import com.example.sebastia_carlos_proyectodi.ui.creacion_usuario.CreacionUsuarioViewModel
import com.example.sebastia_carlos_proyectodi.ui.creacion_usuario.PantallaCreacionUsuario
import com.example.sebastia_carlos_proyectodi.ui.login.LoginViewModel
import com.example.sebastia_carlos_proyectodi.ui.login.PantallaLogin
import com.example.sebastia_carlos_proyectodi.ui.productos.PantallaProductos
import com.example.sebastia_carlos_proyectodi.ui.productos.ProductosViewModel
import com.example.sebastia_carlos_proyectodi.ui.tarjeta.PantallaTarjeta
import com.example.sebastia_carlos_proyectodi.ui.tarjeta.TarjetaViewModel
import com.example.sebastia_carlos_proyectodi.ui.tiendas.PantallaTiendas
import com.example.sebastia_carlos_proyectodi.ui.tiendas.TiendasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomScaffold(navController: NavHostController) {
    // La raíz ahora solo gestiona el NavGraph principal
    AppNavGraph(navController = navController)
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val activity = LocalActivity.current as ComponentActivity
    
    val resetPasswordViewModel: CambioContraseñaViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = CambioContraseñaViewModel.Factory
    )

    val tarjetaViewModel: TarjetaViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = TarjetaViewModel.Factory
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantallas sin Scaffold
        composable("login") {
            PantallaLogin(navController)
        }
        composable("creacion_usuario") {
            PantallaCreacionUsuario(navController)
        }
        composable("validacion_cambio_contraseña") {
            PantallaValidacionCambioContraseña(navController, resetPasswordViewModel)
        }
        composable("cambio_contraseña") {
            PantallaCambioContraseña(navController, resetPasswordViewModel)
        }
        composable("tarjeta") { PantallaTarjeta(navController, tarjetaViewModel) }


        // Pantallas con Scaffold
        composable("home") { MainAppContainer(navController, "home") }
        composable("productos") { MainAppContainer(navController, "productos") }
        composable("tiendas") { MainAppContainer(navController, "tiendas") }
        composable("lista") { MainAppContainer(navController, "lista") }
    }
}

@Composable
fun MainAppContainer(rootNavController: NavHostController, startRoute: String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val activity = LocalActivity.current as ComponentActivity
    
    // ViewModels necesarios para las pantallas del Scaffold
    val productosViewModel: ProductosViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = ProductosViewModel.Factory
    )
    val tiendasViewModel: TiendasViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = TiendasViewModel.Factory
    )

    MyModalDrawer(
        navController = rootNavController,
        drawerState = drawerState
    ) {
        val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: startRoute

        val topBarConfig = when (currentRoute) {
            "home" -> TopBarConfig("EconomyMarket", FontFamily(Font(R.font.jumpswinter)), 20.sp, MaterialTheme.colorScheme.primary)
            "tiendas" -> TopBarConfig("Tiendas", FontFamily(Font(R.font.bebas_regular)), 30.sp, MaterialTheme.colorScheme.onSecondary)
            "productos" -> TopBarConfig("Productos", FontFamily(Font(R.font.bebas_regular)), 30.sp, MaterialTheme.colorScheme.onSecondary)
            "lista" -> TopBarConfig("Mi Lista", FontFamily(Font(R.font.bebas_regular)), 30.sp, MaterialTheme.colorScheme.onSecondary)
            else -> TopBarConfig("", null, 20.sp, Color.Transparent)
        }

        Scaffold(
            topBar = { MyTopAppBar(topBarConfig, drawerState) },
            bottomBar = { MyBottomAppBar(rootNavController) },
            floatingActionButton = { MyFAB(rootNavController) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                // Aquí cargamos directamente la pantalla correspondiente según la ruta del rootNavController
                when (currentRoute) {
                    "home" -> PantallaPrincipal(rootNavController)
                    "productos" -> PantallaProductos(rootNavController, productosViewModel)
                    "tiendas" -> PantallaTiendas(rootNavController, tiendasViewModel)
                    "lista" -> PantallaLista(rootNavController, productosViewModel)
                }
            }
        }
    }
}

@Composable
fun MyModalDrawer(navController: NavHostController, drawerState: DrawerState, contenido: @Composable () -> Unit) {
    val corutina = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                NavBarHeader()
                Column(modifier = Modifier.padding(15.dp)) {
                    NavigationItem("Inicio", Icons.Default.Home) { 
                        navController.navigate("home"); corutina.launch { drawerState.close() } 
                    }
                    NavigationItem("Tiendas", Icons.Default.Place) { 
                        navController.navigate("tiendas"); corutina.launch { drawerState.close() } 
                    }
                    NavigationItem("Ajustes", Icons.Default.Settings) { }
                }
            }
        },
        content = contenido
    )
}

@Composable
fun NavigationItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null); Spacer(Modifier.width(10.dp)); Text(text)
        }},
        selected = false,
        onClick = onClick
    )
}

@Composable
fun NavBarHeader() {
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary).padding(20.dp)) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.height(150.dp).fillMaxWidth())
    }
}

data class TopBarConfig(val title: String, val fontFamily: FontFamily?, val fontSize: TextUnit, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(config: TopBarConfig, myDrawerState: DrawerState) {
    val corutina = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = { Text(config.title, fontFamily = config.fontFamily, color = config.color, fontSize = config.fontSize) },
        navigationIcon = {
            IconButton(onClick = { corutina.launch { myDrawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
            }
        }
    )
}

@Composable
fun MyBottomAppBar(navController: NavHostController) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.secondary) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconoBotonBottomBar(Icons.Default.ShoppingCart, "Productos", "productos", navController)
            IconoBotonBottomBar(Icons.Filled.ListAlt, "Mi lista", "lista", navController)
        }
    }
}

@Composable
fun IconoBotonBottomBar(icono: ImageVector, texto: String, ruta: String, navController: NavHostController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { navController.navigate(ruta) }) { Icon(icono, contentDescription = null) }
        Text(texto)
    }
}

@Composable
fun MyFAB(navController: NavHostController) {
    val colores = MaterialTheme.colorScheme
    var showLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            if (!showLoading) {
                showLoading = true
                scope.launch {
                    delay(2000)
                    navController.navigate("tarjeta")
                }
            }
        },
        containerColor = colores.secondary,
        contentColor = colores.onSecondary,
        shape = RoundedCornerShape(50)
    ) {
        Icon(
            imageVector = Icons.Filled.CreditCard, contentDescription = "FAB icon"
        )
        Box(
            contentAlignment = Alignment.Center,
        ) {
            if (showLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(68.dp),
                    color = colores.onSecondary,
                    strokeWidth = 3.dp,
                )
            }
        }
    }
}

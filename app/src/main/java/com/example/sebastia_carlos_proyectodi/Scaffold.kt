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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sebastia_carlos_proyectodi.data.formatearFechaNotificacion
import com.example.sebastia_carlos_proyectodi.ui.HomeViewModel
import com.example.sebastia_carlos_proyectodi.ui.PantallaLista
import com.example.sebastia_carlos_proyectodi.ui.PantallaPrincipal
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.CambioContraseñaViewModel
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.PantallaCambioContraseña
import com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña.PantallaValidacionCambioContraseña
import com.example.sebastia_carlos_proyectodi.ui.creacion_usuario.CreacionUsuarioViewModel
import com.example.sebastia_carlos_proyectodi.ui.creacion_usuario.PantallaCreacionUsuario
import com.example.sebastia_carlos_proyectodi.ui.cuenta_usuario.PantallaCuentaUsuario
import com.example.sebastia_carlos_proyectodi.ui.login.LoginViewModel
import com.example.sebastia_carlos_proyectodi.ui.login.PantallaLogin
import com.example.sebastia_carlos_proyectodi.ui.notificaciones.NotificacionViewModel
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
        composable("cuenta") { MainAppContainer(navController, "cuenta") }
    }
}

@Composable
fun MainAppContainer(rootNavController: NavHostController, startRoute: String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val activity = LocalActivity.current as ComponentActivity
    
    // ViewModels necesarios para las pantallas del Scaffold
    val notificacionViewModel: NotificacionViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = NotificacionViewModel.Factory
    )
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
            "cuenta" -> TopBarConfig("Cuenta de usuario", FontFamily(Font(R.font.bebas_regular)), 30.sp, MaterialTheme.colorScheme.onSecondary)
            else -> TopBarConfig("", null, 20.sp, Color.Transparent)
        }

        Scaffold(
            topBar = { MyTopAppBar(topBarConfig, drawerState, rootNavController, notificacionViewModel) },
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
                    "cuenta" -> PantallaCuentaUsuario(rootNavController)
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
                Column(modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight()) {
                    NavigationItem("Inicio", Icons.Default.Home) { 
                        navController.navigate("home"); corutina.launch { drawerState.close() } 
                    }
                    NavigationItem("Tiendas", Icons.Default.Place) { 
                        navController.navigate("tiendas"); corutina.launch { drawerState.close() } 
                    }
                    NavigationItem("Cuenta de usuario", Icons.Default.PersonPin) {
                        navController.navigate("cuenta"); corutina.launch { drawerState.close() }
                    }
                    NavigationItem("Cerrar menú", Icons.AutoMirrored.Filled.ArrowBack) {
                        corutina.launch { drawerState.close() }
                    }
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Imagen Modal Drawer",
            Modifier
                .height(200.dp)
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.Start),

            contentScale = ContentScale.Fit
        )
    }
}

data class TopBarConfig(
    val title : String,
    val fontFamily: FontFamily? = null,
    val fontSize: TextUnit,
    val color : Color = Color.Unspecified,
    val actions : @Composable (() -> Unit)? = null
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    config: TopBarConfig,
    myDrawerState: DrawerState,
    navController: NavHostController,
    viewModel: NotificacionViewModel
) {
    val colores = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) }
    val corutina = rememberCoroutineScope()
    val rotacion = remember { Animatable(0f) }
    val notificaciones by viewModel.notificaciones.collectAsStateWithLifecycle()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        title = {
            Text(
                text = config.title,
                fontFamily = config.fontFamily,
                color = config.color,
                fontSize = config.fontSize,
            )
        },
        navigationIcon = {
            IconButton(onClick = { corutina.launch { myDrawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
            }
        },

        actions = {
            Box {
                IconButton(onClick = {
                    isExpanded = true
                    corutina.launch {
                        repeat(6) { index ->
                            rotacion.animateTo(
                                targetValue = if (index % 2 == 0) -25f else 25f,
                                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                            )
                        }
                        rotacion.animateTo(0f, animationSpec = tween(durationMillis = 100))
                    }
                }) {
                    BadgedBox(
                        badge = {
                            if (notificaciones.isNotEmpty()) {
                                Badge { Text(notificaciones.size.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color.Black,
                            modifier = Modifier.graphicsLayer(
                                transformOrigin = TransformOrigin(0.5f, 0.0f),
                                rotationZ = rotacion.value
                            )
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .width(280.dp)
                        .background(colores.background)
                ) {
                    if (notificaciones.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No tienes notificaciones", color = Color.Gray) },
                            onClick = { isExpanded = false }
                        )
                    } else {
                        // Se muestran 6 notificaciones, como máximo
                        notificaciones.take(6).forEach { notificacion ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(notificacion.titulo, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), fontSize = 14.sp)
                                            // Se utiliza la función para mapear la fecha
                                            Text(notificacion.fecha.formatearFechaNotificacion(), fontSize = 10.sp, color = Color.Gray)
                                        }
                                        Text(notificacion.mensaje, fontSize = 12.sp, color = Color.DarkGray)
                                    }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { viewModel.borrarNotificacion(notificacion.id) }) {
                                        Icon(Icons.Default.Close, contentDescription = "Borrar", modifier = Modifier.size(18.dp))
                                    }
                                },
                                onClick = { }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                        }
                    }
                }
            }
        }
    )
}


                /*actions = {
                    Box {
                        IconButton(
                            onClick = {
                                isExpanded = true
                                corutina.launch {
                                    repeat(6) { index ->
                                        rotacion.animateTo(
                                            targetValue = if (index % 2 == 0) -25f else 25f,
                                            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                                        )
                                    }
                                    rotacion.animateTo(0f, animationSpec = tween(durationMillis = 100))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = Color.Black,
                                modifier = Modifier.graphicsLayer(
                                    transformOrigin = TransformOrigin(0.5f, 0.0f),
                                    rotationZ = rotacion.value
                                )
                            )
                        }

                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false },
                            modifier = Modifier.background(colores.background)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(
                                    Icons.Default.Inbox,
                                    null,
                                    tint = Color.Black) },
                                text = { Text(
                                    "No tienes notificaciones",
                                    color = Color.Black) },
                                onClick = {
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }*/

@Composable
fun MyBottomAppBar(navController : NavHostController) {
    val colores = MaterialTheme.colorScheme

    BottomAppBar(
        containerColor = colores.secondary,
        contentColor = colores.onSecondary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoBotonBottomBar(Icons.Default.ShoppingCart, "Productos", navController)
            IconoBotonBottomBar(Icons.Filled.ListAlt, "Mi lista", navController)
        }
    }
}

@Composable
fun IconoBotonBottomBar(icono : ImageVector, texto : String, navController : NavHostController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                when (texto) {
                    "Productos" -> navController.navigate("productos")
                    "Mi lista" -> navController.navigate("lista")
                    else -> null
                }
            }
        ) {
            Icon(imageVector = icono, contentDescription = null)
        }

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

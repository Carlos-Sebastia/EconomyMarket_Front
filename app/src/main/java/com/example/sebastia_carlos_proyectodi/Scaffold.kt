package com.example.sebastia_carlos_proyectodi

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import  androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.ui.HomeViewModel
import com.example.sebastia_carlos_proyectodi.ui.PantallaLista
import com.example.sebastia_carlos_proyectodi.ui.PantallaPrincipal
import com.example.sebastia_carlos_proyectodi.ui.productos.PantallaProductos
import com.example.sebastia_carlos_proyectodi.ui.productos.ProductosViewModel
import com.example.sebastia_carlos_proyectodi.ui.tarjeta.PantallaTarjeta
import com.example.sebastia_carlos_proyectodi.ui.tarjeta.TarjetaViewModel
import com.example.sebastia_carlos_proyectodi.ui.tiendas.PantallaTiendas
import com.example.sebastia_carlos_proyectodi.ui.tiendas.TiendasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(navController : NavHostController) {

    val corutina = rememberCoroutineScope()
    val myDrawerState  = rememberDrawerState(initialValue = DrawerValue.Closed)

    MyModalDrawer(
        navController = navController,
        drawerState = myDrawerState,
        contenido = {
            MyScaffold(myDrawerState, navController)
        }
    )
}

@Composable
fun MyModalDrawer(
    navController : NavHostController,
    drawerState: DrawerState,
    contenido : @Composable () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val corutina = rememberCoroutineScope()
    val sections = listOf(
        "Inicio",
        "Tiendas",
        "Ajustes"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerTonalElevation = 100.dp,
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.clickable {
                    corutina.launch {
                        drawerState.close()
                    }
                }
            ) {
                NavBarHeader()
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .background(colores.background)
                ) {
                    sections.forEach { item ->
                        val icono = when (item) {
                            "Inicio" -> Icons.Default.Home
                            "Tiendas" -> Icons.Default.Place
                            "Ajustes" -> Icons.Default.Settings
                            else -> Icons.Default.Close
                        }
                        NavigationDrawerItem(

                            label = {
                                Row(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            },
                            selected = false,
                            onClick = {
                                when (item) {
                                    "Inicio" -> navController.navigate("home")
                                    "Tiendas" -> navController.navigate("tiendas")
                                    else -> null
                                }
                                corutina.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        contenido()
    }
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
    val actions : @Composable (() -> Unit)? = null,


)

@Composable
fun MyScaffold(
    myDrawerState : DrawerState,
    navController : NavHostController
) {
    val colores = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topBarConfig = when (currentRoute) {
        "home" ->
            TopBarConfig(
                title = "EconomyMarket",
                fontFamily = FontFamily(Font(R.font.jumpswinter)),
                color = colores.primary,
                fontSize = 20.sp
            )

        "tiendas" ->
            TopBarConfig(
                title = "Tiendas",
                fontFamily = FontFamily(Font(R.font.bebas_regular)),
                color = colores.onSecondary,
                fontSize = 30.sp
            )

        "productos" ->
            TopBarConfig(
                title = "Productos",
                fontFamily = FontFamily(Font(R.font.bebas_regular)),
                color = colores.onSecondary,
                fontSize = 30.sp
            )

        "lista" ->
            TopBarConfig(
                title = "Mi Lista",
                fontFamily = FontFamily(Font(R.font.bebas_regular)),
                color = colores.onSecondary,
                fontSize = 30.sp
            )

        else -> TopBarConfig(title = "", fontSize = 20.sp)
    }


    val esPantallaTarjeta = currentRoute == "tarjeta"

    Scaffold (
        topBar = {
            if (!esPantallaTarjeta) {
                MyTopAppBar(
                    config = topBarConfig,
                    myDrawerState = myDrawerState,
                    navController = navController
                )
            }
        },
        bottomBar = {
            if (!esPantallaTarjeta) {
                MyBottomAppBar(navController = navController)
            }
        },
        floatingActionButton = {
            if (!esPantallaTarjeta) {
                MyFAB(navController)
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppNavGraph(navController = navController)
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    config: TopBarConfig,
    myDrawerState: DrawerState,
    navController: NavHostController
) {
    val colores = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) }
    val corutina = rememberCoroutineScope()
    val rotacion = remember { Animatable(0f) }

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
}

@Composable
fun MyBottomAppBar(navController : NavHostController) {
    val colores = MaterialTheme.colorScheme

    BottomAppBar(
        containerColor = colores.secondary, //Color del fondo
        contentColor = colores.onSecondary, //Color de los iconos
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, //Espaciado uniforme
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Botones inferiores
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
            Icon(imageVector = icono, contentDescription = null) //Dibuja el icono
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
@Composable
fun AppNavGraph(navController: NavHostController) {

    val activity = LocalActivity.current as ComponentActivity
    val productosViewModel: ProductosViewModel = viewModel (
        viewModelStoreOwner = activity,
        factory = ProductosViewModel.Factory
    )

    NavHost(
        navController = navController,
        startDestination = "home"   // Pantalla inicial al abrir la app
    ) {

        // Se crea un variable viewModel para cada composable para mantener el estado de cada uno
        // durante la navegación

        // Pantalla home
        composable("home") {
            val homeViewModel : HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            PantallaPrincipal(navController, homeViewModel)
        }

        // Pantalla productos
        composable("productos") {
            val productosViewModel : ProductosViewModel = viewModel(factory = ProductosViewModel.Factory)
            PantallaProductos(navController, productosViewModel)
        }

        // Pantalla tiendas
        composable("tiendas") {
            val tiendasViewModel : TiendasViewModel = viewModel(factory = TiendasViewModel.Factory)
            PantallaTiendas(navController, tiendasViewModel)
        }

        // Pantalla tarjeta
        composable("tarjeta") {
            val tarjetaViewModel : TarjetaViewModel = viewModel(factory = TarjetaViewModel.Factory)
            PantallaTarjeta(navController, tarjetaViewModel)
        }

        //Pantalla lista
        composable("lista") {
            PantallaLista(navController, productosViewModel)
        }
    }
}
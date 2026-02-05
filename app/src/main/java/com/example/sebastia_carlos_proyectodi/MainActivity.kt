package com.example.sebastia_carlos_proyectodi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sebastia_carlos_proyectodi.ui.theme.Sebastia_carlos_proyectoDITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sebastia_carlos_proyectoDITheme {
                val navController = rememberNavController()
                CustomScaffold(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Sebastia_carlos_proyectoDITheme {
        CustomScaffold(navController = rememberNavController())
    }
}
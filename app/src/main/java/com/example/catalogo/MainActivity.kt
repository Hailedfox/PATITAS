package com.example.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad1.Layout.Registro
import com.example.catalogo.layouts.Login.Login
import com.example.catalogo.layouts.PantallaInicio.PantallaInicio
import com.example.catalogo.layouts.RegistroMascota.RegistroMascota
import com.example.catalogo.layouts.ServiciosComplejos.ServiciosComplejos
import com.example.catalogo.layouts.ServiciosRapidos.ServiciosRapidos
import com.example.catalogo.layouts.citas
import com.example.catalogo.layouts.menu.Menu
import com.example.catalogo.layouts.perfil.MenuUsuario
import com.example.catalogo.ui.theme.CatalogoTheme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.catalogo.layouts.Citas.citas2
import com.example.catalogo.layouts.Desplegable.MisCitas.MisCitas
import androidx.lifecycle.viewmodel.compose.viewModel // Import necesario
import com.example.catalogo.layouts.Citas.CitaViewModel // Import necesario
import com.example.catalogo.layouts.Desplegable.Expediente.expediente
import com.example.catalogo.layouts.Desplegable.Nosotros.nosotros
import com.example.catalogo.layouts.RegistroMascota.AnadirMascotaDesdeMenu

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        val navController = rememberNavController()
                        val citaViewModel: CitaViewModel = viewModel()

                        NavHost(navController = navController, startDestination = "Inicio") {

                            composable("Inicio") {
                                PantallaInicio(navController = navController)
                            }

                            composable("Login") {
                                Login(navController = navController)
                            }

                            composable("registro") {
                                Registro(navController = navController)
                            }

                            composable("Menu"){
                                Menu(navController = navController)
                            }

                            composable("MenuUsuario"){
                                MenuUsuario(navController = navController)
                            }

                            composable("RegistroMascota/{idCliente}") { backStackEntry ->
                                val idCliente = backStackEntry.arguments?.getString("idCliente")?.toIntOrNull()
                                RegistroMascota(navController = navController, idCliente = idCliente)
                            }

                            composable(route = "ServiciosRapidos"){
                                ServiciosRapidos(navController = navController, citaViewModel = citaViewModel)
                            }

                            composable(route = "ServiciosComplejos"){
                                ServiciosComplejos(navController = navController, citaViewModel = citaViewModel)
                            }

                            composable(
                                route = "Citas/{tipoServicio}?mascotaNombre={mascotaNombre}&servicioNombre={servicioNombre}",
                                arguments = listOf(
                                    navArgument("mascotaNombre") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("servicioNombre") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) { backStackEntry ->
                                citas(
                                    tipoServicio = backStackEntry.arguments?.getString("tipoServicio") ?: "",
                                    mascotaNombre = backStackEntry.arguments?.getString("mascotaNombre") ?: "",
                                    servicioNombre = backStackEntry.arguments?.getString("servicioNombre") ?: "",
                                    viewModel = citaViewModel,
                                    navController = navController
                                )
                            }

                            composable(route = "Mis citas"){
                                MisCitas(navController = navController)
                            }

                            composable(route = "citas2"){
                                citas2(navController = navController, viewModel = citaViewModel)
                            }

                            composable(route = "nosotros"){
                                nosotros(navController = navController)
                            }

                            composable(route = "Expediente"){
                                expediente(navController = navController)
                            }

                            composable(route= "AñadirMascota"){
                                AnadirMascotaDesdeMenu(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
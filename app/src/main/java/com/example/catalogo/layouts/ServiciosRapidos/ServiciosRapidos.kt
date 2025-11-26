package com.example.catalogo.layouts.ServiciosRapidos

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogo.R
import com.example.catalogo.layouts.menu.DrawerContent
import com.example.catalogo.layouts.Citas.CitaViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.catalogo.layouts.perfil.getUriFromPrefs
import com.example.catalogo.data.BDMascota.MascotaListaViewModel
import com.example.catalogo.data.supabase.models.MascotaDto   // ⚠ Necesario para lista

val montserratAlternatesFamily: FontFamily = FontFamily.Default
val primaryColor = Color(0xFFD06A5B)
val colorFondoContenido = Color(0xFFEEEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosRapidos(
    navController: NavController,
    citaViewModel: CitaViewModel = viewModel(),
    mascotaVM: MascotaListaViewModel = viewModel()  // 👈 cargamos mascotas desde Supabase
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // ------------------ 🔥 Cargar mascotas reales al abrir pantalla ------------------
    LaunchedEffect(Unit) { mascotaVM.cargarMascotas() }
    val mascotas by mascotaVM.mascotas.collectAsState()
    // -------------------------------------------------------------------------------

    // FOTO PERFIL
    val context = LocalContext.current
    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerState, scope, navController, profilePhotoUri) },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                // ======================= 🟥 Tu encabezado ORIGINAL intacto 🟥 =======================

                val CurvedBottomShape = GenericShape { size, _ ->
                    val width = size.width
                    val height = size.height
                    moveTo(0f, 0f)
                    lineTo(0f, height * 0.65f)
                    quadraticBezierTo(width / 2, height, width, height * 0.65f)
                    lineTo(width, 0f)
                    close()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(CurvedBottomShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezado2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, Color.Black, CurvedBottomShape)
                            .graphicsLayer { translationY = -100f },
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 70.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.menu),
                            contentDescription = "Menú",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }
                        )
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { navController.navigate("MenuUsuario") },
                            contentAlignment = Alignment.Center
                        ) {

                            if (profilePhotoUri != null) {
                                AsyncImage(
                                    model = profilePhotoUri,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.usuario),
                                    contentDescription = "Perfil",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }

                // ------------------------------ Contenedor blanco original -------------------------------

                Surface(
                    modifier = Modifier
                        .padding(top = 170.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = primaryColor,
                    shadowElevation = 4.dp,
                ) {
                    Text(
                        text = "Servicios Rápidos",
                        fontFamily = montserratAlternatesFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 240.dp, bottom = 80.dp, start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(colorFondoContenido)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ServicioItem("Consultas", mascotas, navController, citaViewModel)
                    ServicioItem("Desparasitación", mascotas, navController, citaViewModel)
                    ServicioItem("Vacunación", mascotas, navController, citaViewModel)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_negro),
                        contentDescription = "Logo",
                        modifier = Modifier.height(42.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioItem(
    titulo: String,
    mascotas: List<MascotaDto>, // 🟢 ahora con datos reales desde Supabase
    navController: NavController,
    citaViewModel: CitaViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(10.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                placeholder = { Text("Selecciona una mascota") },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(0.9f),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                mascotas.forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m.nombre) },
                        onClick = {
                            selectedOption = m.nombre
                            expanded = false

                            citaViewModel.iniciarAsignacion(
                                mascota = m.nombre,
                                servicio = titulo
                            )

                            navController.navigate("Citas/rapido?mascotaNombre=${m.nombre}&servicioNombre=$titulo")
                        }
                    )
                }
            }
        }
    }
}

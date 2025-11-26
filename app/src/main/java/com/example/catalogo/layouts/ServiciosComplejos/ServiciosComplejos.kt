package com.example.catalogo.layouts.ServiciosComplejos

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.catalogo.layouts.perfil.getUriFromPrefs
import com.example.catalogo.layouts.Citas.CitaViewModel
import com.example.catalogo.R
import com.example.catalogo.layouts.menu.DrawerContent

import com.example.catalogo.data.BDMascota.MascotaListaViewModel
import com.example.catalogo.data.supabase.models.MascotaDto

val primaryColor = Color(0xFFD06A5B)
val colorFondoContenido = Color(0xFFEEEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosComplejos(
    navController: NavController,
    citaViewModel: CitaViewModel = viewModel(),
    mascotaVM: MascotaListaViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // 🔥 Cargar mascotas desde supabase igual que en ServiciosRapidos
    LaunchedEffect(Unit) { mascotaVM.cargarMascotas() }
    val mascotas by mascotaVM.mascotas.collectAsState()

    val context = LocalContext.current
    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(drawerState, scope, navController, profilePhotoUri)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            val CurvedBottomShape = GenericShape { size, _ ->
                moveTo(0f, 0f)
                lineTo(0f, size.height * 0.65f)
                quadraticBezierTo(size.width / 2, size.height, size.width, size.height * 0.65f)
                lineTo(size.width, 0f)
                close()
            }

            //================== ENCABEZADO ORIGINAL ==================
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
                        contentDescription = "menu",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
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
                        if (profilePhotoUri != null)
                            AsyncImage(
                                model = profilePhotoUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        else
                            Image(
                                painterResource(R.drawable.usuario),
                                null,
                                Modifier.size(28.dp)
                            )
                    }
                }
            }

            //================= TITULO =====================
            Surface(
                modifier = Modifier
                    .padding(top = 170.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(20.dp),
                color = primaryColor,
                shadowElevation = 7.dp
            ) {
                Text(
                    "Servicios Complejos",
                    fontSize = 25.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }

            //=================== CONTENIDO ======================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 240.dp, start = 20.dp, end = 20.dp, bottom = 80.dp)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(colorFondoContenido)
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ServicioComplejoItem(R.drawable.cirugias, "Cirugías", mascotas, navController, citaViewModel)
                ServicioComplejoItem(R.drawable.estetica, "Estética", mascotas, navController, citaViewModel)
                ServicioComplejoItem(R.drawable.pension, "Pensión", mascotas, navController, citaViewModel)
            }

            //=================== LOGO ======================
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            ) {
                Image(
                    painterResource(R.drawable.logo_negro),
                    null,
                    modifier = Modifier.height(42.dp)
                )
            }
        }
    }
}


//=======================================================
//   ITEM FINAL  — IGUAL A SERVICIOS RÁPIDOS PERO COMPLEJO
//=======================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioComplejoItem(
    imageResId: Int,
    titulo: String,
    mascotas: List<MascotaDto>,
    navController: NavController,
    citaViewModel: CitaViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(imageResId),
            null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(180.dp)
                .clip(RoundedCornerShape(500.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(500.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(8.dp))
        Text(titulo, fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecciona tu mascota") },
                modifier = Modifier.menuAnchor().fillMaxWidth(0.85f),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                shape = RoundedCornerShape(50.dp)
            )

            ExposedDropdownMenu(expanded, { expanded = false }) {
                mascotas.forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m.nombre) },
                        onClick = {
                            selected = m.nombre
                            expanded = false
                            citaViewModel.iniciarAsignacion(m.nombre, titulo)
                            navController.navigate("Citas/complejo?mascotaNombre=${m.nombre}&servicioNombre=$titulo")
                        }
                    )
                }
            }
        }
    }
}

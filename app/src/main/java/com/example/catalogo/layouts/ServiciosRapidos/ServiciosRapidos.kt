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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.catalogo.layouts.Login.LoginViewModel
import com.example.catalogo.layouts.perfil.getUriFromPrefs

val montserratAlternatesFamily: FontFamily = FontFamily.Default
val primaryColor = Color(0xFFD06A5B)
val colorFondoContenido = Color(0xFFEEEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosRapidos(
    navController: NavController,
    citaViewModel: CitaViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // foto de perfil
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
                            .graphicsLayer {
                                translationY = -100f
                            },
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
                                .clickable { navController.navigate("MenuUsuario")},
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
                        fontFamily = com.example.catalogo.ui.theme.montserratAlternatesFamily,
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

                    ServicioItem(R.drawable.consultas, "Consultas", "Selecciona tu mascota", navController, citaViewModel, tipoServicio = "rapido")
                    ServicioItem(R.drawable.desparasitacion, "Desparasitación", "Selecciona tu mascota", navController, citaViewModel, tipoServicio = "rapido")
                    ServicioItem(R.drawable.vacunas, "Vacunación", "Selecciona tu mascota", navController, citaViewModel, tipoServicio = "rapido")
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
    imageResId: Int,
    titulo: String,
    hintText: String,
    navController: NavController,
    citaViewModel: CitaViewModel,
    tipoServicio: String = "rapido"
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    val OvalShape = RoundedCornerShape(500.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(180.dp)
                .clip(OvalShape)
                .border(width = 2.dp, color = Color.Black, shape = OvalShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = titulo,
            fontFamily = montserratAlternatesFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(hintText, color = Color.Gray) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(0.9f),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(50.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Mascota 1", "Mascota 2", "Mascota 3").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false

                            citaViewModel.iniciarAsignacion(
                                mascota = selectedOption,
                                servicio = titulo
                            )

                            // *** CORRECCIÓN CRÍTICA DE NAVEGACIÓN ***
                            val mascota = selectedOption
                            val servicio = titulo
                            navController.navigate("Citas/$tipoServicio?mascotaNombre=$mascota&servicioNombre=$servicio")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    profilePhotoUri: Uri?
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.65f),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
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
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Usuario",
                fontSize = 20.sp,
                fontFamily = montserratAlternatesFamily,
                color = Color.Black
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            DrawerItem("Mis citas", R.drawable.citas, Color.Black) {
                scope.launch {
                    drawerState.close()
                    navController.navigate("Mis citas")
                }
            }
            DrawerItem("Hoja clínica veterinaria", R.drawable.expediente, Color.Black) {
                scope.launch {
                    drawerState.close()
                navController.navigate("Expediente")
                }
            }
            DrawerItem("Nosotros", R.drawable.informacion, Color.Black) {
                scope.launch {
                    drawerState.close()
                    navController.navigate("nosotros")
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    iconResId: Int,
    color: Color,
    onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = montserratAlternatesFamily,
            color = color
        )
    }
}
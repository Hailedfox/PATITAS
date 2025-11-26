package com.example.catalogo.layouts.menu

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import com.example.catalogo.layouts.ServiciosRapidos.montserratAlternatesFamily
import com.example.catalogo.layouts.perfil.getUriFromPrefs // Importación clave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val montserratAlternatesFamily: FontFamily = FontFamily.Default
val primaryColor = Color(0xFFD06A5B)

@Composable
fun Menu(navController: NavController) {
    val colorFondoContenido = Color(0xFFEEEEEE)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    quadraticBezierTo(
                        width / 2, height,
                        width, height * 0.65f
                    )
                    lineTo(width, 0f)
                    close()
                }

                // Encabezado con curva e imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(CurvedBottomShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezado1),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, Color.Black, CurvedBottomShape)
                            .graphicsLayer {
                                scaleX = 3f
                                scaleY = 3f
                                translationY = 310f
                            },
                        contentScale = ContentScale.Fit
                    )

                    // Iconos arriba
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
                                .clickable { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
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
                        text = "Bienvenido",
                        fontFamily = montserratAlternatesFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // Contenido principal
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 240.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                        .background(
                            colorFondoContenido,
                            RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                        )
                        .padding(horizontal = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, bottom = 16.dp)
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            MenuButton(
                                title = "Servicios Rápidos",
                                subtitle = "5 / 10 minutos",
                                imageResId = R.drawable.consultas,
                                onClick = {navController.navigate("ServiciosRapidos") }
                            )

                            MenuButton(
                                title = "Servicios complejos",
                                subtitle = "30 / 60 minutos",
                                imageResId = R.drawable.estetica,
                                onClick = { navController.navigate("ServiciosComplejos")}
                            )
                        }

                        // Logo inferior fijo
                        Image(
                            painter = painterResource(R.drawable.logo_negro),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(42.dp)
                                .padding(bottom = 10.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    )
}


//Componente de botones
@Composable
fun MenuButton(
    title: String,
    subtitle: String,
    imageResId: Int,
    onClick: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 150)
    )

    val OvalShape = RoundedCornerShape(500.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { isPressed = true },
                    onTap = {
                        isPressed = false
                        onClick()
                    },
                    onLongPress = { isPressed = false }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Título y subtítulo
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            fontFamily = montserratAlternatesFamily
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            fontFamily = montserratAlternatesFamily
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen ovalada sin fondo detrás
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(180.dp)
                .clip(OvalShape)
                .border(width = 2.dp, color = Color.Black, shape = OvalShape)
        ) {
            Image(
                painter = painterResource(imageResId),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


// Barra lateral
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

        Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 1.dp, color = Color.LightGray)

        // Opciones del Menú
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            DrawerItem("Mis citas", R.drawable.citas, Color.Black) {
                scope.launch {
                    drawerState.close()
                    navController.navigate("Mis citas")
                }
            }
            DrawerItem("Hoja clinica veterinaria", R.drawable.expediente, Color.Black) {
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

//Componente para cada ítem de la barra
@Composable
fun DrawerItem(title: String, iconResId: Int, color: Color, onClick: () -> Unit) {
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
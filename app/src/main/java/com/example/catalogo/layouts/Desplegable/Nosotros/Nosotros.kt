package com.example.catalogo.layouts.Desplegable.Nosotros

import android.content.Intent
import android.net.Uri
import android.content.Context
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import com.example.catalogo.layouts.Desplegable.MisCitas.FOOTER_HEIGHT
import com.example.catalogo.layouts.menu.primaryColor
import com.example.catalogo.layouts.perfil.getUriFromPrefs

val montserratAlternatesFamily: FontFamily = FontFamily.Default
val secondaryCardColor = Color(0xFFF0F0F0)

@Composable
fun nosotros(navController: NavController) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(CurvedBottomShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.encabezado4),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black, CurvedBottomShape)
                    .graphicsLayer {
                        scaleX = 1.24f
                        scaleY = 1.24f
                        translationY = -44f
                    },
                contentScale = ContentScale.Fit
            )

            // Iconos de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.atras),
                    contentDescription = "Menú",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
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

        // Título Fijo
        Surface(
            modifier = Modifier
                .padding(top = 170.dp)
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(20.dp),
            color = primaryColor,
            shadowElevation = 4.dp,
        ) {
            Text(
                text = "¡Conócenos!",
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
                .padding(top = 230.dp)
                .verticalScroll(scrollState)
                .padding(
                    top = 20.dp,
                    bottom = FOOTER_HEIGHT + 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Quiénes Somos (Fondo Blanco)
            ExpandableInfoCard(
                title = "Quiénes Somos",
                summary = "Somos un equipo apasionado por el cuidado de tus mascotas, con experiencia y tecnología al alcance.",
                fullContent = {
                    Text(
                        "En VetApp, creemos que cada mascota merece el mejor trato posible. Nuestro equipo está formado por veterinarios y amantes de los animales dedicados a ofrecer el mejor diagnóstico y tratamiento. ¡Tu mascota es parte de nuestra familia!",
                        fontFamily = montserratAlternatesFamily
                    )
                },
            )

            // Misión y Valores (Fondo Gris Claro)
            ExpandableInfoCard(
                title = "Misión y Valores",
                summary = "Conoce nuestra meta principal y los principios que guían nuestro trabajo.",
                fullContent = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Misión:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = primaryColor
                        )
                        Text("Ofrecer soluciones veterinarias integrales, asegurando el bienestar y la calidad de vida de las mascotas.")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Valores:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = primaryColor
                        )
                        Text("• Amor Animal\n• Profesionalismo\n• Confianza\n• Innovación")
                    }
                }
            )

            // Contáctanos (Fondo Blanco)
            ExpandableInfoCard(
                title = "Contáctanos",
                summary = "Teléfonos, correo y dirección para emergencias y citas.",
                fullContent = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "📞 Teléfono Citas: +52 33 1778 0263",
                            fontFamily = montserratAlternatesFamily
                        )
                        Text("📧 Email: Patitas@gmail.com", fontFamily = montserratAlternatesFamily)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "📍 Dirección: Av. Colon 653A zapopan cetro, Zapopan, 45140 Guadalajara, Jal.",
                            fontFamily = montserratAlternatesFamily
                        )
                    }
                },
                backgroundColor = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Encuéntranos",
                fontFamily = montserratAlternatesFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(180.dp)
                    .clickable { LaunchMapIntent(context) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.mapa),
                        contentDescription = "Abrir ubicación en Google Maps",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Text(
                        text = "Toca para llegar con nosotros",
                        fontFamily = montserratAlternatesFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(FOOTER_HEIGHT)
                .background(Color.White)
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
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

fun LaunchMapIntent(context: Context) {
    val address = "Colon 653A zapopan centro Zapopan, 45140 Guadalajara, Jal."

    val gmmIntentUri = Uri.Builder()
        .scheme("geo")
        .path("0,0")
        .appendQueryParameter("q", address)
        .build()

    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(mapIntent)
    } catch (e: Exception) {
        val webIntentUri = Uri.Builder()
            .scheme("https")
            .authority("www.google.com")
            .appendPath("maps")
            .appendQueryParameter("q", address)
            .build()
        val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)
    }
}


@Composable
fun ExpandableInfoCard(
    title: String,
    summary: String,
    fullContent: @Composable () -> Unit,
    backgroundColor: Color = Color.White
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor,
                    fontFamily = montserratAlternatesFamily
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Colapsar" else "Expandir",
                    tint = primaryColor,
                    modifier = Modifier
                        .size(26.dp)
                        .graphicsLayer { rotationZ = if (expanded) 180f else 0f }
                )
            }
            if (!expanded) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = summary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = montserratAlternatesFamily
                )
            }

            if (expanded) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                fullContent()
            }
        }
    }
}
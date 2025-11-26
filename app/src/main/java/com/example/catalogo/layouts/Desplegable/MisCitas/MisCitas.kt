package com.example.catalogo.layouts.Desplegable.MisCitas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import com.example.catalogo.layouts.perfil.getUriFromPrefs
import com.example.catalogo.ui.theme.montserratAlternatesFamily

data class Cita(
    val id: Int,
    val date: String,
    val month: String,
    val hour: String,
    val service: String,
    val petName: String,
    val isPending: Boolean
)

// Altura total del footer fijo
val FOOTER_HEIGHT = 80.dp

@Composable
fun MisCitas(navController: NavController){
    val primaryColor = Color(0xFFD06A5B)
    val accentColor = Color(0xFF16A085)
    val azulFecha = Color(0xFF5DADE2)

    // foto de perfil
    val context = LocalContext.current
    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    var isPendingSelected by remember { mutableStateOf(true) }

    val allCitas = remember {
        listOf(
            Cita(1, "15", "OCT", "10:00 AM", "Baño y Corte", "Max", true),
            Cita(2, "16", "NOV", "03:30 PM", "Consulta Veterinaria", "Luna", true),
            Cita(3, "20", "NOV", "05:00 PM", "Consulta Veterinaria", "Kira", true),
            Cita(4, "21", "NOV", "05:30 PM", "Vacunación", "Kira", true),
            Cita(5, "22", "NOV", "06:00 PM", "Pensión", "Bongo", true),
            Cita(6, "25", "NOV", "05:00 PM", "Pensión", "Dobby", true),
            Cita(7, "05", "SEP", "11:00 AM", "Vacunación", "Toby", false),
            Cita(8, "28", "AUG", "09:00 AM", "Peluquería", "Rocky", false)
        )
    }

    val pendingCitas = allCitas.filter { it.isPending }
    val pastCitas = allCitas.filter { !it.isPending }

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

        // Encabezado con curva (Contenido superior)
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

        // Columna principal (Contenido scrollable)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                modifier = Modifier
                    .padding(top = 170.dp),
                shape = RoundedCornerShape(20.dp),
                color = primaryColor,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Mis citas",
                    fontFamily = montserratAlternatesFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(25.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TabButton(
                    text = "Próximas",
                    isSelected = isPendingSelected,
                    onClick = { isPendingSelected = true },
                    selectedColor = primaryColor
                )
                TabButton(
                    text = "Pasadas",
                    isSelected = !isPendingSelected,
                    onClick = { isPendingSelected = false },
                    selectedColor = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LazyColumn (Contenido con scroll)
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                // Ajustamos el relleno inferior a la altura del footer para que el scroll se detenga antes de él.
                contentPadding = PaddingValues(bottom = FOOTER_HEIGHT)
            ) {
                val listToShow = if (isPendingSelected) pendingCitas else pastCitas
                items(listToShow) { cita ->
                    CitaCard(
                        cita = cita,
                        dateColor = if (cita.isPending) azulFecha else Color.Gray,
                        accentColor = accentColor
                    )
                }
            }
        }

        // --- FOOTER FIJO CON FONDO BLANCO ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(FOOTER_HEIGHT)
                .background(Color.White),
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

// ... (Resto de funciones TabButton y CitaCard sin cambios)
@Composable
fun RowScope.TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedColor else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .weight(1f)
            .height(50.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun CitaCard(cita: Cita, dateColor: Color, accentColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Acción al seleccionar la cita */ },
        shape = RoundedCornerShape(25.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(33.dp))
                    .background(dateColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = cita.date,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = cita.month,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.size(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Cita de: ${cita.service}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Text(
                    text = "Para: ${cita.petName}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Hora: ${cita.hour}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (cita.isPending) {
                    Button(
                        onClick = { /* Acción para marcar como finalizada */ },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(35.dp)
                    ) {
                        Text(text = "Marcar como Finalizada", fontSize = 12.sp)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Completada",
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.background(accentColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { /* Acción de reagendar */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(35.dp)
                        ) {
                            Text(text = "Reagendar", fontSize = 12.sp, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
package com.example.catalogo.layouts.Desplegable.Expediente

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import com.example.catalogo.domain.Entity.ExpedienteEntity
import com.example.catalogo.domain.Entity.datosExpedientesFicticios
import com.example.catalogo.layouts.Desplegable.MisCitas.FOOTER_HEIGHT
import com.example.catalogo.layouts.menu.primaryColor
import com.example.catalogo.layouts.perfil.getUriFromPrefs


@Composable
fun ExpandableInfoCard(
    title: String,
    summary: String,
    fullContent: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .animateContentSize(animationSpec = tween(300))
    ) {
        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Text(text = summary, color = Color.Gray)
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Solo renderiza fullContent si está expandido
            if (isExpanded) {
                fullContent()
            }
        }
    }
}

val appFontFamily = FontFamily.Default

@Composable
fun ExpedienteCampoBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0F0F0))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = appFontFamily,
            )
            Text(
                text = value,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = appFontFamily,
            )
        }
    }
}

@Composable
fun ExpedienteDetalleCompleto(expediente: ExpedienteEntity, isExpandable: Boolean = false) {
    val horizontalPadding = if (isExpandable) 0.dp else 16.dp

    Column(
        modifier = Modifier.padding(horizontal = horizontalPadding)
    ) {
        Text("Detalles de la Visita", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp, top = 8.dp))
        ExpedienteCampoBox(label = "Peso Actual:", value = expediente.pesoActual)
        ExpedienteCampoBox(label = "Temperatura Última Visita:", value = expediente.temperaturaVisita)
        ExpedienteCampoBox(label = "Frecuencia Cardíaca:", value = expediente.frecuenciaCardiaca)
        ExpedienteCampoBox(label = "Frecuencia Respiratoria:", value = expediente.frecuenciaRespiratoria)
        ExpedienteCampoBox(label = "Condición Corporal:", value = expediente.condicionCorporal)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Esquema de Vacunación", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        ExpedienteCampoBox(label = "Esquema Vacuna:", value = expediente.esquemaVacuna)
        ExpedienteCampoBox(label = "Próxima Vacuna:", value = expediente.proximaVacuna)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Esquema de Desparasitación", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        ExpedienteCampoBox(label = "Ultima Desparasitación:", value = expediente.ultimaDesparasitacion)
        ExpedienteCampoBox(label = "Próxima Desparasitación:", value = expediente.proximaDesparasitacion)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Información Adicional", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        ExpedienteCampoBox(label = "Alergias:", value = expediente.alergias)
        ExpedienteCampoBox(label = "Cirugías Previas:", value = expediente.cirugiasPrevias)
        ExpedienteCampoBox(label = "Estado Reproductivo:", value = expediente.estadoReproductivo)
        ExpedienteCampoBox(label = "Fecha de Esterilización:", value = expediente.fechaEsterilizacion)
        ExpedienteCampoBox(label = "Camadas Previas:", value = expediente.camadasPrevias)
        ExpedienteCampoBox(label = "Temperamento:", value = expediente.temperamento)
        ExpedienteCampoBox(label = "Reacción al Manejo:", value = expediente.reaccionManejo)
        ExpedienteCampoBox(label = "Comportamiento Clínico:", value = expediente.comportamientoClinico)

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun expediente (navController: NavController){
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    val expedientes = datosExpedientesFicticios

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
                text = "Expedientes",
                fontFamily = appFontFamily,
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
            when (expedientes.size) {
                1 -> {
                    ExpedienteDetalleCompleto(expedientes.first())
                }
                0 -> {
                    Text("No se encontraron expedientes de mascotas.", modifier = Modifier.padding(32.dp))
                }
                else -> {
                    expedientes.forEach { expediente ->
                        ExpandableInfoCard(
                            title = "Mascota: ${expediente.nombreMascota}",
                            summary = "Expediente # ${expediente.id}",
                            fullContent = {
                                ExpedienteDetalleCompleto(expediente, isExpandable = true)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
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
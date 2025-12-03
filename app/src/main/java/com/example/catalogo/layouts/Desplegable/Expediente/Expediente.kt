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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import com.example.catalogo.domain.Entity.ExpedienteEntity
import com.example.catalogo.domain.UserSession
import com.example.catalogo.layouts.Desplegable.MisCitas.FOOTER_HEIGHT
import com.example.catalogo.layouts.menu.primaryColor
import com.example.catalogo.layouts.perfil.getUriFromPrefs


// ---------------- COMPONENTES ----------------

val appFontFamily = FontFamily.Default

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
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (isExpanded)
                fullContent()
        }
    }
}

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
                fontFamily = appFontFamily
            )
            Text(
                text = value,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = appFontFamily
            )
        }
    }
}

@Composable
fun ExpedienteDetalleCompleto(exp: ExpedienteEntity) {

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Detalles de la Visita", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        ExpedienteCampoBox("Peso Actual:", exp.pesoActual)
        ExpedienteCampoBox("Temperatura Última Visita:", exp.temperaturaVisita)
        ExpedienteCampoBox("Frecuencia Cardíaca:", exp.frecuenciaCardiaca)
        ExpedienteCampoBox("Frecuencia Respiratoria:", exp.frecuenciaRespiratoria)
        ExpedienteCampoBox("Condición Corporal:", exp.condicionCorporal)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Esquema de Vacunación", fontWeight = FontWeight.Bold)
        ExpedienteCampoBox("Esquema Vacuna:", exp.esquemaVacuna)
        ExpedienteCampoBox("Próxima Vacuna:", exp.proximaVacuna)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Esquema de Desparasitación", fontWeight = FontWeight.Bold)
        ExpedienteCampoBox("Última Desparasitación:", exp.ultimaDesparasitacion)
        ExpedienteCampoBox("Próxima Desparasitación:", exp.proximaDesparasitacion)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Información Adicional", fontWeight = FontWeight.Bold)
        ExpedienteCampoBox("Alergias:", exp.alergias)
        ExpedienteCampoBox("Cirugías Previas:", exp.cirugiasPrevias)
        ExpedienteCampoBox("Estado Reproductivo:", exp.estadoReproductivo)
        ExpedienteCampoBox("Fecha Esterilización:", exp.fechaEsterilizacion)
        ExpedienteCampoBox("Camadas Previas:", exp.camadasPrevias)
        ExpedienteCampoBox("Temperamento:", exp.temperamento)
        ExpedienteCampoBox("Reacción al Manejo:", exp.reaccionManejo)
        ExpedienteCampoBox("Comportamiento Clínico:", exp.comportamientoClinico)

        Spacer(modifier = Modifier.height(30.dp))
    }
}


// ---------------- PANTALLA COMPLETA ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun expediente(
    navController: NavController,
    viewModel: ExpedienteViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    val idCliente = UserSession.currentUser?.id ?: 0

    val mascotas by viewModel.mascotas.collectAsState()
    val mascotaSeleccionada by viewModel.mascotaSeleccionada.collectAsState()
    val expedienteState by viewModel.expediente.collectAsState()

    LaunchedEffect(idCliente) {
        if (idCliente != 0) viewModel.cargarMascotas(idCliente)
    }

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Encabezado -------------------------------------------------

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
        }

        // Título -------------------------------------------------

        Surface(
            modifier = Modifier
                .padding(top = 170.dp)
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(20.dp),
            color = primaryColor
        ) {
            Text(
                "Expedientes",
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        // Contenido -------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 230.dp)
                .verticalScroll(scrollState)
                .padding(bottom = FOOTER_HEIGHT + 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Selecciona una mascota",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = mascotaSeleccionada?.nombre ?: "Elige una mascota",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mascota") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    mascotas.forEach { mascota ->
                        DropdownMenuItem(
                            text = { Text(mascota.nombre) },
                            onClick = {
                                expanded = false
                                viewModel.seleccionarMascota(mascota, idCliente)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                mascotaSeleccionada == null -> Text("Selecciona una mascota para ver su expediente.")
                expedienteState == null -> Text("Cargando expediente...")
                else -> ExpandableInfoCard(
                    title = "Mascota: ${expedienteState!!.nombreMascota}",
                    summary = "Expediente #${expedienteState!!.id}"
                ) {
                    ExpedienteDetalleCompleto(expedienteState!!)
                }
            }
        }

        // Footer -------------------------------------------------

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
                modifier = Modifier.height(42.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

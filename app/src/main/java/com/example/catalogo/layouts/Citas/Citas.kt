package com.example.catalogo.layouts

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.catalogo.R
import com.example.catalogo.layouts.Citas.CitaViewModel
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


val montserratAlternatesFamily: FontFamily = FontFamily.Default
val primaryColor = Color(0xFFD06A5B)
val primaryColorMenu = Color(0xFFD06A5B)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun citas(
    tipoServicio: String,
    mascotaNombre: String = "",
    servicioNombre: String = "",
    viewModel: CitaViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)


    var expanded by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val tipoServicioLimpio = tipoServicio.trim().lowercase()

    val horariosDisponibles = if (tipoServicioLimpio == "rapido") {
        listOf("8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM")
    } else {
        listOf("2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM")
    }


    LaunchedEffect(mascotaNombre, servicioNombre) {

        if (viewModel.servicioEnProceso == null && mascotaNombre.isNotBlank() && servicioNombre.isNotBlank()) {
            viewModel.iniciarAsignacion(mascotaNombre, servicioNombre)
        }

        if (viewModel.horarioSeleccionadoTemp.isBlank()) {
            viewModel.horarioSeleccionadoTemp = horariosDisponibles.firstOrNull() ?: ""
        }
    }


    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            viewModel.fechaSeleccionadaTemp = selectedCalendar.time
            mensajeError = null
        },
        year, month, day
    ).apply {
        datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 6)
        datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, -6)
    }


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
                painter = painterResource(id = R.drawable.encabezado3),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black, CurvedBottomShape)
                    .graphicsLayer {
                        scaleX = 1.24f
                        scaleY = 1.24f
                        translationY = -50f
                    },
                contentScale = ContentScale.Fit
            )
        }

        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically )
        {
            Image(
                painter = painterResource(R.drawable.atras),
                contentDescription = "Regresar",
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 170.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(20.dp),
                color = primaryColorMenu,
                shadowElevation = 4.dp,
            ) {

                val currentService = viewModel.servicioEnProceso?.servicioNombre ?: servicioNombre
                Text(
                    text = "Fecha y Horario (${currentService})",
                    fontFamily = montserratAlternatesFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Fecha
            Text("Selecciona una fecha para tu cita", fontFamily = montserratAlternatesFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier.height(55.dp).width(250.dp)
            ) {
                Text("Abrir calendario", color = Color.White, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            viewModel.fechaSeleccionadaTemp?.let {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                Text(
                    text = "Fecha seleccionada: ${dateFormat.format(it)}",
                    fontFamily = montserratAlternatesFamily,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Horario
            Text("Selecciona un horario de atención", fontFamily = montserratAlternatesFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                TextField(
                    value = viewModel.horarioSeleccionadoTemp,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Horario") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = primaryColor, unfocusedIndicatorColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    horariosDisponibles.forEach { horario ->
                        DropdownMenuItem(
                            text = { Text(horario) },
                            onClick = {
                                viewModel.horarioSeleccionadoTemp = horario
                                expanded = false
                                mensajeError = null
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            mensajeError?.let {
                Text(it, color = Color.Red, fontFamily = montserratAlternatesFamily, modifier = Modifier.padding(bottom = 10.dp), textAlign = TextAlign.Center)
            }

            // Botones
            Button(
                onClick = {
                    if (viewModel.servicioEnProceso != null && viewModel.tieneDatosTemporales()) {
                        viewModel.confirmarHorarioYAnadirALista()
                        navController.navigate("Menu") {
                            popUpTo("Menu") { inclusive = true }
                        }
                    } else {
                        mensajeError = "Debes seleccionar una fecha y un horario para agendar más servicios."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier.height(55.dp).width(250.dp).padding(bottom = 10.dp)
            ) {
                Text("Agendar Más Servicios", color = Color.White, fontSize = 18.sp)
            }


            Button(
                onClick = {
                    val datosTemporalesListos = viewModel.servicioEnProceso != null && viewModel.tieneDatosTemporales()
                    val serviciosPrevios = viewModel.serviciosAgendados.isNotEmpty()

                    if (datosTemporalesListos) {
                        viewModel.confirmarHorarioYAnadirALista()
                        navController.navigate("citas2")
                    } else if (serviciosPrevios) {
                        navController.navigate("citas2")
                    } else {
                        mensajeError = "Debes seleccionar al menos un servicio con fecha y hora para finalizar."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.height(55.dp).width(250.dp)
            ) {
                Text("Terminar de Agendar", color = Color.White, fontSize = 18.sp)
            }
        }

        // Logo inferior (sin cambios)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_negro),
                contentDescription = "Logo",
                modifier = Modifier.height(42.dp).fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit
            )
        }
    }
}
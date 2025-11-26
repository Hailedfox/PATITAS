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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var expanded by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val horariosDisponibles = if (tipoServicio.lowercase().trim() == "rapido") {
        listOf("8:00 AM","8:30 AM","9:00 AM","9:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM",
            "12:00 PM","12:30 PM","1:00 PM")
    } else {
        listOf("2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM")
    }

    // ==== Mantiene mascota / servicio seleccionado
    LaunchedEffect(Unit) {
        if (viewModel.servicioEnProceso == null && mascotaNombre.isNotBlank())
            viewModel.iniciarAsignacion(mascotaNombre, servicioNombre)

        if (viewModel.horarioSeleccionadoTemp.isBlank())
            viewModel.horarioSeleccionadoTemp = horariosDisponibles.first()
    }

    // ==== DatePicker
    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            viewModel.fechaSeleccionadaTemp = Calendar.getInstance().apply { set(y,m,d) }.time
            mensajeError = null
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply { datePicker.minDate = System.currentTimeMillis() }


    // UI
    Box(Modifier.fillMaxSize().background(Color.White)) {

        val CurvedShape = GenericShape { s,_ ->
            moveTo(0f,0f)
            lineTo(0f,s.height*0.65f)
            quadraticBezierTo(s.width/2,s.height,s.width,s.height*0.65f)
            lineTo(s.width,0f)
        }

        // Header
        Box(Modifier.fillMaxWidth().height(230.dp).clip(CurvedShape)) {
            Image(
                painter = painterResource(id = R.drawable.encabezado3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().border(2.dp,Color.Black,CurvedShape)
            )
        }

        // Back Button
        Row(Modifier.padding(20.dp)) {
            Image(
                painter = painterResource(R.drawable.atras),
                contentDescription = "Regresar",
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Column(
            Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 170.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Surface(
                Modifier.fillMaxWidth(.9f),
                color = primaryColorMenu,
                shape = RoundedCornerShape(20.dp)
            ){
                Text(
                    "Fecha y Horario ($servicioNombre)",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(25.dp))

            // ==== FECHA
            Text("Selecciona una fecha", fontSize=17.sp, fontWeight=FontWeight.SemiBold)

            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(primaryColor),
                modifier=Modifier.height(50.dp).width(230.dp).padding(top=10.dp)
            ) { Text("Abrir Calendario", color=Color.White,fontSize=18.sp) }

            viewModel.fechaSeleccionadaTemp?.let {
                Text("📅 ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)}")
            }

            Spacer(Modifier.height(30.dp))

            // ==== HORARIO
            Text("Selecciona horario", fontSize=17.sp, fontWeight=FontWeight.SemiBold)

            ExposedDropdownMenuBox(expanded,{expanded=!expanded}) {
                TextField(
                    value = viewModel.horarioSeleccionadoTemp,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(.9f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
                )

                ExposedDropdownMenu(expanded,{expanded=false}) {
                    horariosDisponibles.forEach { h ->
                        DropdownMenuItem(
                            text = { Text(h) },
                            onClick = {
                                viewModel.horarioSeleccionadoTemp = h
                                expanded = false
                                mensajeError = null
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(35.dp))

            // ==== BOTÓN → AGENDAR MÁS
            Button(
                onClick = {
                    if(viewModel.confirmarHorarioYAnadirALista())
                        navController.navigate("Menu")
                    else mensajeError = "⚠ Selecciona fecha y horario"
                },
                modifier = Modifier.height(55.dp).width(250.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ){
                Text("Agendar más servicios", color=Color.White, fontSize=18.sp)
            }

            // ==== BOTÓN → IR A CONFIRMACIÓN
            Button(
                onClick = {
                    if(viewModel.confirmarHorarioYAnadirALista() || viewModel.serviciosAgendados.isNotEmpty())
                        navController.navigate("citas2")
                    else mensajeError="⚠ Debes registrar mínimo 1 servicio"
                },
                modifier = Modifier.height(55.dp).width(250.dp).padding(top=10.dp),
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ){
                Text("Terminar de agendar", color=Color.White, fontSize=18.sp)
            }

            mensajeError?.let { Text(it, color=Color.Red, modifier=Modifier.padding(top=10.dp)) }
        }

        Image(
            painter = painterResource(R.drawable.logo_negro),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter).padding(10.dp).height(40.dp)
        )
    }
}

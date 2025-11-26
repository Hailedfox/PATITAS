package com.example.catalogo.layouts.Citas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.catalogo.data.supabase.CitaSupabaseRepository
import com.example.catalogo.data.supabase.TempCita
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

val primaryColor = Color(0xFFD06A5B)
val primaryColorMenu = Color(0xFFD06A5B)

@Composable
fun citas2(
    navController: NavController,
    viewModel: CitaViewModel = viewModel()
) {

    val servicios = viewModel.serviciosAgendados
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val scope = rememberCoroutineScope()
    val repo = CitaSupabaseRepository()

    val CurvedBottomShape = GenericShape { size,_ ->
        moveTo(0f,0f)
        lineTo(0f,size.height*0.65f)
        quadraticBezierTo(size.width/2,size.height,size.width,size.height*0.65f)
        lineTo(size.width,0f)
    }

    Box(Modifier.fillMaxSize().background(Color.White)) {

        // Header
        Box(Modifier.fillMaxWidth().height(230.dp).clip(CurvedBottomShape)) {
            Image(
                painter = painterResource(id = R.drawable.encabezado3),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().border(2.dp, Color.Black, CurvedBottomShape),
                contentScale = ContentScale.Crop
            )
        }

        // Back
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.atras),
                contentDescription = "Regresar",
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Column(
            Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top=170.dp, start=20.dp, end=20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = primaryColorMenu,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            ){
                Text("Confirmación de Cita", fontSize = 25.sp, color = Color.White,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(25.dp))

            Text("Resumen de Servicios Agendados (${servicios.size}):",
                fontWeight = FontWeight.Bold, fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 10.dp)
            )

            LazyColumn(
                Modifier.fillMaxWidth(0.9f).heightIn(max = 200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF3F2))
                    .border(1.dp, primaryColor, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                if (servicios.isEmpty()) {
                    item { Text("No se han seleccionado servicios.", color = Color.Red,
                        modifier = Modifier.fillMaxWidth().padding(14.dp), textAlign = TextAlign.Center) }
                } else items(servicios) { s ->
                    val fecha = s.fecha?.let { dateFormat.format(it) } ?: "Sin fecha"

                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        Column(Modifier.weight(1f)) {
                            Text("Mascota: ${s.mascotaNombre} (${s.servicioNombre})", fontWeight = FontWeight.SemiBold)
                            Text("📅 $fecha    🕒 ${s.horario}")
                        }

                        IconButton(onClick = { viewModel.eliminarServicio(s) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = primaryColor)
                        }
                    }
                    Divider()
                }
            }

            Spacer(Modifier.height(20.dp))

            // Datos cliente
            OutlinedTextField(
                value = viewModel.nombreCliente,
                onValueChange = { viewModel.nombreCliente = it },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top=10.dp)
            )

            OutlinedTextField(
                value = viewModel.numeroEmergencia,
                onValueChange = { viewModel.numeroEmergencia = it },
                label = { Text("Número de emergencia") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top=10.dp)
            )

            Spacer(Modifier.height(30.dp))


            // ---- GUARDAR EN SUPABASE ----
            Button(
                onClick = {
                    scope.launch {
                        val lista = servicios.map { TempCita(
                            mascotaNombre = it.mascotaNombre,
                            servicioNombre = it.servicioNombre,
                            fecha = it.fecha!!,
                            horario = it.horario
                        ) }

                        val ok = repo.guardarCitas(
                            viewModel.nombreCliente,
                            viewModel.numeroEmergencia,
                            lista
                        )

                        if(ok){
                            viewModel.limpiarCitas()
                            navController.navigate("Menu")
                        }
                    }
                },
                enabled = servicios.isNotEmpty() &&
                        viewModel.nombreCliente.isNotBlank() &&
                        viewModel.numeroEmergencia.isNotBlank(),
                modifier = Modifier.height(55.dp).width(250.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ){
                Text("Confirmar Cita", color=Color.White, fontSize=20.sp)
            }
        }

        Image(
            painter = painterResource(R.drawable.logo_negro),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom=10.dp).height(42.dp)
        )
    }
}

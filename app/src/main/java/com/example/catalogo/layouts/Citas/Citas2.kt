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
import androidx.compose.ui.graphics.graphicsLayer
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
import java.text.SimpleDateFormat
import java.util.Locale

val montserratAlternatesFamily: FontFamily = FontFamily.Default
val primaryColor = Color(0xFFD06A5B)
val primaryColorMenu = Color(0xFFD06A5B)

@Composable
fun citas2 (
    navController: NavController,
    viewModel: CitaViewModel = viewModel()
) {

    val servicios = viewModel.serviciosAgendados
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // UI Shape para el encabezado
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
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Encabezado
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

        // Botón Atrás
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
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

        // Contenido Principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 170.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la pantalla
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = primaryColorMenu,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Confirmación de Cita",
                    fontFamily = montserratAlternatesFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Resumen de Servicios Agendados
            Text(
                text = "Resumen de Servicios Agendados (${servicios.size}):",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(max = 200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF3F2))
                    .border(1.dp, primaryColor, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                if (servicios.isEmpty()) {
                    item {
                        Text("No se han seleccionado servicios.", color = Color.Red, modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                } else {
                    items(servicios) { servicio ->
                        val dateStr =
                            servicio.fecha?.let { dateFormat.format(it) } ?: "Fecha Pendiente"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mascota: ${servicio.mascotaNombre} (${servicio.servicioNombre})",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Fecha: ${dateStr} a las ${servicio.horario}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            // BOTÓN DE ELIMINAR
                            IconButton(
                                onClick = {
                                    viewModel.eliminarServicio(servicio)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar servicio",
                                    tint = primaryColor
                                )
                            }
                        }
                        Divider(
                            color = Color.LightGray,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Campos de Datos del Cliente
            Text(
                text = "Completa tus datos:",
                fontFamily = montserratAlternatesFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 10.dp)
            )

            // Nombre del cliente
            OutlinedTextField(
                value = viewModel.nombreCliente,
                onValueChange = { viewModel.nombreCliente = it },
                label = { Text("Nombre del cliente para la cita") },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Número de emergencia
            OutlinedTextField(
                value = viewModel.numeroEmergencia,
                onValueChange = { viewModel.numeroEmergencia = it },
                label = { Text("Número de emergencia") },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // BOTÓN FINAL DE CONFIRMACIÓN
            Button(
                onClick = {
                    viewModel.guardarCitaMaestra()
                    // Aquí iría la navegación a la pantalla de éxito o inicio
                    // navController.navigate("Exito")
                },
                enabled = viewModel.nombreCliente.isNotBlank() && viewModel.numeroEmergencia.isNotBlank() && servicios.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier
                    .height(55.dp)
                    .width(250.dp)
            ) {
                Text("Confirmar Cita", color = Color.White, fontSize = 20.sp)
            }
        }

        // Logo inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_negro),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(42.dp)
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit
            )
        }
    }
}
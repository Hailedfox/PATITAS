package com.example.catalogo.layouts.RegistroMascota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.catalogo.R
import com.example.catalogo.data.BDMascota.MascotaRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroMascota(
    navController: NavController,
    idCliente: Int? = null // Recibido desde NavHost
) {

    val repository = remember { MascotaRepositoryImpl() }

    // Creamos el ViewModel usando un Factory para inyectar el Repositorio
    val viewModel: MascotaViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MascotaViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MascotaViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    // ----------------------------------------------------

    val nombre by viewModel.nombre.observeAsState("")
    val especie by viewModel.especie.observeAsState("")
    val edad by viewModel.edad.observeAsState("")
    val sexo by viewModel.sexo.observeAsState("")

    val errorNombre by viewModel.errorNombre.observeAsState()
    val errorEspecie by viewModel.errorEspecie.observeAsState()
    val errorSexo by viewModel.errorSexo.observeAsState()

    val primaryColor = Color(0xFFD06A5B)

    // Usar el ID real
    val clienteIdUsar = idCliente

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
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black, CurvedBottomShape)
                    .graphicsLayer {
                        translationX = 300f
                        scaleX = 3f
                        scaleY = 3f
                        translationY = -90f
                    },
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.atras),
                    contentDescription = "Regresar",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            navController.navigate("registro")
                        }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 135.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gato),
                        contentDescription = "Mascota",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registro de Mascota",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                // Campo: Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = viewModel::onNombreChanged,
                    label = { Text("Nombre de la Mascota") },
                    singleLine = true,
                    isError = errorNombre != null,
                    modifier = Modifier
                        .width(300.dp)
                        .padding(vertical = 6.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                if (errorNombre != null)
                    Text(errorNombre!!, color = MaterialTheme.colorScheme.error)

                // Campo: Especie
                OutlinedTextField(
                    value = especie,
                    onValueChange = viewModel::onEspecieChanged,
                    label = { Text("Especie") },
                    singleLine = true,
                    isError = errorEspecie != null,
                    modifier = Modifier
                        .width(300.dp)
                        .padding(vertical = 6.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                if (errorEspecie != null)
                    Text(errorEspecie!!, color = MaterialTheme.colorScheme.error)

                // Campo: Raza
                OutlinedTextField(
                    value = viewModel.raza.observeAsState("").value,
                    onValueChange = viewModel::onRazaChanged,
                    label = { Text("Raza (opcional)") },
                    singleLine = true,
                    modifier = Modifier
                        .width(300.dp)
                        .padding(vertical = 6.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Campo: Edad
                OutlinedTextField(
                    value = edad,
                    onValueChange = viewModel::onEdadChanged,
                    label = { Text("Edad (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(300.dp)
                        .padding(vertical = 6.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Campo: Sexo con Dropdown
                var expanded by remember { mutableStateOf(false) }
                val opcionesSexo = listOf("Macho", "Hembra")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = sexo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sexo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .width(300.dp)
                            .padding(vertical = 6.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcionesSexo.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    viewModel.onSexoChanged(opcion)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (errorSexo != null)
                    Text(errorSexo!!, color = MaterialTheme.colorScheme.error)

                Spacer(modifier = Modifier.height(50.dp))

                // BOTONES
                Button(
                    // Llama al registro pasando el ID
                    onClick = { viewModel.registrarMascota(idCliente = clienteIdUsar) },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(220.dp).height(52.dp)
                ) {
                    Text(
                        "GUARDAR Y AGREGAR OTRA MASCOTA",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(

                    onClick = {
                        viewModel.registrarMascota(idCliente = clienteIdUsar) {
                            navController.navigate("Menu")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(220.dp).height(52.dp)
                ) {
                    Text("TERMINAR REGISTRO", color = Color.White)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_negro),
                    contentDescription = "Logo Patitäs Veterinaria",
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}
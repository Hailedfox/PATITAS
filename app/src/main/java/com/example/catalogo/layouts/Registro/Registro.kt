package com.example.actividad1.Layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogo.R
import com.example.catalogo.data.BDCliente.AuthRepositoryImpl
import com.example.catalogo.data.RegistroUseCase
import com.example.catalogo.layouts.Registro.RegistroViewModel
import androidx.compose.ui.platform.LocalContext // IMPORTADO
import com.example.catalogo.layouts.perfil.saveClientIdToPrefs // IMPORTADO

@Composable
fun Registro(
    navController: NavController
) {
    val authRepository = remember { AuthRepositoryImpl() }
    val registroUseCase = remember { RegistroUseCase(authRepository) }
    val viewModel = remember { RegistroViewModel(registroUseCase) }

    val nombre by viewModel.nombre.observeAsState("")
    val apellidoPaterno by viewModel.apellidoPaterno.observeAsState("")
    val apellidoMaterno by viewModel.apellidoMaterno.observeAsState("")
    val correo by viewModel.correo.observeAsState("")
    val contraseña by viewModel.contraseña.observeAsState("")
    val confirmar by viewModel.confirmar.observeAsState("")

    val errorNombre by viewModel.errorNombre.observeAsState(null)
    val errorCorreo by viewModel.errorCorreo.observeAsState(null)
    val errorContraseña by viewModel.errorContraseña.observeAsState(null)

    val primaryColor = Color(0xFFD06A5B)
    val context = LocalContext.current // ✅ CONTEXTO OBTENIDO

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
                    Icon(
                        painter = painterResource(id = R.drawable.usuario),
                        contentDescription = "Usuario",
                        tint = Color.Gray,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Registro",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                val campos = listOf(
                    "Nombre" to nombre,
                    "Apellido Paterno" to apellidoPaterno,
                    "Apellido Materno" to apellidoMaterno,
                    "E-mail" to correo,
                    "Contraseña" to contraseña,
                    "Confirmar contraseña" to confirmar
                )

                campos.forEach { (label, valor) ->
                    val isPassword = label.contains("Contraseña")
                    val currentError = when (label) {
                        "Nombre" -> errorNombre
                        "E-mail" -> errorCorreo
                        "Contraseña", "Confirmar contraseña" -> errorContraseña
                        else -> null
                    }

                    OutlinedTextField(
                        value = valor,
                        onValueChange = {
                            when (label) {
                                "Nombre" -> viewModel.onNombreChanged(it)
                                "Apellido Paterno" -> viewModel.onApellidoPaternoChanged(it)
                                "Apellido Materno" -> viewModel.onApellidoMaternoChanged(it)
                                "E-mail" -> viewModel.onCorreoChanged(it)
                                "Contraseña" -> viewModel.onContraseñaChanged(it)
                                "Confirmar contraseña" -> viewModel.onConfirmarChanged(it)
                            }
                        },
                        label = { Text(label) },
                        singleLine = true,
                        isError = currentError != null,
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        modifier = Modifier
                            .width(300.dp)
                            .padding(vertical = 3.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        )
                    )

                    currentError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .width(300.dp)
                                .padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        viewModel.onRegisterClicked { idCliente ->
                            // 💥 GUARDAR EL ID DEL CLIENTE 💥
                            saveClientIdToPrefs(context, idCliente)
                            navController.navigate("RegistroMascota/${idCliente.toString()}")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .width(220.dp)
                        .height(45.dp)
                ) {
                    Text("CONTINUAR", color = Color.White)
                }
            }



            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes una cuenta? ", color = Color.Black.copy(alpha = 0.7f))
                TextButton(onClick = { navController.navigate("Login") }) {
                    Text(
                        text = "Ir a inicio de sesión",
                        color = primaryColor,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 16.sp
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logo_negro),
                contentDescription = "Logo Patitäs Veterinaria",
                modifier = Modifier.height(40.dp)
            )
        }
    }
}
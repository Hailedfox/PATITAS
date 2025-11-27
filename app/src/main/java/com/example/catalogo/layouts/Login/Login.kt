package com.example.catalogo.layouts.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogo.R
import com.example.catalogo.domain.UseCase.LoginUseCase
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.catalogo.data.BDCliente.AuthRepositoryImpl
import com.example.catalogo.layouts.perfil.getUriFromPrefs
import com.example.catalogo.layouts.perfil.saveClientDataToPrefs
import com.example.catalogo.layouts.perfil.saveClientIdToPrefs


@Composable
fun Login(
    navController: NavController,
) {
    val primaryColor = Color(0xFFD06A5B)

    val repository = remember { AuthRepositoryImpl() }
    val loginUseCase = remember { LoginUseCase(repository) }
    val viewModel = remember { LoginViewModel(loginUseCase) }

    val uiState by viewModel.uiState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // foto de perfil
    val context = LocalContext.current
    val profilePhotoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Fondo curvo
        val CurvedBottomShape = GenericShape { size, _ ->
            val width = size.width
            val height = size.height
            moveTo(0f, 0f)
            lineTo(0f, height * 0.85f)
            quadraticBezierTo(width / 2, height, width, height * 0.85f)
            lineTo(width, 0f)
            close()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(CurvedBottomShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black, CurvedBottomShape)
                    .graphicsLayer {
                        translationX = 230f
                        scaleX = 2.8f
                        scaleY = 2.8f
                        translationY = -180f
                    },
                contentScale = ContentScale.Fit,
            )
        }

        // Formulario (Column principal)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Column interno para los elementos del formulario (arriba)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
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
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Inicio sesión", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(50.dp))

                // Email
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("E-mail") },
                    singleLine = true,
                    modifier = Modifier.width(300.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.width(300.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 14.sp)
                }

                Button(
                    onClick = {
                        viewModel.onLoginSelected(
                            onSuccess = { userEntity ->
                                saveClientIdToPrefs(context, userEntity.id)
                                saveClientDataToPrefs(
                                    context,
                                    userEntity.nombre,
                                    userEntity.apellido_paterno,
                                    userEntity.email
                                )
                                navController.navigate("Menu")
                            },
                            onError = { errorMessage = "Usuario o contraseña incorrectos" }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .width(220.dp)
                        .height(48.dp),
                    enabled = uiState.isLoginEnabled
                ) {
                    Text("INICIAR SESIÓN", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta? ", color = Color.Black.copy(alpha = 0.7f))
                TextButton(onClick = { navController.navigate("registro") }) {
                    Text(
                        text = "Regístrate",
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
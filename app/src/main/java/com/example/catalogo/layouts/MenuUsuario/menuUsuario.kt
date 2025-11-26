package com.example.catalogo.layouts.perfil

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import java.io.File


val montserratAlternatesFamily: FontFamily = FontFamily.Default
val colorFondoContenido = Color(0xFFEEEEEE)
val colorTextoPrincipal = Color.DarkGray
val colorTextoSeccion = Color.Black


fun getPhotoFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File(filesDir, "profile_photo.jpg")
}

fun saveUriToPrefs(context: Context, uri: Uri) {
    val sharedPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit {
        putString("profile_photo_uri", uri.toString())
    }
}

fun getUriFromPrefs(context: Context): Uri? {
    val sharedPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
    val uriString = sharedPrefs.getString("profile_photo_uri", null)
    return uriString?.let { Uri.parse(it) }
}

// ----------------------------------------------------------------------

@Composable
fun MenuUsuario(navController: NavController) {
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf(getUriFromPrefs(context)) }

    val file = remember { getPhotoFile(context) }

    val cameraUri: Uri = remember {
        FileProvider.getUriForFile(
            context,
            "com.example.catalogo.fileprovider", // <-- Valor fijo
            file
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                photoUri = cameraUri
                saveUriToPrefs(context, cameraUri)
            } else {
                Toast.makeText(context, "No se pudo tomar la foto.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {

                cameraLauncher.launch(cameraUri)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    fun checkAndRequestPermission() {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                cameraLauncher.launch(cameraUri)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val CurvedBottomShape = GenericShape { size, _ ->
            val width = size.width; val height = size.height
            moveTo(0f, 0f); lineTo(0f, height * 0.65f)
            quadraticBezierTo(width / 2, height, width, height * 0.65f)
            lineTo(width, 0f); close()
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

            // Icono regresar
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
                        .clickable { navController.popBackStack() }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .background(
                    colorFondoContenido,
                    RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 105.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, Color.LightGray, CircleShape)
                .clickable {
                    checkAndRequestPermission()
                },
            contentAlignment = Alignment.Center
        ) {
            if (photoUri != null) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Usuario por defecto",
                    tint = Color.Gray,
                    modifier = Modifier.size(60.dp)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.camara),
                contentDescription = "Tomar foto",
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 5.dp, y = 5.dp)
                    .size(24.dp)
                    .background(Color.White, CircleShape)
                    .border(1.5.dp, Color.Gray, CircleShape)
                    .padding(4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 135.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(start = 10.dp) ) {

                Spacer(modifier = Modifier.height(65.dp))

                Text(
                    text = "Usuario",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // SECCIÓN 1: Cuenta
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                    PerfilItem(title = "Correo electrónico") {}
                    PerfilItem(title = "Cambiar contraseña") {}
                    PerfilItem(title = "Eliminar cuenta") {}
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                    color = Color.LightGray
                )

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                    PerfilItem(title = "Seguridad y Privacidad") {}
                    PerfilItem(title = "Cuentas Bancarios") {}
                    PerfilItem(title = "Ayuda") {}
                    PerfilItem(title = "Contáctanos") {}
                    PerfilItem(title = "Ubicaciones") {}
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                    color = Color.LightGray
                )

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)) {
                    TextButton(onClick = { navController.navigate("Login") }) {
                        Text(
                            text = "Cerrar sesión",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PerfilItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = montserratAlternatesFamily,
            color = colorTextoPrincipal
        )
    }
}
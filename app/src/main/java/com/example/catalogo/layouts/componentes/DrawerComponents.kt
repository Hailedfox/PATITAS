package com.example.catalogo.layouts.componentes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catalogo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    profilePhotoUri: Uri?
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.65f),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
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
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(text = "Usuario", fontSize = 20.sp, color = Color.Black)
        }

        DrawerItem("Mis citas", R.drawable.citas, Color.Black) {
            scope.launch {
                drawerState.close()
                navController.navigate("Mis citas")
            }
        }
        DrawerItem("Hoja clínica veterinaria", R.drawable.expediente, Color.Black) {
            scope.launch {
                drawerState.close()
                navController.navigate("Expediente")
            }
        }
        DrawerItem("Nosotros", R.drawable.informacion, Color.Black) {
            scope.launch {
                drawerState.close()
                navController.navigate("nosotros")
            }
        }
    }
}

@Composable
fun DrawerItem(title: String, iconRes: Int, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = color)
    }
}

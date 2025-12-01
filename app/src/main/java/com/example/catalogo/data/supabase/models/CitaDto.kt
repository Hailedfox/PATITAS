package com.example.catalogo.data.supabase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CitaDto(
    @SerialName("nombre_cliente")
    val nombreCliente: String,
    @SerialName("numero_emergencia")
    val numeroEmergencia: String,
    @SerialName("mascota_nombre")
    val mascotaNombre: String,
    @SerialName("servicio_nombre")
    val servicioNombre: String,
    val fecha: String,
    val hora: String,
    val estatus: String,
    @SerialName("id_cliente")
    val idCliente: Int
)

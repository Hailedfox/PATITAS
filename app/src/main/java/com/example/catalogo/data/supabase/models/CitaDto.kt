package com.example.catalogo.data.supabase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CitaDto(
    @SerialName("idcitas")
    val idCitas: Int = 0,
    @SerialName("idmascota")
    val idMascota: Int,
    @SerialName("idservicio")
    val idServicio: Int,
    @SerialName("idempleado")
    val idEmpleado: Int,
    @SerialName("idcliente")
    val idCliente: Int,
    @SerialName("fecha")
    val fecha: String,
    @SerialName("hora")
    val hora: String,
    @SerialName("estatus")
    val estatus: String? = null,
    @SerialName("tiposervicio")
    val tipoServicio: String
)

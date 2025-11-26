package com.example.catalogo.data.supabase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MascotaDto(
    @SerialName("idmascota")
    val idMascota: Int = 0,

    @SerialName("nombre")
    val nombre: String,

    @SerialName("especie")
    val especie: String,

    @SerialName("raza")
    val raza: String? = null,

    @SerialName("edad")
    val edad: Int? = null,

    @SerialName("sexo")
    val sexo: String,

    @SerialName("idcliente")
    val idCliente: Int
)

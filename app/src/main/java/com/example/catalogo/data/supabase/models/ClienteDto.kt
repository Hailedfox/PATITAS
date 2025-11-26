package com.example.catalogo.data.supabase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClienteDto(
    @SerialName("idcliente")
    val idCliente: Int = 0,
    @SerialName("nombrepila")
    val nombrePila: String,
    @SerialName("apellidopaterno")
    val apellidoPaterno: String,
    @SerialName("apellidomaterno")
    val apellidoMaterno: String? = null,
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String? = null,
    @SerialName("password")
    val password: String
)

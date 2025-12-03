package com.example.catalogo.data.supabase.models

import kotlinx.serialization.Serializable

@Serializable
data class ExpedienteDto(
    val idexpediente: Int,
    val iddueño: Int,
    val idmascota: Int,

    // NUMERIC — Double
    val pesoactual: Double? = null,
    val temperaturaultimavisita: Double? = null,

    // INT
    val frecuenciacardiaca: Int? = null,
    val frecuencirespiratoria: Int? = null,

    // TEXT / VARCHAR
    val condicioncorporal: String? = null,
    val esquemavacuna: String? = null,
    val proximavacuna: String? = null,
    val ultimadesparacitada: String? = null,
    val proximadesparacitada: String? = null,
    val alergias: String? = null,
    val cirujiasprevias: String? = null,
    val estadoreproductivo: String? = null,
    val fechaesterilizacion: String? = null,

    // INT
    val camadasprevias: Int? = null,

    // TEXT / VARCHAR
    val temperamento: String? = null,
    val reaccionmanejo: String? = null,
    val comportamientoclinica: String? = null
)

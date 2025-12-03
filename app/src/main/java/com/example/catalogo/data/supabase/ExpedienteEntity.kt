package com.example.catalogo.domain.Entity

data class ExpedienteEntity(
    val id: Int,
    val nombreMascota: String,
    val pesoActual: String,
    val temperaturaVisita: String,
    val frecuenciaCardiaca: String,
    val frecuenciaRespiratoria: String,
    val condicionCorporal: String,
    val esquemaVacuna: String,
    val proximaVacuna: String,
    val ultimaDesparasitacion: String,
    val proximaDesparasitacion: String,
    val alergias: String,
    val cirugiasPrevias: String,
    val estadoReproductivo: String,
    val fechaEsterilizacion: String,
    val camadasPrevias: String,
    val temperamento: String,
    val reaccionManejo: String,
    val comportamientoClinico: String
)

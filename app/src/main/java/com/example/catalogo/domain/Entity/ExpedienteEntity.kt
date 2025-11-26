package com.example.catalogo.domain.Entity

class ExpedienteEntity(
    val id: Int,
    val nombreMascota: String,

    // Datos del formulario

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

// --- Datos Ficticios para Pruebas (Lista de 1 Expediente) ---
val datosExpedientesFicticios: List<ExpedienteEntity> = listOf(
    ExpedienteEntity(
        id = 1, nombreMascota = "Max", pesoActual = "7.5 kg", temperaturaVisita = "38.5 °C",
        frecuenciaCardiaca = "120 lpm", frecuenciaRespiratoria = "25 rpm",
        condicionCorporal = "Ideal", esquemaVacuna = "Quíntuple", proximaVacuna = "05/12/2025",
        ultimaDesparasitacion = "10/06/2025", proximaDesparasitacion = "10/12/2025",
        alergias = "Ninguna conocida", cirugiasPrevias = "Castración (2024)",
        estadoReproductivo = "Castrado", fechaEsterilizacion = "15/03/2024",
        camadasPrevias = "0", temperamento = "Dócil", reaccionManejo = "Calma",
        comportamientoClinico = "Normal"
    )

    ,
    ExpedienteEntity(
        id = 2, nombreMascota = "Luna", pesoActual = "3.2 kg", temperaturaVisita = "38.9 °C",
        frecuenciaCardiaca = "130 lpm", frecuenciaRespiratoria = "30 rpm",
        condicionCorporal = "Bajo", esquemaVacuna = "Triple felina", proximaVacuna = "01/10/2025",
        ultimaDesparasitacion = "01/04/2025", proximaDesparasitacion = "01/10/2025",
        alergias = "Polen", cirugiasPrevias = "Ninguna", estadoReproductivo = "Entera",
        fechaEsterilizacion = "N/A", camadasPrevias = "1", temperamento = "Tímida",
        reaccionManejo = "Ansiedad", comportamientoClinico = "Activa"
    )

)

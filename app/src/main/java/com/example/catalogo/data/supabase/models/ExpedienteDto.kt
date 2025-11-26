package com.example.catalogo.data.supabase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Nueva tabla sugerida en Supabase:
 *
 * CREATE TABLE expediente (
 *   idexpediente SERIAL PRIMARY KEY,
 *   idmascota INT NOT NULL REFERENCES mascota(idmascota) ON DELETE CASCADE,
 *   peso NUMERIC(10,2),
 *   temperatura NUMERIC(10,2),
 *   frecuencia_cardiaca INT,
 *   frecuencia_respiratoria INT,
 *   condicion VARCHAR(100),
 *   esquema_vacuna TEXT,
 *   proxima_vacuna TEXT,
 *   ultima_desparasitacion TEXT,
 *   proxima_desparasitacion TEXT,
 *   alergias TEXT,
 *   cirugias_previas TEXT,
 *   estado_reproductivo TEXT,
 *   fecha_esterilizacion TEXT,
 *   camadas_previas TEXT,
 *   temperamento TEXT,
 *   reaccion_manejo TEXT,
 *   comportamiento_clinico TEXT
 * );
 */
@Serializable
data class ExpedienteDto(
    @SerialName("idexpediente")
    val idExpediente: Int = 0,
    @SerialName("idmascota")
    val idMascota: Int,
    @SerialName("peso")
    val peso: String? = null,
    @SerialName("temperatura")
    val temperatura: String? = null,
    @SerialName("frecuencia_cardiaca")
    val frecuenciaCardiaca: String? = null,
    @SerialName("frecuencia_respiratoria")
    val frecuenciaRespiratoria: String? = null,
    @SerialName("condicion")
    val condicion: String? = null,
    @SerialName("esquema_vacuna")
    val esquemaVacuna: String? = null,
    @SerialName("proxima_vacuna")
    val proximaVacuna: String? = null,
    @SerialName("ultima_desparasitacion")
    val ultimaDesparasitacion: String? = null,
    @SerialName("proxima_desparasitacion")
    val proximaDesparasitacion: String? = null,
    @SerialName("alergias")
    val alergias: String? = null,
    @SerialName("cirugias_previas")
    val cirugiasPrevias: String? = null,
    @SerialName("estado_reproductivo")
    val estadoReproductivo: String? = null,
    @SerialName("fecha_esterilizacion")
    val fechaEsterilizacion: String? = null,
    @SerialName("camadas_previas")
    val camadasPrevias: String? = null,
    @SerialName("temperamento")
    val temperamento: String? = null,
    @SerialName("reaccion_manejo")
    val reaccionManejo: String? = null,
    @SerialName("comportamiento_clinico")
    val comportamientoClinico: String? = null
)

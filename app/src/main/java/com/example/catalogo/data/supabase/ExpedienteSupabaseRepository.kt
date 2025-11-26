package com.example.catalogo.data.supabase

import android.util.Log
import com.example.catalogo.data.supabase.models.ExpedienteDto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpedienteSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "ExpedienteSupabaseRepo"

    suspend fun guardarExpediente(dto: ExpedienteDto): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val data = mapOf(
                    "idmascota" to dto.idMascota,
                    "peso" to dto.peso,
                    "temperatura" to dto.temperatura,
                    "frecuencia_cardiaca" to dto.frecuenciaCardiaca,
                    "frecuencia_respiratoria" to dto.frecuenciaRespiratoria,
                    "condicion" to dto.condicion,
                    "esquema_vacuna" to dto.esquemaVacuna,
                    "proxima_vacuna" to dto.proximaVacuna,
                    "ultima_desparasitacion" to dto.ultimaDesparasitacion,
                    "proxima_desparasitacion" to dto.proximaDesparasitacion,
                    "alergias" to dto.alergias,
                    "cirugias_previas" to dto.cirugiasPrevias,
                    "estado_reproductivo" to dto.estadoReproductivo,
                    "fecha_esterilizacion" to dto.fechaEsterilizacion,
                    "camadas_previas" to dto.camadasPrevias,
                    "temperamento" to dto.temperamento,
                    "reaccion_manejo" to dto.reaccionManejo,
                    "comportamiento_clinico" to dto.comportamientoClinico
                )

                client.postgrest["expediente"].insert(data)
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error guardando expediente: ${e.message}", e)
                false
            }
        }

    suspend fun obtenerExpedientesPorMascota(idMascota: Int): List<ExpedienteDto> =
        withContext(Dispatchers.IO) {
            try {
                client.postgrest["expediente"]
                    .select {
                        filter {
                            eq("idmascota", idMascota)
                        }
                    }
                    .decodeList<ExpedienteDto>()
            } catch (e: Exception) {
                Log.e(TAG, "Error obteniendo expedientes: ${e.message}", e)
                emptyList()
            }
        }
}

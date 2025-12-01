package com.example.catalogo.data.supabase

import android.util.Log
import com.example.catalogo.data.supabase.models.CitaDto
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Use a specific, serializable data class instead of a generic map
@Serializable
private data class CitaInsertDto(
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

class CitaSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "CitaSupabaseRepo"

    suspend fun obtenerCitasPorClienteId(clienteId: Long): List<CitaDto> = withContext(Dispatchers.IO) {
        try {
            val result = client.postgrest["citas"].select(Columns.ALL) {
                filter {
                    eq("id_cliente", clienteId)
                }
            }.decodeList<CitaDto>()
            Log.i(TAG, "Citas obtenidas: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener citas: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun guardarCitas(
        nombreCliente: String,
        numeroEmergencia: String,
        citas: List<TempCita>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // Map the list of TempCita to a list of the new serializable DTO
            val citasToInsert = citas.map { cita ->
                val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cita.fecha)
                CitaInsertDto(
                    nombreCliente = nombreCliente,
                    numeroEmergencia = numeroEmergencia,
                    mascotaNombre = cita.mascotaNombre,
                    servicioNombre = cita.servicioNombre,
                    fecha = fechaFormateada,
                    hora = cita.horario,
                    estatus = "Programada",
                    idCliente = cita.id_cliente
                )
            }

            // Insert the entire list in a single request
            client.postgrest["citas"].insert(citasToInsert)

            Log.i(TAG, "Citas registradas con éxito en Supabase")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar citas: ${e.message}", e)
            false
        }
    }

    suspend fun cancelarCita(citaId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            client.postgrest["citas"].update(
                { set("estatus", "Cancelada") }
            ) {
                filter {
                    eq("idcitas", citaId)
                }
            }
            Log.i(TAG, "Cita $citaId cancelada con éxito")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al cancelar cita: ${e.message}", e)
            false
        }
    }
    
    suspend fun completarCita(citaId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            client.postgrest["citas"].update(
                { set("estatus", "Completada") }
            ) {
                filter {
                    eq("idcitas", citaId)
                }
            }
            Log.i(TAG, "Cita $citaId completada con éxito")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al completar cita: ${e.message}", e)
            false
        }
    }
}


// MODELO PARA GUARDAR SIMILAR A TU TABLA
data class TempCita(
    val mascotaNombre: String,
    val servicioNombre: String,
    val fecha: Date,
    val horario: String,
    val id_cliente: Int
)
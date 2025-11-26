package com.example.catalogo.data.supabase

import android.util.Log
import com.example.catalogo.data.supabase.models.CitaDto
import com.example.catalogo.data.supabase.models.MascotaDto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitaSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "CitaSupabaseRepo"

    suspend fun crearCitasParaCliente(
        idCliente: Int,
        servicios: List<CitaInfo>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val inserts = servicios.map { info ->
                mapOf(
                    "idmascota" to info.idMascota,
                    "idservicio" to info.idServicio,
                    "idempleado" to info.idEmpleado,
                    "idcliente" to idCliente,
                    "fecha" to info.fecha,
                    "hora" to info.hora,
                    "tiposervicio" to info.tipoServicio
                )
            }

            // Inserta cada cita (puedes optimizar con RPC o bulk insert)
            inserts.forEach { data ->
                client.postgrest["citas"].insert(data)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creando citas: ${e.message}", e)
            false
        }
    }

    suspend fun obtenerCitasPorCliente(idCliente: Int): List<CitaVista> =
        withContext(Dispatchers.IO) {
            try {
                val citas = client.postgrest["citas"]
                    .select {
                        filter {
                            eq("idcliente", idCliente)
                        }
                    }
                    .decodeList<CitaDto>()

                val mascotas = client.postgrest["mascota"]
                    .select {
                        filter {
                            eq("idcliente", idCliente)
                        }
                    }
                    .decodeList<MascotaDto>()

                val mapaMascotas = mascotas.associateBy { it.idMascota }

                return@withContext citas.map { c ->
                    val mascota = mapaMascotas[c.idMascota]
                    CitaVista(
                        id = c.idCitas,
                        fecha = c.fecha,
                        hora = c.hora,
                        servicio = c.tipoServicio,
                        nombreMascota = mascota?.nombre ?: "Mascota #${c.idMascota}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error obteniendo citas: ${e.message}", e)
                emptyList()
            }
        }
}

data class CitaInfo(
    val idMascota: Int,
    val idServicio: Int,
    val idEmpleado: Int,
    val fecha: String,
    val hora: String,
    val tipoServicio: String
)

data class CitaVista(
    val id: Int,
    val fecha: String,
    val hora: String,
    val servicio: String,
    val nombreMascota: String
)

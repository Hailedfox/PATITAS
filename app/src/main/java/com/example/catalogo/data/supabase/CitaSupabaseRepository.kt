package com.example.catalogo.data.supabase

import android.util.Log
import com.example.catalogo.data.supabase.models.CitaDto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class CitaSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "CitaSupabaseRepo"

    // Guarda UNA cita por cada servicio del ViewModel
    suspend fun guardarCitas(
        nombreCliente: String,
        numeroEmergencia: String,
        citas: List<TempCita>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            citas.forEach { cita ->

                val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cita.fecha)

                val data = mapOf(
                    "nombre_cliente" to nombreCliente,
                    "numero_emergencia" to numeroEmergencia,
                    "mascota_nombre" to cita.mascotaNombre,
                    "servicio_nombre" to cita.servicioNombre,
                    "fecha" to fechaFormateada,
                    "hora" to cita.horario,
                    "estatus" to "Programada" // DEFAULT
                )

                client.postgrest["citas"].insert(data)
            }

            Log.i(TAG, "Citas registradas con éxito en Supabase")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar citas: ${e.message}", e)
            false
        }
    }
}


// MODELO PARA GUARDAR SIMILAR A TU TABLA
data class TempCita(
    val mascotaNombre: String,
    val servicioNombre: String,
    val fecha: java.util.Date,
    val horario: String
)

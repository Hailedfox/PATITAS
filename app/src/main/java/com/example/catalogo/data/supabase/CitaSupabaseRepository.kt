package com.example.catalogo.data.supabase

import android.util.Log
import com.example.catalogo.data.supabase.models.CitaDto
import com.example.catalogo.domain.UserSession // Importación necesaria para el ID del cliente
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStreamWriter
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.HttpsURLConnection

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
    val fecha: String, // Formato "yyyy-MM-dd"
    val hora: String, // Horario de la cita
    val estatus: String,
    @SerialName("id_cliente")
    val idCliente: Int
)

// ==========================================================
// DTOs para Webhook (n8n)
// ==========================================================

// Item individual de la cita para el payload del Webhook
@Serializable
data class CitaWebhookItem(
    @SerialName("mascota_nombre")
    val mascotaNombre: String,
    @SerialName("servicio_nombre")
    val servicioNombre: String,
    val fecha: String,
    val hora: String
)

// Payload completo que se envía al Webhook de n8n
@Serializable
data class WebhookPayload(
    @SerialName("nombre_cliente")
    val nombreCliente: String,
    @SerialName("numero_emergencia")
    val numeroEmergencia: String,
    @SerialName("id_cliente")
    val idCliente: Int,
    @SerialName("email_cliente")
    val emailCliente: String,
    val citas: List<CitaWebhookItem>
)

// ==========================================================
// REPOSITORIO
// ==========================================================

class CitaSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "CitaSupabaseRepo"
    private val WEBHOOK_URL = "https://emmap.app.n8n.cloud/webhook/confirmar-cita"


    // Función para obtener citas (existente)
    suspend fun obtenerCitasPorClienteId(clienteId: Long): List<CitaDto> = withContext(Dispatchers.IO) {
        // ... (Tu código existente para obtener citas)
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


    // Función para guardar citas en Supabase (existente)
    suspend fun guardarCitas(
        nombreCliente: String,
        numeroEmergencia: String,
        citas: List<TempCita>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val citasToInsert = citas.map { cita ->
                val fechaFormateada = dateFormat.format(cita.fecha)
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

            client.postgrest["citas"].insert(citasToInsert)
            Log.i(TAG, "Citas registradas con éxito en Supabase")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar citas: ${e.message}", e)
            false
        }
    }

    // 🚀 NUEVA FUNCIÓN: Enviar datos al Webhook de n8n
    suspend fun postWebhook(
        nombreCliente: String,
        numeroEmergencia: String,
        citas: List<TempCita>
    ) = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val citasPayload = citas.map { cita ->
            CitaWebhookItem(
                mascotaNombre = cita.mascotaNombre,
                servicioNombre = cita.servicioNombre,
                fecha = dateFormat.format(cita.fecha),
                hora = cita.horario
            )
        }

        // Se usa el ID del cliente de la sesión actual
        val clienteEmail = UserSession.currentUser?.email ?: ""
        val clienteId = UserSession.currentUser?.id ?: 0

        val payload = WebhookPayload(
            nombreCliente = nombreCliente,
            numeroEmergencia = numeroEmergencia,
            idCliente = clienteId,
            emailCliente = clienteEmail, // 🚨 CAMBIO AQUI: Se añade el email
            citas = citasPayload
        )

        try {
            val jsonPayload = Json.encodeToString(payload)
            val url = URL(WEBHOOK_URL)

            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonPayload)
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..202) {
                Log.i(TAG, "Webhook de n8n enviado con éxito. Código: $responseCode")
            } else {
                // Leer el error del cuerpo de la respuesta de n8n si existe
                val errorStream = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No body"
                Log.e(TAG, "Error al enviar Webhook. Código: $responseCode. Respuesta de n8n: $errorStream")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Excepción al enviar Webhook a n8n: ${e.message}", e)
        }
    }
    // ... (Tu código existente para cancelarCita, completarCita, verificarHorarioOcupado)

    suspend fun cancelarCita(citaId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            client.postgrest["citas"].update(
                { set("estatus", "Cancelada") }
            ) {
                filter {
                    eq("idcitas", citaId)
                }
            }
            Log.i(TAG, "Cita $citaId cancelada con éxito (estatus actualizado)")
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

    suspend fun verificarHorarioOcupado(fecha: Date, hora: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fecha)

            val resultList = client.postgrest["citas"].select {
                filter {
                    eq("fecha", fechaFormateada)
                    eq("hora", hora)
                }
            }.decodeList<CitaDto>()

            val isOcupado = resultList.isNotEmpty()

            Log.i(TAG, "Consulta Horario $fechaFormateada $hora: Ocupado=$isOcupado (Citas encontradas: ${resultList.size})")

            return@withContext isOcupado
        } catch (e: Exception) {
            Log.e(TAG, "Error al verificar horario ocupado: ${e.message}", e)
            return@withContext true
        }
    }
}

// MODELO PARA GUARDAR SIMILAR A TU TABLA (existente)
data class TempCita(
    val mascotaNombre: String,
    val servicioNombre: String,
    val fecha: Date,
    val horario: String,
    val id_cliente: Int
)
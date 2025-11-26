package com.example.catalogo.data.BDMascota

import android.util.Log
import com.example.catalogo.data.supabase.SupabaseClient
import com.example.catalogo.data.supabase.models.MascotaDto
import com.example.catalogo.domain.Entity.MascotaEntity
import com.example.catalogo.domain.Repository.MascotaRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MascotaRepositoryImpl : MascotaRepository {

    private val client = SupabaseClient.client
    private val TAG = "MascotaRepo"

    override suspend fun registroMascota(mascota: MascotaEntity, idCliente: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {

                // 👉 Convertir sexo a ENUM de Supabase
                val sexoConvertido = when (mascota.sexo.lowercase()) {
                    "macho", "m" -> "M"
                    "hembra", "h" -> "H"
                    else -> "M" // valor por defecto
                }

                val dto = MascotaDto(
                    idMascota = 0,
                    nombre = mascota.nombre,
                    especie = mascota.especie,
                    raza = mascota.raza,
                    edad = mascota.edad,
                    sexo = sexoConvertido, // <-- SOLUCIÓN REAL
                    idCliente = idCliente
                )

                client.postgrest["mascota"].insert(dto)

                val mascotas = client.postgrest["mascota"]
                    .select {
                        filter { eq("idcliente", idCliente) }
                    }
                    .decodeList<MascotaDto>()

                return@withContext mascotas.isNotEmpty()

            } catch (e: Exception) {
                Log.e(TAG, "Error registrando mascota en Supabase: ${e.message}", e)
                return@withContext false
            }
        }
}

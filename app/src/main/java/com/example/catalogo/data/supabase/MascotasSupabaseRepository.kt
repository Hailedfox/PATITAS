package com.example.catalogo.data.supabase

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest
import com.example.catalogo.data.supabase.models.MascotaDto

class MascotasSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "MascotasRepo"

    suspend fun obtenerMascotas(idCliente: Int): List<MascotaDto> {
        return try {
            val response = client.postgrest["mascota"]
                .select {
                    filter {
                        eq("idcliente", idCliente)
                    }
                }

            val lista = response.decodeList<MascotaDto>()

            return lista

        } catch (e: Exception) {
            Log.e(TAG, "Error cargando mascotas filtradas → ${e.message}")
            emptyList()
        }
    }
}
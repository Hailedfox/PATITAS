package com.example.catalogo.data.supabase

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest

class MascotasSupabaseRepository {

    private val client = SupabaseClient.client
    private val TAG = "MascotasRepo"

    suspend fun obtenerMascotas(idCliente: Int): List<String> {
        return try {

            val response = client.postgrest["mascota"].select()

            val lista = response.decodeList<MascotaRow>()
                .filter { it.idcliente == idCliente } // 🔥 filtrado correcto

            lista.map { it.nombre }

        } catch (e: Exception) {
            Log.e(TAG, "Error cargando mascotas → ${e.message}")
            emptyList()
        }
    }
}

data class MascotaRow(
    val nombre: String,
    val idcliente: Int
)

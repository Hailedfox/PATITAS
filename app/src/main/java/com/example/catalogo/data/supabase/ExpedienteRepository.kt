package com.example.catalogo.data.supabase

import com.example.catalogo.data.supabase.models.ExpedienteDto
import io.github.jan.supabase.postgrest.postgrest
import com.example.catalogo.data.supabase.SupabaseClient.client

class ExpedienteRepository {

    suspend fun obtenerExpediente(idCliente: Int, idMascota: Int): ExpedienteDto? {
        return client.postgrest["expediente"]
            .select {
                filter {
                    eq("iddueño", idCliente)
                    eq("idmascota", idMascota)
                }
                limit(1)
            }
            .decodeList<ExpedienteDto>()
            .firstOrNull()
    }
}

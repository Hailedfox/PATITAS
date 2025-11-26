package com.example.catalogo.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

object SupabaseClient {

    private const val SUPABASE_URL = "https://wqxqfuwhkaxbjqplocqs.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_LLfSj_mZZci5_lfjoal11g_fMNFFQ3Q"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}

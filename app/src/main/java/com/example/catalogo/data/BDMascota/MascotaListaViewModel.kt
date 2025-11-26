package com.example.catalogo.data.BDMascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.data.supabase.SupabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.postgrest
import android.util.Log
import com.example.catalogo.data.supabase.models.MascotaDto

class MascotaListaViewModel : ViewModel() {

    private val client = SupabaseClient.client

    private val _mascotas = MutableStateFlow<List<MascotaDto>>(emptyList())
    val mascotas : StateFlow<List<MascotaDto>> = _mascotas

    /**  Se llama desde Servicios Rápidos */
    fun cargarMascotas() {
        viewModelScope.launch {
            try {
                val response = client.postgrest["mascota"].select()
                _mascotas.value = response.decodeList<MascotaDto>()  // Lista para dropdown
                Log.i("MascotaVM", "Mascotas cargadas → ${_mascotas.value.size}")
            } catch (e: Exception) {
                Log.e("MascotaVM", "❌ Error al obtener mascotas: ${e.message}")
            }
        }
    }
}

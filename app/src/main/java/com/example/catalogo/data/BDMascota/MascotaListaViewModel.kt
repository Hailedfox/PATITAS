package com.example.catalogo.data.BDMascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.catalogo.data.supabase.MascotasSupabaseRepository // ⬅️ Importar el repositorio
import com.example.catalogo.data.supabase.models.MascotaDto

class MascotaListaViewModel(
    private val repository: MascotasSupabaseRepository = MascotasSupabaseRepository() // ⬅️ Inyección simple
) : ViewModel() {

    private val TAG = "MascotaVM"

    private val _mascotas = MutableStateFlow<List<MascotaDto>>(emptyList())
    val mascotas : StateFlow<List<MascotaDto>> = _mascotas


    fun cargarMascotas(clientId: Int) {
        viewModelScope.launch {
            try {
                val listaMascotas = repository.obtenerMascotas(clientId)

                _mascotas.value = listaMascotas
                Log.i(TAG, "Mascotas cargadas (filtradas por cliente $clientId) → ${_mascotas.value.size}")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener mascotas: ${e.message}")
            }
        }
    }
}
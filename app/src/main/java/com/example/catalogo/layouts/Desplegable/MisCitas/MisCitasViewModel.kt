package com.example.catalogo.layouts.Desplegable.MisCitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.data.supabase.CitaSupabaseRepository
import com.example.catalogo.data.supabase.models.CitaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MisCitasViewModel : ViewModel() {

    private val citaRepository = CitaSupabaseRepository()

    private val _citas = MutableStateFlow<List<CitaDto>>(emptyList())
    val citas: StateFlow<List<CitaDto>> = _citas

    var clienteId: Long = 0

    fun cargarCitas() {
        viewModelScope.launch {
            _citas.value = citaRepository.obtenerCitasPorClienteId(clienteId)
            println("Citas del cliente $clienteId: ${_citas.value}")

        }
    }
}
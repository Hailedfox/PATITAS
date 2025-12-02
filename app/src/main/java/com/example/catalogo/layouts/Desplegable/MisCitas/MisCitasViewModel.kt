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
        }
    }

    fun cancelarCita(cita: CitaDto) {
        viewModelScope.launch {
            val success = citaRepository.cancelarCita(cita.idCita)
            if (success) {
                cargarCitas()
            }
        }
    }
    
    fun completarCita(cita: CitaDto) {
        viewModelScope.launch {
            val success = citaRepository.completarCita(cita.idCita)
            if (success) {
                cargarCitas()
            }
        }
    }
}
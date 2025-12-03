package com.example.catalogo.layouts.Desplegable.Expediente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.data.supabase.ExpedienteRepository
import com.example.catalogo.data.supabase.MascotasSupabaseRepository
import com.example.catalogo.data.supabase.models.MascotaDto
import com.example.catalogo.domain.Entity.ExpedienteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpedienteViewModel : ViewModel() {

    private val expedienteRepo = ExpedienteRepository()
    private val mascotaRepo = MascotasSupabaseRepository()

    private val _mascotas = MutableStateFlow<List<MascotaDto>>(emptyList())
    val mascotas: StateFlow<List<MascotaDto>> = _mascotas

    private val _mascotaSeleccionada = MutableStateFlow<MascotaDto?>(null)
    val mascotaSeleccionada: StateFlow<MascotaDto?> = _mascotaSeleccionada

    private val _expediente = MutableStateFlow<ExpedienteEntity?>(null)
    val expediente: StateFlow<ExpedienteEntity?> = _expediente

    fun cargarMascotas(idCliente: Int) {
        viewModelScope.launch {
            _mascotas.value = mascotaRepo.obtenerMascotas(idCliente)
        }
    }

    fun seleccionarMascota(mascota: MascotaDto, idCliente: Int) {
        _mascotaSeleccionada.value = mascota
        cargarExpediente(idCliente, mascota.idMascota, mascota.nombre)
    }

    private fun cargarExpediente(idCliente: Int, idMascota: Int, nombreMascota: String) {
        viewModelScope.launch {

            val dto = expedienteRepo.obtenerExpediente(idCliente, idMascota)

            // --------- SI NO EXISTE EXPEDIENTE ---------
            if (dto == null) {
                _expediente.value = ExpedienteEntity(
                    id = 0,
                    nombreMascota = nombreMascota,
                    pesoActual = "Sin datos",
                    temperaturaVisita = "Sin datos",
                    frecuenciaCardiaca = "Sin datos",
                    frecuenciaRespiratoria = "Sin datos",
                    condicionCorporal = "Sin datos",
                    esquemaVacuna = "Sin datos",
                    proximaVacuna = "Sin datos",
                    ultimaDesparasitacion = "Sin datos",
                    proximaDesparasitacion = "Sin datos",
                    alergias = "Sin datos",
                    cirugiasPrevias = "Sin datos",
                    estadoReproductivo = "Sin datos",
                    fechaEsterilizacion = "Sin datos",
                    camadasPrevias = "Sin datos",
                    temperamento = "Sin datos",
                    reaccionManejo = "Sin datos",
                    comportamientoClinico = "Sin datos"
                )
                return@launch
            }

            // --------- SI EXISTE EXPEDIENTE ---------
            _expediente.value = ExpedienteEntity(
                id = dto.idexpediente,
                nombreMascota = nombreMascota,

                pesoActual = dto.pesoactual?.toString() ?: "Sin datos",
                temperaturaVisita = dto.temperaturaultimavisita?.toString() ?: "Sin datos",

                frecuenciaCardiaca = dto.frecuenciacardiaca?.toString() ?: "Sin datos",
                frecuenciaRespiratoria = dto.frecuencirespiratoria?.toString() ?: "Sin datos",

                condicionCorporal = dto.condicioncorporal ?: "Sin datos",
                esquemaVacuna = dto.esquemavacuna ?: "Sin datos",
                proximaVacuna = dto.proximavacuna ?: "Sin datos",
                ultimaDesparasitacion = dto.ultimadesparacitada ?: "Sin datos",
                proximaDesparasitacion = dto.proximadesparacitada ?: "Sin datos",
                alergias = dto.alergias ?: "Sin datos",
                cirugiasPrevias = dto.cirujiasprevias ?: "Sin datos",
                estadoReproductivo = dto.estadoreproductivo ?: "Sin datos",
                fechaEsterilizacion = dto.fechaesterilizacion ?: "Sin datos",
                camadasPrevias = dto.camadasprevias?.toString() ?: "Sin datos",
                temperamento = dto.temperamento ?: "Sin datos",
                reaccionManejo = dto.reaccionmanejo ?: "Sin datos",
                comportamientoClinico = dto.comportamientoclinica ?: "Sin datos"
            )
        }
    }
}

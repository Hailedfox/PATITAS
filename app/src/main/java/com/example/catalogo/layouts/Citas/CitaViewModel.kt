package com.example.catalogo.layouts.Citas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CitaItem(
    val mascotaNombre: String,
    val servicioNombre: String,
    val fecha: Date? = null,
    val horario: String = ""
)

class CitaViewModel : ViewModel() {

    var serviciosAgendados by mutableStateOf(listOf<CitaItem>())
    var servicioEnProceso by mutableStateOf<CitaItem?>(null)

    var nombreCliente by mutableStateOf("")
    var numeroEmergencia by mutableStateOf("")

    var fechaSeleccionadaTemp by mutableStateOf<Date?>(null)
    var horarioSeleccionadoTemp by mutableStateOf("")


    // → Ordenar cita por fecha / horario

    private fun ordenarServicios() {
        serviciosAgendados = serviciosAgendados.sortedWith(
            compareBy<CitaItem> { it.fecha }
                .thenBy { parseHorario(it.horario) }
        )
    }

    private fun parseHorario(horario: String): Date {
        val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        return try {

            format.parse(horario) ?: Date(0)
        } catch (e: ParseException) {
            println("Error al parsear horario: $horario. Usando fecha base (medianoche) como fallback.")
            Date(0)
        }
    }



    // → Nueva asignación
    fun iniciarAsignacion(mascota: String, servicio: String) {
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        servicioEnProceso = CitaItem(mascota, servicio)
    }

    // → CONFIRMA y ENVÍA A LISTA
    fun confirmarHorarioYAnadirALista(): Boolean {
        val servicioActual = servicioEnProceso

        if (servicioActual != null && fechaSeleccionadaTemp != null && horarioSeleccionadoTemp.isNotBlank()) {
            val itemCompleto = servicioActual.copy(
                fecha = fechaSeleccionadaTemp,
                horario = horarioSeleccionadoTemp
            )

            serviciosAgendados = serviciosAgendados + itemCompleto
            ordenarServicios()


            servicioEnProceso = null
            fechaSeleccionadaTemp = null
            horarioSeleccionadoTemp = ""

            return true
        }
        return false
    }

    fun eliminarServicio(item: CitaItem) {
        serviciosAgendados = serviciosAgendados.filter { it != item }
    }

    fun limpiarCitas() {
        serviciosAgendados = emptyList()
        servicioEnProceso = null
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        nombreCliente = ""
        numeroEmergencia = ""
    }


    // =====================================================
    // 🚨 Lo siguiente fue AGREGADO para evitar duplicar citas
    // =====================================================

    /** 🔥 Verifica si fecha + horario ya existen en la lista */
    fun horarioDisponibleLocal(fecha: Date, hora: String): Boolean {
        return serviciosAgendados.none { it.fecha == fecha && it.horario == hora }
    }

    /** 🔥 Versión lista para usar desde tu pantalla */
    fun validarHorario(fecha: Date?, hora: String, resultado: (Boolean) -> Unit) {
        if (fecha == null || hora.isBlank()) {
            resultado(false)
            return
        }

        viewModelScope.launch {

            val disponibleLocal = horarioDisponibleLocal(fecha, hora)

            // Si está libre en la app → devuelve TRUE
            resultado(disponibleLocal)
        }
    }
    fun tieneDatosTemporales(): Boolean {
        return fechaSeleccionadaTemp != null && horarioSeleccionadoTemp.isNotBlank()
    }
}

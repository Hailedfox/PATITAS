package com.example.catalogo.layouts.Citas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
    private fun parseHorario(horario: String): Date {
        val format = SimpleDateFormat("h:mm a", Locale.US)
        return try { format.parse(horario) ?: Date(0) } catch (_: ParseException) { Date(0) }
    }

    private fun ordenarServicios() {
        serviciosAgendados = serviciosAgendados.sortedWith(
            compareBy<CitaItem> { it.fecha }.thenBy { parseHorario(it.horario) }
        )
    }

    // → Nueva asignación
    fun iniciarAsignacion(mascota: String, servicio: String) {
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        servicioEnProceso = CitaItem(mascota, servicio)
    }

    // → CONFIRMA y ENVÍA A LISTA
    fun confirmarHorarioYAnadirALista(): Boolean {
        val actual = servicioEnProceso ?: return false
        if (fechaSeleccionadaTemp == null || horarioSeleccionadoTemp.isBlank()) return false

        serviciosAgendados = serviciosAgendados + actual.copy(
            fecha = fechaSeleccionadaTemp,
            horario = horarioSeleccionadoTemp
        )

        ordenarServicios()

        servicioEnProceso = null
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        return true
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
}

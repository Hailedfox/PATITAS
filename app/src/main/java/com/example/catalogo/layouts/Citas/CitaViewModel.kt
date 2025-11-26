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


    // ------------------------------------------------
    // ORDENAR POR FECHA + HORARIO
    // ------------------------------------------------
    private fun parseHorario(horario: String): Date {
        val format = SimpleDateFormat("h:mm a", Locale.US)
        return try { format.parse(horario) ?: Date(0) }
        catch (_: ParseException) { Date(0) }
    }

    fun ordenarServicios() {
        serviciosAgendados = serviciosAgendados.sortedWith(
            compareBy<CitaItem> { it.fecha }
                .thenBy { parseHorario(it.horario) }
        )
    }


    // ------------------------------------------------
    // NUEVAS FUNCIONES (⚠ LAS QUE FALTABAN)
    // ------------------------------------------------

    /** Se usa cuando el usuario quiere agregar más servicios */
    fun validarCitaLista(): Boolean {
        return fechaSeleccionadaTemp != null && horarioSeleccionadoTemp.isNotBlank()
    }

    /** Se usa cuando quiere terminar y pasar a confirmación */
    fun validarFinalizacion(): Boolean {
        return serviciosAgendados.isNotEmpty()
    }


    // ------------------------------------------------
    // Flujo principal
    // ------------------------------------------------
    fun iniciarAsignacion(mascota: String, servicio: String) {
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        servicioEnProceso = CitaItem(mascota, servicio)
    }

    fun confirmarHorarioYAnadirALista(): Boolean {
        val actual = servicioEnProceso ?: return false
        if(fechaSeleccionadaTemp == null || horarioSeleccionadoTemp.isBlank()) return false

        val final = actual.copy(
            fecha = fechaSeleccionadaTemp,
            horario = horarioSeleccionadoTemp
        )

        serviciosAgendados = serviciosAgendados + final
        ordenarServicios()

        servicioEnProceso = null
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        return true
    }


    fun eliminarServicio(item: CitaItem) {
        serviciosAgendados = serviciosAgendados.filter { it != item }
    }

    fun tieneDatosTemporales(): Boolean {
        return fechaSeleccionadaTemp != null && horarioSeleccionadoTemp.isNotBlank()
    }

    fun guardarCitaMaestra() {
        if (nombreCliente.isNotBlank() && numeroEmergencia.isNotBlank() && serviciosAgendados.isNotEmpty()) {
            println("------ CITA GUARDADA ------")
            println("Cliente: $nombreCliente")
            println("Emergencia: $numeroEmergencia")
            serviciosAgendados.forEach {
                println("${it.mascotaNombre} - ${it.servicioNombre} - ${it.fecha} - ${it.horario}")
            }
        } else {
            println("❌ No se pudo guardar, faltan datos!")
        }
    }
}

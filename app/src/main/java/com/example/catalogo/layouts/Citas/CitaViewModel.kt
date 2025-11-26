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

    // 🚨 CAMBIO 2: La temporal de fecha usa java.util.Date en lugar de LocalDate
    var fechaSeleccionadaTemp by mutableStateOf<Date?>(null)
    var horarioSeleccionadoTemp by mutableStateOf("")

    // MÉTODO DE ORDENAMIENTO CRONOLÓGICO

    fun ordenarServicios() {
        serviciosAgendados = serviciosAgendados.sortedWith(
            // La comparación ahora se hace directamente entre objetos Date (para fecha)
            // y el objeto Date devuelto por parseHorario (para hora).
            compareBy<CitaItem> { it.fecha }
                .thenBy { parseHorario(it.horario) }
        )
    }


    private fun parseHorario(horario: String): Date {
        val format = SimpleDateFormat("h:mm a", Locale.US)
        return try {

            format.parse(horario) ?: Date(0)
        } catch (e: ParseException) {
            println("Error al parsear horario: $horario. Usando fecha base (medianoche) como fallback.")
            Date(0)
        }
    }


    fun iniciarAsignacion(mascota: String, servicio: String) {
        fechaSeleccionadaTemp = null
        horarioSeleccionadoTemp = ""
        servicioEnProceso = CitaItem(mascota, servicio)
    }

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

    fun guardarCitaMaestra() {
        if (nombreCliente.isNotBlank() && numeroEmergencia.isNotBlank() && serviciosAgendados.isNotEmpty()) {
            println("--- REGISTRO DE CITA MAESTRA EN BASE DE DATOS ---")
            println("Cliente: $nombreCliente, Contacto: $numeroEmergencia")
            serviciosAgendados.forEachIndexed { index, item ->

                println("  [#${index + 1}] Mascota: ${item.mascotaNombre}, Servicio: ${item.servicioNombre}, Fecha/Hora: ${item.fecha} @ ${item.horario}")
            }
            println("-------------------------------------------------")
            // Aquí iría la lógica final de guardar en la DB
        } else {
            println("ERROR: Faltan datos del cliente o no hay servicios agendados.")
        }
    }

    fun eliminarServicio(item: CitaItem) {
        serviciosAgendados = serviciosAgendados.filter { it != item }
    }

    // Función de chequeo auxiliar
    fun tieneDatosTemporales(): Boolean {
        return fechaSeleccionadaTemp != null && horarioSeleccionadoTemp.isNotBlank()
    }
}
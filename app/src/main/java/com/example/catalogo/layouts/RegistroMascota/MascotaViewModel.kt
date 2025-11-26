package com.example.catalogo.layouts.RegistroMascota

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.data.BDMascota.MascotaRepositoryImpl
import com.example.catalogo.domain.Entity.MascotaEntity
import kotlinx.coroutines.launch

// CORRECCIÓN CLAVE: Recibir el repositorio en el constructor
class MascotaViewModel(
    private val repository: MascotaRepositoryImpl
) : ViewModel() {

    val nombre = MutableLiveData("")
    val especie = MutableLiveData("")
    val raza = MutableLiveData("")
    val edad = MutableLiveData("")
    val sexo = MutableLiveData("")

    val errorNombre = MutableLiveData<String?>()
    val errorEspecie = MutableLiveData<String?>()
    val errorSexo = MutableLiveData<String?>()

    private fun validarCampos(): Boolean {
        var valido = true

        if (nombre.value.isNullOrBlank()) {
            errorNombre.value = "Ingresa el nombre"
            valido = false
        } else errorNombre.value = null

        if (especie.value.isNullOrBlank()) {
            errorEspecie.value = "Ingresa la especie"
            valido = false
        } else errorEspecie.value = null

        if (sexo.value.isNullOrBlank()) {
            errorSexo.value = "Selecciona el sexo"
            valido = false
        } else errorSexo.value = null

        return valido
    }

    fun registrarMascota(idCliente: Int?, onSuccess: (() -> Unit)? = null) {
        val clienteId = idCliente ?: run {
            Log.e("MascotaViewModel", "ID Cliente es nulo. No se puede registrar la mascota.")
            return
        }

        if (!validarCampos()) return

        viewModelScope.launch {
            val mascota = MascotaEntity(
                nombre = nombre.value ?: "",
                especie = especie.value ?: "",
                raza = raza.value ?: "",
                edad = edad.value?.toIntOrNull(),
                sexo = sexo.value ?: ""
            )

            val success = repository.registroMascota(mascota, clienteId)
            if (success) {
                limpiarCampos()
                onSuccess?.invoke()
            } else {
                errorNombre.postValue("Error al guardar mascota. Inténtalo de nuevo.")
            }
        }
    }

    private fun limpiarCampos() {
        nombre.value = ""
        especie.value = ""
        raza.value = ""
        edad.value = ""
        sexo.value = ""
        errorNombre.value = null
        errorEspecie.value = null
        errorSexo.value = null
    }

    fun onNombreChanged(value: String) { nombre.value = value }
    fun onEspecieChanged(value: String) { especie.value = value }
    fun onRazaChanged(value: String) { raza.value = value }
    fun onEdadChanged(value: String) { edad.value = value }
    fun onSexoChanged(value: String) { sexo.value = value }
}
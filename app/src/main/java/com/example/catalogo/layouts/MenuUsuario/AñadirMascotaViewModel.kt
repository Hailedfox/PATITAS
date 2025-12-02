package com.example.catalogo.layouts.MenuUsuario // Paquete donde creaste el VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.domain.Entity.MascotaEntity
import com.example.catalogo.domain.Repository.MascotaRepository
import kotlinx.coroutines.launch


class AnadirMascotaViewModel(
    private val repository: MascotaRepository
) : ViewModel() {

    // --- LiveData de Estados de Campos ---
    private val _nombre = MutableLiveData("")
    val nombre: LiveData<String> = _nombre

    private val _especie = MutableLiveData("")
    val especie: LiveData<String> = _especie

    private val _raza = MutableLiveData("")
    val raza: LiveData<String> = _raza

    private val _edad = MutableLiveData("")
    val edad: LiveData<String> = _edad

    private val _sexo = MutableLiveData("")
    val sexo: LiveData<String> = _sexo

    // --- LiveData de Errores ---
    private val _errorNombre = MutableLiveData<String?>(null)
    val errorNombre: LiveData<String?> = _errorNombre

    private val _errorEspecie = MutableLiveData<String?>(null)
    val errorEspecie: LiveData<String?> = _errorEspecie

    private val _errorSexo = MutableLiveData<String?>(null)
    val errorSexo: LiveData<String?> = _errorSexo

    // --- Funciones de Cambio de Valor ---
    fun onNombreChanged(valor: String) {
        _nombre.value = valor
        _errorNombre.value = if (valor.isBlank()) "Nombre requerido" else null
    }

    fun onEspecieChanged(valor: String) {
        _especie.value = valor
        _errorEspecie.value = if (valor.isBlank()) "Especie requerida" else null
    }

    fun onRazaChanged(valor: String) { _raza.value = valor }

    fun onEdadChanged(valor: String) { _edad.value = valor }

    fun onSexoChanged(valor: String) {
        _sexo.value = valor
        _errorSexo.value = if (valor.isBlank()) "Sexo requerido" else null
    }

    // --- Lógica de Registro ---

    fun registrarMascota(idCliente: Int, onNavigate: (() -> Unit)? = null) {
        val nombre = _nombre.value.orEmpty()
        val especie = _especie.value.orEmpty()
        val sexo = _sexo.value.orEmpty()

        // Validación
        if (nombre.isBlank() || especie.isBlank() || sexo.isBlank()) {
            _errorNombre.value = if (nombre.isBlank()) "Nombre requerido" else null
            _errorEspecie.value = if (especie.isBlank()) "Especie requerida" else null
            _errorSexo.value = if (sexo.isBlank()) "Sexo requerido" else null
            return
        }

        viewModelScope.launch {
            val mascota = MascotaEntity(
                nombre = nombre,
                especie = especie,
                raza = _raza.value.orEmpty(),
                edad = _edad.value?.toIntOrNull(),
                sexo = sexo
            )

            val success = repository.registroMascota(mascota, idCliente)

            if (success) {
                _nombre.value = ""
                _especie.value = ""
                _raza.value = ""
                _edad.value = ""
                _sexo.value = ""

                // Limpiar errores
                _errorNombre.value = null
                _errorEspecie.value = null
                _errorSexo.value = null

                onNavigate?.invoke()
            } else {
            }
        }
    }
}
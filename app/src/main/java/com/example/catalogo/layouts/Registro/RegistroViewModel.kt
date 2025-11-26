package com.example.catalogo.layouts.Registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.catalogo.data.RegistroUseCase
import com.example.catalogo.domain.Entity.UserEntity
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val registroUseCase: RegistroUseCase
) : ViewModel() {

    private val _nombre = MutableLiveData("")
    val nombre: LiveData<String> = _nombre

    private val _apellidoPaterno = MutableLiveData("")
    val apellidoPaterno: LiveData<String> = _apellidoPaterno

    private val _apellidoMaterno = MutableLiveData("")
    val apellidoMaterno: LiveData<String> = _apellidoMaterno

    private val _correo = MutableLiveData("")
    val correo: LiveData<String> = _correo

    private val _contraseña = MutableLiveData("")
    val contraseña: LiveData<String> = _contraseña

    private val _confirmar = MutableLiveData("")
    val confirmar: LiveData<String> = _confirmar

    private val _errorNombre = MutableLiveData<String?>(null)
    val errorNombre: LiveData<String?> = _errorNombre

    private val _errorCorreo = MutableLiveData<String?>(null)
    val errorCorreo: LiveData<String?> = _errorCorreo

    private val _errorContraseña = MutableLiveData<String?>(null)
    val errorContraseña: LiveData<String?> = _errorContraseña

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered


    fun onNombreChanged(valor: String) {
        _nombre.value = valor
        _errorNombre.value = if (valor.isBlank()) "Nombre requerido" else null
    }

    fun onApellidoPaternoChanged(valor: String) { _apellidoPaterno.value = valor }
    fun onApellidoMaternoChanged(valor: String) { _apellidoMaterno.value = valor }
    fun onCorreoChanged(valor: String) { _correo.value = valor }
    fun onContraseñaChanged(valor: String) { _contraseña.value = valor }
    fun onConfirmarChanged(valor: String) { _confirmar.value = valor }


    fun onRegisterClicked(onSuccess: (idCliente: Int) -> Unit) {
        val nombre = _nombre.value.orEmpty()
        val apellidoPaterno = _apellidoPaterno.value.orEmpty()
        val apellidoMaterno = _apellidoMaterno.value.orEmpty()
        val correo = _correo.value.orEmpty()
        val contraseña = _contraseña.value.orEmpty()
        val confirmar = _confirmar.value.orEmpty()

        if (nombre.isBlank()) {
            _errorNombre.value = "Nombre requerido"
            return
        }

        if (correo.isBlank()) {
            _errorCorreo.value = "Correo requerido"
            return
        }

        if (contraseña != confirmar) {
            _errorContraseña.value = "Las contraseñas no coinciden"
            return
        }

        // Limpiar errores previos si las validaciones básicas pasaron
        _errorNombre.value = null
        _errorCorreo.value = null
        _errorContraseña.value = null

        viewModelScope.launch {
            val user = UserEntity(
                nombre = nombre,
                apellido_paterno = apellidoPaterno,
                apellido_materno = apellidoMaterno,
                email = correo,
                password = contraseña
            )

            val (success, idCliente) = registroUseCase(user)
            if (success && idCliente != null) {
                onSuccess(idCliente)
            } else {
                _errorCorreo.postValue("El correo ya está registrado o ocurrió un error")
            }
        }
    }
}

package com.example.catalogo.layouts.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogo.domain.Entity.UserEntity
import com.example.catalogo.domain.UseCase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState
    var user: UserEntity? = null


    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
        verifyLogin()
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
        verifyLogin()
    }

    fun onLoginSelected(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = loginUseCase(_uiState.value.email, _uiState.value.password)
            this@LoginViewModel.user = user
            _uiState.update { it.copy(isLoading = false, isUserLogged = user != null) }

            if (user != null) onSuccess() else onError()
        }
    }

    private fun verifyLogin() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        _uiState.update {
            it.copy(isLoginEnabled = email.contains("@") && password.length >= 6)
        }
    }
}

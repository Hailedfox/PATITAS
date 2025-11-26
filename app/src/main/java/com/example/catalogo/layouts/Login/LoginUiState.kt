package com.example.catalogo.layouts.Login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isUserLogged: Boolean = false
)
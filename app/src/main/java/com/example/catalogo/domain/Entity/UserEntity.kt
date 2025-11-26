package com.example.catalogo.domain.Entity

data class UserEntity(
    val id: Int = 0,
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val email: String,
    val password: String
)
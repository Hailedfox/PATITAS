package com.example.catalogo.data

import com.example.catalogo.data.BDCliente.AuthRepositoryImpl
import com.example.catalogo.domain.Entity.UserEntity

class RegistroUseCase(private val repository: AuthRepositoryImpl) {

    suspend operator fun invoke(user: UserEntity): Pair<Boolean, Int?> {
        // Ahora usamos el método corregido del repositorio
        return repository.registerUser(user)
    }
}

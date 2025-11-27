package com.example.catalogo.data

import com.example.catalogo.domain.Entity.UserEntity
import com.example.catalogo.domain.Repository.AuthRepository

class RegistroUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: UserEntity): Triple<Boolean, Int?, Boolean> {

        val emailExists = repository.checkEmailExists(user.email)

        if (emailExists) {
            return Triple(false, null, true)
        }

        val (success, idCliente) = repository.registerUser(user)

        return Triple(success, idCliente, false)
    }
}
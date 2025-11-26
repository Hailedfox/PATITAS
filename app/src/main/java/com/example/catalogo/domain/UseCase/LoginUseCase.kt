package com.example.catalogo.domain.UseCase

import com.example.catalogo.domain.Entity.UserEntity
import com.example.catalogo.domain.Repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: String, password: String): UserEntity? {
        // Validaciones básicas
        if (!isValidEmail(user) || password.isEmpty()) return null
        return authRepository.doLogin(user, password)
    }

    private fun isValidEmail(email: String): Boolean =
        email.contains("@") && email.contains(".")
}

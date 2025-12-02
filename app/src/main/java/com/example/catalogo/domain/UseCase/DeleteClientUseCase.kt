package com.example.catalogo.domain.UseCase

import com.example.catalogo.domain.Repository.AuthRepository

class DeleteClientUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(clientId: Int): Boolean {
        return repository.deleteClient(clientId)
    }
}
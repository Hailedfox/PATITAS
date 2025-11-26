package com.example.catalogo.domain.UseCase

import com.example.catalogo.domain.Entity.MascotaEntity
import com.example.catalogo.domain.Repository.MascotaRepository
import javax.inject.Inject

class MascotaUseCase @Inject constructor(
    private val mascotaRepository: MascotaRepository
) {
    suspend operator fun invoke(mascota: MascotaEntity, idCliente: Int): Boolean {
        if (mascota.nombre.isBlank() || mascota.especie.isBlank() || mascota.sexo.isBlank()) return false
        return mascotaRepository.registroMascota(mascota, idCliente)
    }
}
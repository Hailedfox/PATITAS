package com.example.catalogo.domain.Repository

import com.example.catalogo.domain.Entity.MascotaEntity

interface MascotaRepository {
    suspend fun registroMascota(mascota: MascotaEntity, idCliente: Int): Boolean
}
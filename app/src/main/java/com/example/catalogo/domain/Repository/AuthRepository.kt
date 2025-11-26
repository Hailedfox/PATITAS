package com.example.catalogo.domain.Repository

import com.example.catalogo.domain.Entity.UserEntity

interface AuthRepository {
    suspend fun doLogin(user: String, password: String): UserEntity?
    suspend fun registerUser(user: UserEntity): Pair<Boolean, Int?>
}

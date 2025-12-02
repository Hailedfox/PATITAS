package com.example.catalogo.domain

import com.example.catalogo.domain.Entity.UserEntity

object UserSession {

    var currentUser: UserEntity? = null

    fun getCurrentUserId(): Int? {
        return currentUser?.id
    }
}
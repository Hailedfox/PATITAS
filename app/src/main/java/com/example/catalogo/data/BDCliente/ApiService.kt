package com.example.catalogo.data.BDCliente

import com.example.catalogo.domain.Entity.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("registro_usuario.php")
    fun registrarUsuario(@Body user: UserEntity): Call<Map<String, String>>
}
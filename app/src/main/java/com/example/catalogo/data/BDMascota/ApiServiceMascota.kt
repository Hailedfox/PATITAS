package com.example.catalogo.data.BDMascota

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody


interface ApiServiceMascota {

    @POST("registro_mascota.php")
    suspend fun registrarMascota(
        @Body mascotaData: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>
}
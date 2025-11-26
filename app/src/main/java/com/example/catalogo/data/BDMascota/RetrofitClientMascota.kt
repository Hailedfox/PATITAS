package com.example.catalogo.data.BDMascota

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClientMascota {
    private const val BASE_URL = "http://10.0.2.2/veterinaria/"

    private val client: OkHttpClient = run {
        val interceptor = HttpLoggingInterceptor().apply {

            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    val api: ApiServiceMascota by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Agregamos el cliente HTTP con el Interceptor
            .client(client)
            // Converters necesarios
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceMascota::class.java)
    }
}
package com.example.catalogo.layouts.perfil

import android.content.Context
import android.net.Uri


const val PREFS_NAME = "user_prefs"
const val CLIENT_ID_KEY = "client_id"
const val PROFILE_PHOTO_KEY = "profile_photo_uri"

const val CLIENT_NOMBRE_PILA_KEY = "client_nombre_pila"
const val CLIENT_APELLIDO_PATERNO_KEY = "client_apellido_paterno"
const val CLIENT_EMAIL_KEY = "client_email"
fun getClientIdFromPrefs(context: Context): Int {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getInt(CLIENT_ID_KEY, 0)
}

fun saveClientIdToPrefs(context: Context, id: Int) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putInt(CLIENT_ID_KEY, id)
        .apply()
}


fun saveUriToPrefs(context: Context, uri: Uri?) {
    val uriString = uri?.toString()
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(PROFILE_PHOTO_KEY, uriString)
        .apply()
}


fun saveClientDataToPrefs(
    context: Context,
    nombrePila: String,
    apellidoPaterno: String,
    email: String
) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(CLIENT_NOMBRE_PILA_KEY, nombrePila)
        .putString(CLIENT_APELLIDO_PATERNO_KEY, apellidoPaterno)
        .putString(CLIENT_EMAIL_KEY, email)
        .apply()
}


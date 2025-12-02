package com.example.catalogo.data.BDCliente

import android.util.Log
import com.example.catalogo.data.supabase.SupabaseClient
import com.example.catalogo.data.supabase.models.ClienteDto
import com.example.catalogo.domain.Entity.UserEntity
import com.example.catalogo.domain.Repository.AuthRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthRepositoryImpl : AuthRepository {

    private val client = SupabaseClient.client
    private val TAG = "AuthRepositorySupabase"

    override suspend fun doLogin(user: String, password: String): UserEntity? =
        withContext(Dispatchers.IO) {
            try {
                val clientes = client.postgrest["cliente"]
                    .select {
                        filter {
                            eq("email", user)
                            eq("password", password)
                        }
                    }
                    .decodeList<ClienteDto>()

                val c = clientes.firstOrNull() ?: return@withContext null

                return@withContext UserEntity(
                    id = c.idCliente,
                    nombre = c.nombrePila,
                    apellido_paterno = c.apellidoPaterno,
                    apellido_materno = c.apellidoMaterno ?: "",
                    email = c.email,
                    password = c.password
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error en login Supabase: ${e.message}", e)
                return@withContext null
            }
        }

    override suspend fun registerUser(user: UserEntity): Pair<Boolean, Int?> =
        withContext(Dispatchers.IO) {
            try {
                // Insertar nuevo cliente en la tabla `cliente`
                client.postgrest["cliente"].insert(
                    mapOf(
                        "nombrepila" to user.nombre,
                        "apellidopaterno" to user.apellido_paterno,
                        "apellidomaterno" to user.apellido_materno,
                        "email" to user.email,
                        "username" to user.email,
                        "password" to user.password
                    )
                )

                val clientes = client.postgrest["cliente"]
                    .select {
                        filter {
                            eq("email", user.email)
                            eq("password", user.password)
                        }
                    }
                    .decodeList<ClienteDto>()

                val c = clientes.firstOrNull()
                val id = c?.idCliente

                return@withContext Pair(id != null, id)
            } catch (e: Exception) {
                Log.e(TAG, "Error en registro Supabase: ${e.message}", e)
                return@withContext Pair(false, null)
            }
        }

    override suspend fun checkEmailExists(email: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val clientes = client.postgrest["cliente"]
                    .select {
                        filter { eq("email", email)}
                        limit (1)
                    }
                    .decodeList<ClienteDto>()

                return@withContext clientes.isNotEmpty()
            } catch (e: Exception) {
                Log.e(TAG, "Error verificando email en Supabase: ${e.message}", e)
                return@withContext false
            }
        }

    override suspend fun deleteClient(clientId: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                client.postgrest["cliente"]
                    .delete {
                        filter {
                            eq("idcliente", clientId)
                        }
                    }
                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Error al eliminar cliente en Supabase: ${e.message}", e)
                return@withContext false
            }
        }

    // ⭐ NUEVA FUNCIÓN: Implementación del cambio de contraseña
    override suspend fun updatePassword(
        clientId: Int,
        currentPassword: String,
        newPassword: String
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                // 1. Verificar la contraseña actual
                val clientes = client.postgrest["cliente"]
                    .select {
                        filter {
                            eq("idcliente", clientId)
                            eq("password", currentPassword)
                        }
                    }
                    .decodeList<ClienteDto>()

                if (clientes.isEmpty()) {
                    // Contraseña actual no coincide
                    Log.w(TAG, "Intento fallido de cambio de contraseña para ID: $clientId. Contraseña actual incorrecta.")
                    return@withContext false
                }

                // 2. Si coincide, actualizar la contraseña
                client.postgrest["cliente"]
                    .update(
                        mapOf("password" to newPassword)
                    ) {
                        filter {
                            eq("idcliente", clientId)
                        }
                    }

                Log.i(TAG, "Contraseña actualizada exitosamente para ID: $clientId.")
                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Error al actualizar contraseña en Supabase: ${e.message}", e)
                return@withContext false
            }
        }
}
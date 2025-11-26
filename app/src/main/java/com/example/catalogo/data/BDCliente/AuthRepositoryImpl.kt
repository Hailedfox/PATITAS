package com.example.catalogo.data.BDCliente

import android.util.Log
import com.example.catalogo.data.supabase.SupabaseClient
import com.example.catalogo.data.supabase.models.ClienteDto
import com.example.catalogo.domain.Entity.UserEntity
import com.example.catalogo.domain.Repository.AuthRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementacion de AuthRepository usando SOLO Supabase PostgREST
 * (sin GoTrue, sin Auth avanzada).
 *
 * Login: consulta directa a la tabla `cliente` con email + password
 * Registro: inserta fila en tabla `cliente` y devuelve el idcliente
 */
class AuthRepositoryImpl : AuthRepository {

    private val client = SupabaseClient.client
    private val TAG = "AuthRepositorySupabase"

    override suspend fun doLogin(user: String, password: String): UserEntity? =
        withContext(Dispatchers.IO) {
            try {
                // Buscar cliente por email y password
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

                // Volver a leer para obtener el idcliente
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
}

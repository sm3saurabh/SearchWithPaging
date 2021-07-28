package dev.saurabhmishra.searchwithpagination.sources.network.helper

import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

sealed class SafeResult<out T> {
    class Success<T>(val data: T): SafeResult<T>()
    class Failure(val exception: Exception): SafeResult<Nothing>()
}

suspend fun <T> safeApiCall(executionContext: CoroutineContext, call: suspend () -> T): SafeResult<T> {
    return withContext(executionContext) {
        try {
            val result = call.invoke()
            SafeResult.Success(result)
        } catch (exception: Exception) {

            val newException = when (exception) {
                is UnknownHostException -> {
                    NoInternetException()
                }
                is HttpException -> {
                    ApiException(exception.code(), exception.response()?.errorBody().toString())
                }
                else -> {
                    exception
                }
            }

            SafeResult.Failure(exception = newException)
        }
    }
}

class ApiException(val code: Int, private val msg: String): Exception(message = msg)

class NoInternetException: Exception("No internet")

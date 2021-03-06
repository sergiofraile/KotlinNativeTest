package no.bakkenbaeck.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

sealed class RequestMethod(val stringValue: String) {
    class Get: RequestMethod("GET")
    class Put: RequestMethod("PUT")
    class Post(val body: String): RequestMethod("POST")
    class Delete: RequestMethod("DELETE")
    class Patch: RequestMethod("PATCH")
}

sealed class NetworkResult<T> {
    class Success<T>(val item: T) : NetworkResult<T>()
    class Error<T>(val message: String) : NetworkResult<T>()
}

internal expect val ApplicationDispatcher: CoroutineDispatcher


open class NetworkClient(val rootURLString: String) {

    //https://ktor.io/clients/http-client/calls/requests.html
    private val ktorClient = HttpClient()

    private fun fullURLStringForPath(path: String): String {
        return "$rootURLString/$path"
    }

    fun executeRequest(
        method: RequestMethod = RequestMethod.Get(),
        path: String,
        callback: (NetworkResult<String>) -> Unit
    ) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val result = when (method) {
                    is RequestMethod.Get -> get(path)
                    is RequestMethod.Post -> post(path, method.body)
                    else -> "NOT IMPLEMENTED"
                }

                callback(NetworkResult.Success(result))
            } catch (exception: Exception) {
                callback(NetworkResult.Error(exception.message ?: "¯\\_(ツ)_/¯"))
            }
        }
    }

    suspend fun get(
        path: String): String {
        val fullPath = fullURLStringForPath(path)
        return ktorClient.get {
            url(fullPath)
        }
    }

    suspend fun post(
        path: String,
        data: String
    ): String {
        val fullPath = fullURLStringForPath(path)
        return ktorClient.post {
            url(fullPath)
            body = data
        }
    }

}
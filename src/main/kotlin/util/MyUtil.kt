package org.example.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

object MyUtil {

    internal val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val client = OkHttpClient.Builder().build()


    fun <T> fetchFromUrl(url: String, serializer: KSerializer<T>): T? {
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().let { response ->
                val bodyString = response.body?.string()
                if (!response.isSuccessful || bodyString.isNullOrBlank()) {
                    return null
                }
                return json.decodeFromString(serializer, bodyString)
            }
        } catch (e: Throwable) {
            return null
        }
    }
}
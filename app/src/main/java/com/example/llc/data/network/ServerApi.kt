package com.example.llc.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {
    @GET("api/test/points")
    fun getPoints(@Query("count") count: Int): ServerPoints
}

class ServerApiImpl: ServerApi {

    var isMock = true

    @OptIn(ExperimentalSerializationApi::class)
    private val serverApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://88.212.235.131/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(ServerApi::class.java)
    }

    override fun getPoints(count: Int): ServerPoints {
        return if (isMock) {
            ServerPoints(
                listOf(
                    ServerPoint(-5, -5),
                    ServerPoint(1, 1),

                    ServerPoint(1, 1),
                    ServerPoint(3, 3),

                    ServerPoint(3, 3),
                    ServerPoint(5, 5),

                    ServerPoint(5, 5),
                    ServerPoint(7, 9),

                    ServerPoint(7, 9),
                    ServerPoint(9, 22),
                ).take(count)
            )
        } else {
            serverApi.getPoints(count)
        }
    }
}

@Serializable
data class ServerPoints(
    val points: List<ServerPoint>
)

@Serializable
data class ServerPoint(val x: Int, val y: Int)
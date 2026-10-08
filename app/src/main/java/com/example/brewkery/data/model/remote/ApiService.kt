package com.example.brewkery.data.model.remote



import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MenuResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApiService {
    @GET("data.json")
    suspend fun getFullMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItemDetail(@Path("id") id: String): MenuItem

    companion object {
        const val BASE_URL = "https://raw.githubusercontent.com/VivekShah138/Brewkery/main/"

        fun create(): BrewkeryApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BrewkeryApiService::class.java)
        }
    }
}
package com.iffecode.api_integration_v5

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("v1/search")
    fun getLocation(
        @Query("name") city: String,
        @Query("count") count: Int
    ): Call<GeocodingResponse>

    @GET("v1/forecast")
    fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String
    ): Call<OpenMeteoWeather>

    @GET("name/{country}")
    fun getCountry(
        @Path("country") country: String
    ): Call<MutableList<Country>>
}

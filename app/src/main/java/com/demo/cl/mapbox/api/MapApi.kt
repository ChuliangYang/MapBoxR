package com.demo.cl.mapbox.api

import com.demo.cl.mapbox.db.Pin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MapApi{
    @GET("scripts/get_map_pins.php")
    suspend fun getPins():List<Pin>
}

const val BASE_URL="https://annetog.gotenna.com/development/"
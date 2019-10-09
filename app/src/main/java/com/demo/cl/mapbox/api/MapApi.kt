package com.demo.cl.mapbox.api

import com.demo.cl.mapbox.db.Pin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MapApi{
    @GET("scripts/get_map_pins.php")
    suspend fun getPins():List<Pin>
}



class RetrofitClient{
    companion object{
        @Volatile private var INSTANCE:Retrofit?=null
        fun getInstance():Retrofit{
            if(INSTANCE ==null){
                synchronized(this){
                    if(INSTANCE ==null){
                        INSTANCE=Retrofit.Builder().baseUrl("https://annetog.gotenna.com/development/").addConverterFactory(GsonConverterFactory.create()).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
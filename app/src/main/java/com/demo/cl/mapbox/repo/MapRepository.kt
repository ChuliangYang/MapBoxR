package com.demo.cl.mapbox.repo

import androidx.lifecycle.LiveData
import com.demo.cl.mapbox.api.MapApi
import com.demo.cl.mapbox.db.Pin
import com.demo.cl.mapbox.db.PinsDao

interface IMapRepository {
    suspend fun getPinsFromRemote(): List<Pin>
    suspend fun addPinsToLocal(pins: List<Pin>)
    fun getPinsFromLocal(): LiveData<List<Pin>>
    suspend fun addPinToLocal(pin: Pin)
}

class MapRepository(val mapApi: MapApi, val pinsDao: PinsDao) : IMapRepository {

    override suspend fun getPinsFromRemote():List<Pin>{
       return mapApi.getPins()
    }

    override suspend fun addPinsToLocal(pins:List<Pin>){
        pinsDao.addPins(pins)
    }

    override fun getPinsFromLocal():LiveData<List<Pin>>{
        return pinsDao.getPins()
    }

    override suspend fun addPinToLocal(pin: Pin){
        pinsDao.addPin(pin)
    }
}
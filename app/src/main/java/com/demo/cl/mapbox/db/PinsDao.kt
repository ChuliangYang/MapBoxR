package com.demo.cl.mapbox.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.cl.mapbox.db.Pin

@Dao
interface PinsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPins(pins:List<Pin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPin(pin: Pin)

    @Query("SELECT * FROM Pins")
    fun getPins():LiveData<List<Pin>>

    @Query("DELETE FROM Pins")
    suspend fun clearPins()

}
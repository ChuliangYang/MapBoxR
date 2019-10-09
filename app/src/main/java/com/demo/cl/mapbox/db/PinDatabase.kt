package com.demo.cl.mapbox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pin::class], version = 1, exportSchema = false)
abstract class PinDatabase:RoomDatabase() {
        abstract fun pinsDao(): PinsDao

    companion object{
        @Volatile private var INSTANCE: PinDatabase?=null
        fun getInstance(context: Context): PinDatabase {
            if(INSTANCE ==null){
                synchronized(this){
                    INSTANCE = INSTANCE
                        ?: Room.databaseBuilder(context,
                            PinDatabase::class.java,"MapBox.db").build()
                }
            }
            return INSTANCE!!
        }
    }
}
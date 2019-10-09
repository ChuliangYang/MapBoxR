package com.demo.cl.mapbox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pin::class], version = 1, exportSchema = false)
abstract class PinDatabase:RoomDatabase() {
        abstract fun pinsDao(): PinsDao
}

const val DB_NAME="MapBox.db"
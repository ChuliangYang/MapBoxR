package com.demo.cl.mapbox.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng

//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions

@Entity(tableName = "Pins")
data class Pin(
    val description: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?
)

fun Pin.positionIsValid():Boolean{
    return latitude!=null&&longitude!=null
}

fun Pin.toMarkerOptions(): MarkerOptions?{
    if(positionIsValid()){
        return MarkerOptions().position(toLatLng()!!).title(name).snippet(description)
    }else{
        return null
    }
}

fun Pin.toLatLng(): LatLng?{
    return if (positionIsValid()){
        LatLng(latitude!!,longitude!!)
    }else {
        null
    }
}
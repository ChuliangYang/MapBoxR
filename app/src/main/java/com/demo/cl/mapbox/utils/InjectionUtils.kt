package com.demo.cl.mapbox.utils

import android.content.Context
import com.demo.cl.mapbox.api.MapApi
import com.demo.cl.mapbox.api.RetrofitClient
import com.demo.cl.mapbox.db.PinDatabase
import com.demo.cl.mapbox.db.PinsDao
import com.demo.cl.mapbox.repo.MapRepository

object InjectionUtils {
  fun injectMapRepo(context: Context): MapRepository {
      return MapRepository(
          injectMapApi(),
          injectPinsDao(context)
      )
  }

  fun injectMapApi():MapApi{
      return RetrofitClient.getInstance().create(MapApi::class.java)
  }

    fun injectPinsDao(context: Context):PinsDao{
        return  PinDatabase.getInstance(context).pinsDao()
    }


}
package com.demo.cl.mapbox.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.cl.mapbox.utils.InjectionUtils

class ViewModelFactory(var context: Context):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        modelClass.apply {
            return  when{
                isAssignableFrom(MapViewModel::class.java) ->
                    MapViewModel(InjectionUtils.injectMapRepo(context))
                else ->
                    throw java.lang.IllegalArgumentException()
            } as T
        }
    }

    companion object{
        @Volatile private var INSTANCE: ViewModelFactory?=null

        fun getInstance(context: Context): ViewModelFactory {
            if(INSTANCE ==null){
                synchronized(this){
                    if (INSTANCE ==null){
                        INSTANCE =
                            ViewModelFactory(context.applicationContext)
                    }
                }
            }

            return INSTANCE!!
        }
    }
}
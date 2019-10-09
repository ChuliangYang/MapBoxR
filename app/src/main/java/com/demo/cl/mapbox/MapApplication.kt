package com.demo.cl.mapbox

import android.app.Application
import com.demo.cl.mapbox.di.base.DaggerAutoInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MapApplication: Application(),HasAndroidInjector{
    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> =androidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAutoInjector.inject(this)
    }
}
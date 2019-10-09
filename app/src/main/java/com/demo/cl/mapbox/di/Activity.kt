package com.demo.cl.mapbox.di

import com.demo.cl.mapbox.MapsActivity
import com.demo.cl.mapbox.db.Pin
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.channels.Channel

@Module
abstract class ActivitySubComponentModule{
    @ContributesAndroidInjector(modules = [MapsActivityModule::class])
    abstract fun mapActivityInjector(): MapsActivity
}

@Module
abstract class MapsActivityModule{
    @Binds
    abstract fun providePermissionsListener(mapsActivity: MapsActivity):PermissionsListener


    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePinsChannel(): Channel<List<Pin>> {
            return Channel(1)
        }

        @JvmStatic
        @Provides
        fun providePermissionsManager(permissionsListener: PermissionsListener): PermissionsManager {
            return PermissionsManager(permissionsListener)
        }
    }
}
package com.demo.cl.mapbox.di

import android.app.Application
import androidx.room.Room
import com.demo.cl.mapbox.MapApplication
import com.demo.cl.mapbox.api.BASE_URL
import com.demo.cl.mapbox.api.MapApi
import com.demo.cl.mapbox.db.DB_NAME
import com.demo.cl.mapbox.db.PinDatabase
import com.demo.cl.mapbox.db.PinsDao
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule{
    @Singleton
    @Provides
    fun provideRetrofitClient():Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun providePinDatabase(application: Application):PinDatabase{
       return Room.databaseBuilder(application,
           PinDatabase::class.java,DB_NAME).build()
    }


    @Singleton
    @Provides
    fun provideMapApi(retrofit: Retrofit):MapApi{
        return retrofit.create(MapApi::class.java)
    }

    @Singleton
    @Provides
    fun providePinsDao(pinDatabase: PinDatabase):PinsDao{
        return pinDatabase.pinsDao()
    }
}

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivitySubComponentModule::class
])
interface AppComponent{
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application:Application):Builder
        fun build():AppComponent
    }

    fun inject(application: MapApplication)
}

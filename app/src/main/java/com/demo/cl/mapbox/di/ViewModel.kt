package com.demo.cl.mapbox.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.cl.mapbox.di.base.ViewModelKey
import com.demo.cl.mapbox.repo.IMapRepository
import com.demo.cl.mapbox.repo.MapRepository
import com.demo.cl.mapbox.ui.viewmodel.MapViewModel
import com.demo.cl.mapbox.ui.viewmodel.MyViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindCityListViewModel(mapViewModel:MapViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory

    @Binds
    abstract fun bindIMapRepository(mapRepository: MapRepository): IMapRepository
}
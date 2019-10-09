package com.demo.cl.mapbox.ui.viewmodel

import androidx.lifecycle.*
import com.demo.cl.mapbox.db.Pin
import com.demo.cl.mapbox.repo.IMapRepository
import com.demo.cl.mapbox.repo.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(private val mapRepository: IMapRepository):ViewModel() {
    val localPins=MediatorLiveData<List<Pin>>()
    private var oldPins:LiveData<List<Pin>>?=null

    fun getPinsFromServer(forcedFetch:Boolean){
        viewModelScope.launch(Dispatchers.Main) {
                if(forcedFetch||oldPins==null){
                    mapRepository.apply {
                        addPinsToLocal(getPinsFromRemote())
                    }
                }
                if(oldPins==null){
                    oldPins= mapRepository.getPinsFromLocal().also {pins->
                        localPins.addSource(pins) {
                            localPins.postValue(it)
                        }
                    }
                }
        }
    }

    fun savePin(pin: Pin){
        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.addPinToLocal(pin)
        }
    }
}
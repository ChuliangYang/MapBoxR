package com.demo.cl.mapbox.ui.viewmodel

import androidx.lifecycle.*
import com.demo.cl.mapbox.db.Pin
import com.demo.cl.mapbox.repo.IMapRepository
import com.demo.cl.mapbox.repo.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val mapRepository: IMapRepository):ViewModel() {
    val localPins=MediatorLiveData<List<Pin>>()
    private var oldPins:LiveData<List<Pin>>?=null

    fun getPinsFromServer(){
        viewModelScope.launch(Dispatchers.Main) {
                mapRepository.apply {
                    addPinsToLocal(getPinsFromRemote())
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
package dev.marawanxmamdouh.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.marawanxmamdouh.marsrealestate.network.MarsApi
import dev.marawanxmamdouh.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _property = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty>
        get() = _property

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        viewModelScope.launch {
            try {

                val listResult = MarsApi.retrofitService.getProperties()
                if (listResult.isNotEmpty()) {
                    _property.value = listResult[0]
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }
}

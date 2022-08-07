package dev.marawanxmamdouh.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.marawanxmamdouh.marsrealestate.network.MarsApi
import dev.marawanxmamdouh.marsrealestate.network.MarsProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

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
        MarsApi.retrofitService.getProperties().enqueue( object: Callback<List<MarsProperty>> {
            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
                _response.value = "Success: ${response.body()?.size} Mars properties retrieved"
            }
        })
    }
}

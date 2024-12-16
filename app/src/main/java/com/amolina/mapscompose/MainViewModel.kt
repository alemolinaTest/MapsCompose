package com.amolina.mapscompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amolina.mapscompose.data.City
import com.amolina.mapscompose.models.LocalCity
import com.amolina.mapscompose.repositories.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val dispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private val _citiesState = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _citiesState

    private val _jsonCitiesState = MutableStateFlow<List<LocalCity>>(emptyList())
    val jsonCities: StateFlow<List<LocalCity>> = _jsonCitiesState

    private val _filteredList = MutableStateFlow<List<LocalCity>>(emptyList())

    private val _selectedCity = MutableStateFlow<LocalCity?>(null)
    val selectedCity: StateFlow<LocalCity?> = _selectedCity

    init {
        fetchFilteredJsonCities("")
    }

    fun fetchJsonCities() {
        viewModelScope.launch(context = dispatcher) {
            _jsonCitiesState.value = citiesRepository.getJsonCities()
        }
    }

    fun fetchFilteredJsonCities(filter: String) {
        viewModelScope.launch(context = dispatcher) {
            if (_filteredList.value.isEmpty()) {
                _jsonCitiesState.value = citiesRepository.getJsonCities().filter { cities ->
                    cities.name.contains(filter,true) && cities.country.contains("US",true)
                }.take(30)
                _filteredList.value = _jsonCitiesState.value
            } else {
                _jsonCitiesState.value =
                    _filteredList.value.filter { cities -> cities.name.contains(filter,true) }
            }
        }
    }

    fun fetchFilteredIdJsonCities(cityId: Int) {
        viewModelScope.launch(context = dispatcher) {
            _selectedCity.value =
                _jsonCitiesState.value.filter { cities -> cities.id == cityId }.first()
        }
    }

    fun fetchCities() {
        viewModelScope.launch(context = dispatcher) {
            citiesRepository.getCities()
                .collect { citiesList ->
                    _citiesState.value = citiesList
                }
        }
    }

    fun getFilteredCities(filter: String) {
        viewModelScope.launch(context = dispatcher) {
            citiesRepository.getCities()
                .map { cities -> cities.filter { it.name.contains(filter, true) } }
                .collect { citiesList ->
                    _citiesState.value = citiesList
                }
        }
    }

}
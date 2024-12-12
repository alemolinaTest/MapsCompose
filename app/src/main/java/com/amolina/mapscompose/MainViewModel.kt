package com.amolina.mapscompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amolina.mapscompose.data.City
import com.amolina.mapscompose.models.LocalCity
import com.amolina.mapscompose.repositories.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val citiesRepository: CitiesRepository) :
    ViewModel() {
    private val _citiesState = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _citiesState

    private val _jsonCitiesState = MutableStateFlow<List<LocalCity>>(emptyList())
    val jsonCities: StateFlow<List<LocalCity>> = _jsonCitiesState

    init {
        fetchFilteredJsonCities()
    }

    private fun fetchJsonCities() {
        viewModelScope.launch {
            _jsonCitiesState.value = citiesRepository.getJsonCities()
        }
    }

    private fun fetchFilteredJsonCities() {
        viewModelScope.launch {
            _jsonCitiesState.value = citiesRepository.getJsonCities().filter { cities -> cities.country.contains("AR") && cities.name.contains("Salta") }
        }
    }

    private fun fetchCities() {
        viewModelScope.launch {
            citiesRepository.getCities()
                .collect { citiesList ->
                    _citiesState.value = citiesList
                }
        }
    }

    fun fetchFilteredCities(filter: String) {
        viewModelScope.launch {
            citiesRepository.getCities()
                .map { cities -> cities.filter { it.name.contains(filter, true) } }
                .collect { citiesList ->
                    _citiesState.value = citiesList
                }
        }
    }

}
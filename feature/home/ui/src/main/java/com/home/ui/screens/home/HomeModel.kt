package com.home.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Resource
import com.core.database.dao.PetsDao
import com.core.database.model.toPetInfo
import com.home.domain.use_cases.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val dao: PetsDao
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    var state by mutableStateOf(PetStates())
        private set

    //make the uiEvent sealed class
    //use the use case data class way

    init {
        viewModelScope.launch {
            //if the user didn't type anything for 1 second then we make out api call
            searchQuery.debounce(1000).collectLatest {
                getSearchResult(it)
            }
        }
    }

    fun setQuery(newQuery: String){
        _searchQuery.value = newQuery
    }

    fun getPetsInfos(fromRemote: Boolean = false) {

        viewModelScope.launch {
            homeUseCase.getPosts().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                        state = state.copy(petsListing =dao.getAllPets().firstOrNull() )
                    }
                    is Resource.Success -> {
                        dao.getAllPets().collectLatest {
                            state = state.copy(petsListing = it)
                        }

                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getPetsInfos: ${resource.message}")
                        state = state.copy(petsListing =dao.getAllPets().firstOrNull(), errorMessage = resource.message )
                    }
                }
            }
        }
    }

    private fun getSearchResult(query:String) {

        viewModelScope.launch {
            homeUseCase.getSearchResult(query).onEach { resource ->
                state = when (resource) {
                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }

                    is Resource.Success -> {
                        state.copy(petsListing = resource.data?.data?.map { it.toPetInfo() })

                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getSearchInfo: ${resource.message}")
                        state.copy(errorMessage = resource.message )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    internal fun addFavorite(id:Int){

        viewModelScope.launch {
            homeUseCase.addFavorite(id).collect{resource->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                       // getPetsInfos()
                        Log.d("AppSuccess", "addFavorite:${resource.data?.message} ")
                    }
                    is Resource.Error -> {
                        Log.d("AppError", "addFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }

    internal fun removeFavorite(id:Int){

        viewModelScope.launch {
            homeUseCase.removeFavoriteHome(id).collect{resource->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        //getPetsInfos()
                        Log.d("AppSuccess", "removeFavorite:${resource.data?.message} ")
                    }
                    is Resource.Error -> {
                        Log.d("AppError", "removeFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }

    internal fun getProfile(){
        viewModelScope.launch {
            homeUseCase.getProfileHome().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        resource.data?.let {
                            state = state.copy(profileName = it.name,profileCountry = it.country)
                        }
                    }
                    is Resource.Error -> {
                        resource.message?.let {
                            state = state.copy(errorMessage = it)
                        }
                        Log.d("AppError", "getFavorite: ${state.errorMessage}")
                    }
                }
            }
        }
    }
}
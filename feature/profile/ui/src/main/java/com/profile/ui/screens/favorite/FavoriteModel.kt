package com.profile.ui.screens.favorite

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Resource
import com.feature.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): ViewModel(){

    var state by mutableStateOf(FavoriteState())
        private set


    fun getFavorites(){
        viewModelScope.launch {
            profileUseCase.getFavoritePets().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        resource.data?.let {
                            state = state.copy(petListings = it)
                        }
                    }
                    is Resource.Error -> {
                        resource.message?.let {
                            state = state.copy(error = it)
                        }
                        Log.d("AppError", "getFavorite: ${state.error}")
                    }
                }
            }
        }
    }


    internal fun removeFavorite(id:Int){

        viewModelScope.launch {
            profileUseCase.removeFavorite(id).collect{resource->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        getFavorites()
                        Log.d("AppSuccess", "removeFavorite:${resource.data?.message} ")
                    }
                    is Resource.Error -> {
                        Log.d("AppError", "removeFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }
}
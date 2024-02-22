package com.profile.ui.screens.my_pets

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.utls.Resource
import com.feature.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPetsModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
):ViewModel(){

    var state by mutableStateOf(MyPetState())
        private set


    fun getMyPets(){
        viewModelScope.launch {
            profileUseCase.getMyPets().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        resource.data?.let {
                            state = state.copy(petListings = it.reversed())
                        }
                    }
                    is Resource.Error -> {
                        resource.message?.let {
                            state = state.copy(error = it)
                        }
                        Log.d("AppError", "getMyPets: ${state.error}")
                    }
                }
            }
        }
    }

    internal fun deletePost(id:Int){

        viewModelScope.launch {
            profileUseCase.deletePost(id).collect{resource->
                when(resource){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        getMyPets()
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
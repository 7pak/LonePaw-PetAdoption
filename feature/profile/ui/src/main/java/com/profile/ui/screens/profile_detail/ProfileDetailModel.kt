package com.profile.ui.screens.profile_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Resource
import com.core.network.profile_api.model.AddPostData
import com.core.network.profile_api.model.UpdateProfileData
import com.feature.profile.domain.use_cases.ProfileUseCase
import com.profile.ui.screens.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
):ViewModel(){


    var state by mutableStateOf(ProfileDetailState())
        private set

    init {
        getProfile()
    }

    fun updateProfileStete(newState:ProfileDetailState){
        state = newState.copy()
    }


    private fun getProfile(){
        viewModelScope.launch {
            profileUseCase.getProfile().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {

                        resource.data?.let {
                            state = state.copy( address = it.address,
                                contactNumber = it.contactNumber,
                                country = it.country,
                                email = it.email,
                                name = it.name,
                                username = it.username)
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


    fun updateProfile() {

        viewModelScope.launch {
            profileUseCase.updateProfile(
                UpdateProfileData(
                    address = state.address,
                    contactNumber = state.contactNumber,
                    country = state.country,
                    email = state.email,
                    name = state.name,
                    username = state.username)
            ).collect { resource ->

                when (resource) {

                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        getProfile()
                        Log.d("AppSuccess", "updateProfile: ${resource.data?.status?.description}")
                        Log.d("AppSuccess", "updateProfile: ${resource.data?.status}---${resource.message}")
                    }
                    is Resource.Error -> {
                        state = state.copy(error = "${resource.data?.status?.description}")
                        Log.d("AppError", "updateProfile: ${resource.data?.status?.description}")
                        Log.d("AppError", "updateProfile: ${resource.data?.status?.value}---${resource.message}")
                    }
                }
            }
        }
    }
}
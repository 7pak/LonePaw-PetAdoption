package com.profile.ui.screens.profile_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.utls.Resource
import com.core.network.profile_api.model.UpdateProfileData
import com.feature.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun updateProfileState(newState:ProfileDetailState){
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
                                username = it.username,
                                profilePic = it.profilePic
                            )
                        }

                    }
                    is Resource.Error -> {
                        Log.d("AppError", "getProfile:${resource.message} ")
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
                    username = state.username,
                    profilePic = state.profilePic?.toUri())
            ).collect { resource ->

                when (resource) {

                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        getProfile()
                        state = state.copy(serverMessage = resource.message.toString())
                        Log.d("AppSuccess", "updateProfile: ${resource.data?.status?.description}")
                        Log.d("AppSuccess", "updateProfile: ${resource.data?.status}---${resource.message}")
                    }
                    is Resource.Error -> {
                        state = state.copy(serverMessage = resource.message.toString())
                        Log.d("AppError", "updateProfile: ${resource.data?.status?.description}")
                        Log.d("AppError", "updateProfile: ${resource.data?.status?.value}---${resource.message}")
                    }
                }
            }
        }
    }
}
package com.profile.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.core.database.model.PetInfo
import com.feature.profile.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val userVerificationModel: UserVerificationModel
): ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    var lastFavorite:PetInfo? by mutableStateOf(null)
        private set

    var lastMyPet:PetInfo? by mutableStateOf(null)
        private set



    fun getProfile(){
        viewModelScope.launch {
            profileUseCase.getProfile().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        getLastPet()
                        getLastFavorite()
                        resource.data?.let {
                            state = state.copy(profileName = it.name, profilePic = it.profilePic?:"")
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

    fun logout(){
        userVerificationModel.clearToken()
    }

    private fun getLastPet(){
        viewModelScope.launch {
            profileUseCase.getMyPets().collect{resource->
                when (resource) {
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val lastPet = resource.data?.lastOrNull()
                        if (lastPet != null) {
                            lastMyPet = lastPet
                        }
                    }
                }
            }
        }
    }

    private fun getLastFavorite(){
        viewModelScope.launch {
            profileUseCase.getFavoritePets().collect{resource->
                when (resource) {
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val lastLike = resource.data?.lastOrNull()
                        if (lastLike != null) {
                            lastFavorite = lastLike
                        }
                    }
                }
            }
        }
    }
}
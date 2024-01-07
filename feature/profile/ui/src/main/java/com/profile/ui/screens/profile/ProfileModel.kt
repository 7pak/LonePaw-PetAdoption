package com.profile.ui.screens.profile

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
class ProfileModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set



    fun getProfile(){
        viewModelScope.launch {
            profileUseCase.getProfile().collectLatest {resource->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        resource.data?.let {
                            state = state.copy(profileName = it.name)
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
}
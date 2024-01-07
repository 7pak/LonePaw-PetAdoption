package com.home.ui.screens.pet_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Resource
import com.home.domain.use_cases.GetPosts
import com.home.domain.use_cases.HomeUseCase
import com.home.ui.screens.navArgs
import com.home.ui.shared.PetDetailNavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    handle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(PetDetailState())

    private var postId by mutableIntStateOf(-1)


    init {
        viewModelScope.launch {

            postId = handle.navArgs<PetDetailNavArgs>().id
            getPetInfo()
        }

    }


    fun getPetInfo() {

        viewModelScope.launch {
            homeUseCase.getPet(postId).collectLatest { resource ->
                val petInfo = resource.data
                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        petInfo?.let { pet ->
                            state = state.copy(
                                address = pet.address,
                                contactNumber = pet.contactNumber,
                                country = pet.country,
                                createdAt = pet.createdAt,
                                petAge = pet.petAge,
                                petBreed = pet.petBreed,
                                petGender = pet.petGender,
                                petName = pet.petName,
                                ownerName = pet.ownerName,
                                petDesc = pet.petDesc,
                                petPhoto = pet.petPhoto,
                                petType = pet.petType,
                                petFavorite = pet.petFavorite
                            )
                        }
                    }

                    is Resource.Error -> Log.d("AppError", "getPetsInfos: ${resource.message}")
                }
            }
        }
    }

    internal fun addFavorite(id: Int) {

        viewModelScope.launch {
            homeUseCase.addFavorite(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        homeUseCase.getPosts()
                        Log.d("AppSuccess", "addFavorite:${resource.data?.message} ")
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "addFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }

    internal fun removePets(id: Int) {

        viewModelScope.launch {
            homeUseCase.removeFavoriteHome(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        homeUseCase.getPosts()
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
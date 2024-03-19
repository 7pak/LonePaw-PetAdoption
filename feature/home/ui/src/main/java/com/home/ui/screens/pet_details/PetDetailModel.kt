package com.home.ui.screens.pet_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Constants
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.google.firebase.firestore.FirebaseFirestore
import com.home.domain.use_cases.HomeUseCase
import com.home.ui.screens.navArgs
import com.home.ui.shared.PetDetailNavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PetDetailModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val firebaseFirestore: FirebaseFirestore,
    private val userVerificationModel: UserVerificationModel,
    handle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(PetDetailState())

    private var postId by mutableIntStateOf(-1)

     var currentUser by mutableIntStateOf(-1)
    init {
        viewModelScope.launch {

            postId = handle.navArgs<PetDetailNavArgs>().id

            currentUser = userVerificationModel.userIdFlow.firstOrNull()?:-1

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
                                id = postId,
                                address = pet.address,
                                contactNumber = pet.contactNumber,
                                country = pet.country,
                                createdAt = pet.createdAt,
                                petAge = pet.petAge,
                                petBreed = pet.petBreed,
                                petGender = pet.petGender,
                                petName = pet.petName,
                                ownerName = pet.ownerName,
                                ownerId = pet.ownerId,
                                ownerPhoto = pet.ownerProfilePic,
                                petDesc = pet.petDesc,
                                petPhotos = pet.petPhoto ?: emptyList(),
                                petType = pet.petType,
                                petFavorite = pet.petFavorite,
                            )
                        }
                    }

                    is Resource.Error -> Log.d("AppError", "getPetsInfos: ${resource.message}")
                }
            }
        }
    }

     fun addFavorite(id: Int) {

        viewModelScope.launch {
            homeUseCase.addFavorite(id).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        homeUseCase.getPosts()
                        Log.d("AppSuccess", "addFavorite:${resource.data?.message} ")
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "addFavorite:${resource.data?.message} ")
                    }

                    else -> Unit
                }
            }
        }
    }

     fun removePets(id: Int) {

        viewModelScope.launch {
            homeUseCase.removeFavoriteHome(id).collect { resource ->
                when (resource) {

                    is Resource.Success -> {
                        homeUseCase.getPosts()
                        Log.d("AppSuccess", "removeFavorite:${resource.data?.message} ")
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "removeFavorite:${resource.data?.message} ")
                    }

                    else -> Unit
                }
            }
        }
    }

    suspend fun registeredToChat(userId:Int):Boolean{
        return try {
            val query = firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userId.toString()).get().await()

            query.exists()
        } catch (e: Exception) {
            false
        }
    }
}
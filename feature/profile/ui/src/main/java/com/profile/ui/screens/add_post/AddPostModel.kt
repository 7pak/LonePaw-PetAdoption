package com.profile.ui.screens.add_post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Resource
import com.core.database.dao.PetsDao
import com.core.network.profile_api.model.AddPostData
import com.core.network.profile_api.model.UpdateProfileData
import com.feature.profile.domain.use_cases.ProfileUseCase
import com.profile.ui.screens.navArgs
import com.profile.ui.shared.AddPostNavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val dao: PetsDao,
    private val handle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(AddPostState())

    private var _onPostAdded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onPostAdded: StateFlow<Boolean> = _onPostAdded


     var postId by mutableIntStateOf(-1)
         private set


    init {
        viewModelScope.launch {

            postId = handle.navArgs<AddPostNavArgs>().id

            if (postId != -1) {
                getPetInfo(postId)
            }
        }

    }


    fun updateState(newState: AddPostState) {
        state = newState.copy()
    }

    fun resetOnPostAdded() {
        _onPostAdded.value = false
    }

    fun addPost() {

        viewModelScope.launch {
            Log.d("AddPet", "addPost: $state")
            profileUseCase.addPost(
                addPostData = AddPostData(
                    categoryId = state.categoryId!!,
                    petAge = state.petAge!!,
                    petBreed = state.petBreed,
                    petDesc = state.petDesc,
                    petGender = state.petGender,
                    petName = state.petName,
                    petPhoto = state.petPhoto,
                    petType = state.petType
                )
            ).collect { resource ->

                when (resource) {

                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        _onPostAdded.value = true
                        Log.d("AppSuccess", "addPost: ${resource.data?.status?.description}")
                        Log.d(
                            "AppSuccess",
                            "addPost: ${resource.data?.status}---${resource.message}"
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(error = resource.data?.status?.description)
                        Log.d("AppError", "addPost: ${resource.data?.status?.description}")
                        Log.d(
                            "AppError",
                            "addPost: ${resource.data?.status?.value}---${resource.message}"
                        )
                    }
                }
            }
        }
    }

    fun updatePost() {

        viewModelScope.launch {
            profileUseCase.updatePost(
                id = postId,
                addPostData = AddPostData(
                    categoryId = state.categoryId!!,
                    petAge = state.petAge!!,
                    petBreed = state.petBreed,
                    petDesc = state.petDesc,
                    petGender = state.petGender,
                    petName = state.petName,
                    petPhoto = state.petPhoto,
                    petType = state.petType
                )
            ).collect { resource ->

                when (resource) {

                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        Log.d("AppSuccess", "statePostUpate:$state ")

                        _onPostAdded.value = true
                        Log.d("AppSuccess", "updatePost: ${resource.data?.status?.description}")
                        Log.d(
                            "AppSuccess",
                            "updateProfile: ${resource.data?.status}---${resource.message}"
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(error = "${resource.data?.status?.description}")
                        Log.d("AppError", "updatePost: ${resource.data?.status?.description}")
                        Log.d(
                            "AppError",
                            "updatePost: ${resource.data?.status?.value}---${resource.message}"
                        )
                    }
                }
            }
        }
    }


     fun getPetInfo(id: Int) {

        viewModelScope.launch {
            val myPost = dao.getPetById(id)
            updateState(
                state.copy(
                    petName = myPost.petName,
                    categoryId = myPost.category,
                    petDesc = myPost.petDesc,
                    petAge = myPost.petAge,
                    petBreed = myPost.petBreed,
                    petGender = myPost.petGender,
                    petPhoto = myPost.petPhoto.toUri(),
                    petType = myPost.petType
                )
            )
        }
    }
}

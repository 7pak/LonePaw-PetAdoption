package com.home.ui.screens.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.Constants.SHARED_CHAT_COLLECTION
import com.core.common.Constants.USERS_COLLECTION
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.core.database.dao.PetsDao
import com.core.database.model.toPetInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.home.domain.use_cases.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeModel @Inject constructor(
    application: Application,
    private val homeUseCase: HomeUseCase,
    private val dao: PetsDao,
    private val userVerificationModel: UserVerificationModel,
    private val firebaseFirestore: FirebaseFirestore
) : AndroidViewModel(application) {

    private val context = application

    private val _categoryId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val categoryId: StateFlow<Int> = _categoryId

    var state by mutableStateOf(PetStates())
        private set

    private var currentUserId by mutableIntStateOf(-1)

    private var _hasUnseenMessage = MutableStateFlow(false)
    val hasUnseenMessage: StateFlow<Boolean> = _hasUnseenMessage

    init {
        viewModelScope.launch {
            val userId = userVerificationModel.userIdFlow.firstOrNull()
            currentUserId = userId ?: -1

            getProfile()
            _hasUnseenMessage.value = hasUnseenMessage()
        }
    }

    fun updateCategory(categoryId: Int) {
        _categoryId.value = categoryId
    }

    fun updateState(newState: PetStates) {
        state = newState
    }

    fun getPetsInfos() {

        viewModelScope.launch {
            homeUseCase.getPosts().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                        state = state.copy(petsListing = dao.getAllPets().firstOrNull())
                    }

                    is Resource.Success -> {
                        updateState(state.copy(selectedCategory = "", searchQuery = ""))
                        dao.getAllPets().collectLatest {
                            state = state.copy(petsListing = it)
                        }

                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getPest: ${resource.message}")
                        state = state.copy(
                            petsListing = dao.getAllPets().firstOrNull(),
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    fun getSearchResult(query: String) {
        updateState(state.copy(selectedCategory = ""))
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
                        state.copy(errorMessage = resource.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getPostsCategories() {
        updateState(state.copy(searchQuery = ""))
        viewModelScope.launch {
            if (categoryId.value == -1) getPetsInfos() else {
                homeUseCase.getPostsCategory(categoryId.value).onEach { resource ->
                    state = when (resource) {
                        is Resource.Loading -> {
                            state.copy(isLoading = resource.isLoading)
                        }

                        is Resource.Success -> {
                            state.copy(petsListing = resource.data?.data?.map { it.toPetInfo() })
                        }

                        is Resource.Error -> {
                            Log.d("AppError", "getSearchInfo: ${resource.message}")
                            state.copy(errorMessage = resource.message)
                        }
                    }
                }.launchIn(viewModelScope)
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
                        Log.d("AppSuccess", "addFavorite:${resource.data?.message} ")

                    }

                    is Resource.Error -> {
                        Log.d("AppError", "addFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }

    internal fun removeFavorite(id: Int) {

        viewModelScope.launch {
            homeUseCase.removeFavoriteHome(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Log.d("AppSuccess", "removeFavorite:${resource.data?.message} ")
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "removeFavorite:${resource.data?.message} ")
                    }
                }
            }
        }
    }

    internal fun getProfile() {

        viewModelScope.launch {

            homeUseCase.getProfileHome().collectLatest { resource ->

                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {

                        resource.data?.let {
                            state = state.copy(
                                profileName = it.name,
                                profilePic = it.profilePic ?: "",
                                profileCountry = it.country
                            )
                        }


                        if (currentUserId != -1) {

                            if (state.profilePic.isNotEmpty()) {

                                firebaseFirestore.document("$USERS_COLLECTION/$currentUserId")
                                    .set(
                                        mapOf(
                                            "name" to state.profileName,
                                            "profilePic" to state.profilePic
                                        ),
                                        SetOptions.merge()
                                    )
                            } else {
                                firebaseFirestore.document("$USERS_COLLECTION/$currentUserId")
                                    .set(
                                        mapOf(
                                            "name" to state.profileName,
                                            "profilePic" to ""
                                        ),
                                        SetOptions.merge()
                                    )
                            }
                        }


                    }

                    is Resource.Error -> {
                        resource.message?.let {
                            state = state.copy(errorMessage = it)
                        }
                        Log.d("AppError", "getProfile: ${state.errorMessage}")
                    }
                }
            }
        }
    }


    private suspend fun hasUnseenMessage(): Boolean {
        val querySnapshot = firebaseFirestore
            .collection("$USERS_COLLECTION/$currentUserId/$SHARED_CHAT_COLLECTION")
            .whereEqualTo("messageStatus", "UNSEEN")
            .get()
            .await()

        return !querySnapshot.isEmpty
    }

    fun updateHasUnseenMessage() {
        viewModelScope.launch {
            val result = hasUnseenMessage()
            _hasUnseenMessage.value = result
        }
    }
}
package com.auth.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.use_cases.AuthUseCase
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.core.network.auth_api.models.RegisterUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userVerificationModel: UserVerificationModel
) : ViewModel() {

    private val _state: MutableStateFlow<RegisterStates> = MutableStateFlow(RegisterStates())
    val state: StateFlow<RegisterStates> = _state

    val countriesList = getCountriesList()
    private var _selectedCountry:MutableStateFlow<Country> = MutableStateFlow(Country())
    val selectedCountry:StateFlow<Country> = _selectedCountry


    fun registerUser() {

        viewModelScope.launch {
            authUseCase.registerUser(
                RegisterUserData(
                    name = state.value.fullName,
                    username = state.value.username,
                    email = state.value.email,
                    password = state.value.password,
                    country = selectedCountry.value.fullName,
                    contactNumber = getContactNumber(),
                    address = state.value.address
                )
            )
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val token = resource.data?.body()?.data?.token
                            val userId = resource.data?.body()?.data?.userId
                            updateStatus(
                                _state.value.copy(
                                    success = resource.data?.body()?.message
                                )
                            )
                            token?.let {
                                userVerificationModel.saveToken(it)
                            }
                            userId?.let {
                                userVerificationModel.saveUserId(it)
                            }

                        }

                        is Resource.Loading -> updateStatus(_state.value.copy(isLoading = resource.isLoading))
                        is Resource.Error -> updateStatus(_state.value.copy(error = resource.message))
                    }
                }
        }
    }

    fun updateStatus(state: RegisterStates) {
        _state.value = state
    }

    fun updateCountry(country: Country){
        _selectedCountry.value = country
    }

    private fun getContactNumber():String{
        return selectedCountry.value.code+state.value.contactNumber
    }
}
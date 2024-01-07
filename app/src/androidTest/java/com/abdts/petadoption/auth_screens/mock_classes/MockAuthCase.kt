package com.abdts.petadoption.auth_screens.mock_classes

import com.auth.domain.repository.AuthRepository
import com.auth.domain.use_cases.AuthUseCase
import com.core.common.Resource
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MockAuthCase(private val authhRepository: AuthRepository) :
    AuthUseCase(authRepository = authhRepository) {

    override operator fun invoke(loginUserData: LoginUserData): Flow<Resource<LoginUserResponse>> {
        // Create a mock successful response for testing
        return flow {
            if (loginUserData.username_email == "az_bod" && loginUserData.password == "12345678a") {
                emit(
                    Resource.Success(
                        LoginUserResponse(
                            message = "Mock Success",
                            status = 202,
                            data = loginUserData.copy(token = "fake-token")
                        )
                    )
                )
            } else {
                emit(
                    Resource.Error("An unexpected error occurred")
                )
            }
        }
    }

    override operator fun invoke(registerUserData: RegisterUserData): Flow<Resource<RegisterUserResponse>> {
        // Create a mock successful response for testing
        return flow {
            if (registerUserData.username == "az_bod" || registerUserData.password == "az@gamil.com") {
                emit(
                    Resource.Error("User Already Registered")
                )
            } else {
                emit(

                    Resource.Success(
                        RegisterUserResponse(
                            message = "Mock Success",
                            status = 202,
                            data = registerUserData.copy(token = "fake-token")
                        )
                    )
                )

            }
        }
    }
}
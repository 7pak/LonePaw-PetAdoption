package com.core.network.di

import android.content.Context
import android.util.Log
import com.core.common.Constants.BASE_URL
import com.core.network.auth_api.AuthApi
import com.core.network.auth_api.AuthDataProvider
import com.core.network.chat_api.ChatApi
import com.core.network.chat_api.ChatDataProvider
import com.core.network.home_api.HomeApi
import com.core.network.home_api.HomeDataProvider
import com.core.network.profile_api.ProfileApi
import com.core.network.profile_api.ProfileDataProvider
import com.core.network.profile_api.UploadProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenFlow: Flow<String?>): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val token = runBlocking {
            tokenFlow.firstOrNull()
        }
        val tokenInterceptor = Interceptor { chain ->
            Log.d("AppToken", "provideOkHttpClient: $token")
            val request = chain.request().newBuilder()
                .apply {
                    token?.let {
                        addHeader("Accept", "application/json")
                        addHeader("Content-Type", "application/json")
                    }
                }
                .build()
            chain.proceed(request)
        }
        builder.addNetworkInterceptor(tokenInterceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthDataProvider(authApi: AuthApi, tokenFlow: Flow<String?>): AuthDataProvider {
        return AuthDataProvider(authApi, tokenFlow = tokenFlow)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeDataProvider(
        homeApi: HomeApi,
        tokenFlow: Flow<String?>,
        profileApi: ProfileApi
    ): HomeDataProvider {
        return HomeDataProvider(homeApi, tokenFlow, profileApi = profileApi)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatDataProvider(chatApi: ChatApi, tokenFlow: Flow<String?>): ChatDataProvider {
        return ChatDataProvider(chatApi, tokenFlow)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }


    @Provides
    @Singleton
    fun provideProfileDataProvider(
        profileApi: ProfileApi,
        tokenFlow: Flow<String?>,
        context: Context,
        uploadProfileApi: UploadProfileApi,
        homeApi: HomeApi
    ): ProfileDataProvider {
        return ProfileDataProvider(
            profileApi,
            tokenFlow = tokenFlow,
            context = context,
            uploadProfileApi = uploadProfileApi,
            homeApi = homeApi
        )
    }

    @Provides
    @Singleton
    fun provideKtorClient(
        tokenFlow: Flow<String?>
    ): HttpClient {
        val token = runBlocking {
            tokenFlow.firstOrNull() // Fetch the token asynchronously
        }

        return HttpClient {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorFitLogger", "log: $message")
                    }

                }
                level = LogLevel.ALL
            }
            token?.let {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $it")
                }
            }

        }
    }

    @Provides
    @Singleton
    fun provideUpdateApi(httpClient: HttpClient): UploadProfileApi {

        return Ktorfit.Builder()
            .baseUrl(BASE_URL) // there was /
            .httpClient(httpClient)
            .build()
            .create()
    }

}
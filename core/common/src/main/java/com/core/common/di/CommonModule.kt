package com.core.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.core.common.app_data.AppDataStoreManager
import com.core.common.Constants
import com.core.common.UserVerificationModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(Constants.DATA_STORE_NAME)
        }

    @Provides
    @Singleton
    fun providesAppDataStoreManger(dataStore: DataStore<Preferences>):AppDataStoreManager = AppDataStoreManager(dataStore)


    @Provides
    @Singleton
    fun provideTokenFlow(appDataStoreManager: AppDataStoreManager): Flow<String?> {
        return appDataStoreManager.currentToken
    }

    @Provides
    @Singleton
    fun provideUserVerficationModel(appDataStoreManager: AppDataStoreManager) = UserVerificationModel(appDataStoreManager)
}
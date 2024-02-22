package com.core.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.core.common.app_data.AppDataStoreManager
import com.core.common.Constants
import com.core.common.utls.UserVerificationModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
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
    fun provideFireStore() =
        Firebase.firestore


    @Provides
    @Singleton
    fun provideFireStorage():StorageReference{
        return Firebase.storage.reference
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
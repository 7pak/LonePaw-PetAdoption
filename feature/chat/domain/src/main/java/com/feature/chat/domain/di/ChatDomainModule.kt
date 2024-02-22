package com.feature.chat.domain.di

import android.content.Context
import com.feature.chat.domain.use_cases.ChatUseCase
import com.feature.chat.domain.use_cases.DeleteChat
import com.feature.chat.domain.use_cases.GetContacts
import com.feature.chat.domain.use_cases.GetMessages
import com.feature.chat.domain.use_cases.GetRecipientProfile
import com.feature.chat.domain.use_cases.SearchContact
import com.feature.chat.domain.use_cases.SendMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatDomainModule {

    @Provides
    @Singleton
    fun provideChatCase(firestore: FirebaseFirestore,@ApplicationContext context: Context,fireStorage: StorageReference):ChatUseCase{
        return ChatUseCase(
            sendMessage = SendMessage(firestore = firestore, fireStorage = fireStorage,context = context),
            getMessages = GetMessages(firestore = firestore,fireStorage = fireStorage),
            getContacts = GetContacts(firestore = firestore,fireStorage = fireStorage),
            searchContact = SearchContact(firestore = firestore),
            deleteChat = DeleteChat(firestore = firestore),
            getRecipientProfile = GetRecipientProfile(firebaseFirestore = firestore,fireStorage = fireStorage)
        )
    }
}
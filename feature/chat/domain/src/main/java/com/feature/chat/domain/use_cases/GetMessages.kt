package com.feature.chat.domain.use_cases

import com.core.common.Constants.CHAT_CHANNELS_COLLECTION
import com.core.common.Constants.MESSAGES_COLLECTION
import com.core.common.Constants.SHARED_CHAT_COLLECTION
import com.core.common.Constants.USERS_COLLECTION
import com.core.common.utls.Resource
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.MessageStatus
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GetMessages @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    operator fun invoke(
        currentUser: String,
        recipientId: String
    ): Flow<Resource<List<ChatContent>>> {
        return callbackFlow {
            if (!hasChat(currentUser)) return@callbackFlow
            this.trySend(Resource.Loading(isLoading = true))
            val messagesContent = mutableListOf<ChatContent>()
            val chatId = getChatId(currentUser = currentUser, recipientId = recipientId)
            val existingMessages = loadExistingMessages(chatId)
            val listenerRegistration = firestore.document("$CHAT_CHANNELS_COLLECTION/$chatId")
                .collection(MESSAGES_COLLECTION)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    launch(Dispatchers.IO) {
                        changeMessageStatus(currentUser = currentUser, recipientId = recipientId)
                        value?.documentChanges?.forEach { documentChange ->

                            val message = documentChange.document.toObject(ChatContent::class.java)

                            when (documentChange.type) {
                                DocumentChange.Type.ADDED -> {
                                    // Handle new messages
                                    if (existingMessages.contains(message)) {
                                        messagesContent.add(
                                            message
                                        )
                                    } else {
                                        messagesContent.add(
                                            0,
                                            message
                                        )
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    // Handle modified messages (if needed)
                                }

                                DocumentChange.Type.REMOVED -> {
                                    // Handle removed messages (if needed)
                                }
                            }
                        }

                        this@callbackFlow.trySend(Resource.Success(messagesContent.toList())).isSuccess
                    }
                }

            awaitClose { listenerRegistration.remove() }
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getChatId(currentUser: String, recipientId: String): String {
        return suspendCoroutine { continuation ->
            firestore.document("$USERS_COLLECTION/$currentUser")
                .collection(SHARED_CHAT_COLLECTION).document(recipientId).get()
                .addOnSuccessListener { document ->
                    val chatId = document["chatId"] as String
                    continuation.resume(chatId) // Resume the coroutine with the retrieved username
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception) // Resume with exception if retrieval fails
                }
        }
    }

    private suspend fun loadExistingMessages(chatId: String): List<ChatContent> {
        return try {
            val querySnapshot = firestore.document("$CHAT_CHANNELS_COLLECTION/$chatId")
                .collection(MESSAGES_COLLECTION)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.documents.mapNotNull {
                it.toObject(ChatContent::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun hasChat(currentUser: String): Boolean {
        return try {
            val query = firestore.document("$USERS_COLLECTION/$currentUser")
                .collection(SHARED_CHAT_COLLECTION).limit(1).get().await()

            !query.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun changeMessageStatus(currentUser: String, recipientId: String) {
        val task = firestore.document("$USERS_COLLECTION/$currentUser")
            .collection(SHARED_CHAT_COLLECTION)
            .document(recipientId)
            .get()

        val document = task.await()
        val messageRecipientId = document.get("recipientId") as String?

        if (messageRecipientId == currentUser) {
            firestore.document("$USERS_COLLECTION/$currentUser")
                .collection(SHARED_CHAT_COLLECTION).document(recipientId)
                .update("messageStatus", MessageStatus.SEEN)
        }
    }
}
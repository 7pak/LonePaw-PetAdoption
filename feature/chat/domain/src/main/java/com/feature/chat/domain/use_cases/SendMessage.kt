package com.feature.chat.domain.use_cases

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.core.common.Constants
import com.core.common.Constants.CHAT_CHANNELS_COLLECTION
import com.core.common.Constants.MESSAGES_COLLECTION
import com.core.common.Constants.USERS_COLLECTION
import com.core.common.utls.Resource
import com.core.common.utls.toByteArray
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.ImageMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SendMessage @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val fireStorage: StorageReference,
    private val context: Context
) {

    operator fun invoke(
        chatContent: ChatContent
    ): Flow<Resource<Unit>> {
        return flow {
            if (chatContent.senderId.isNullOrEmpty() || chatContent.recipientId.isNullOrEmpty()) return@flow

            val senderName = getUserName(chatContent.senderId)
            val recipientName = getUserName(chatContent.recipientId)

            var fullChatContent =
                chatContent.copy(senderName = senderName, recipientName = recipientName)

            if (fullChatContent.type == ImageMessage.IMAGE) {
                val byteArray = fullChatContent.message.toUri().toByteArray(context = context)
                if (byteArray != null && byteArray.isNotEmpty()) {
                    val uniquePicName = UUID.nameUUIDFromBytes(byteArray)
                    val ref = fireStorage.child("${chatContent.senderId}/images/$uniquePicName.jpg")

                    ref.putBytes(byteArray).onSuccessTask {
                        ref.downloadUrl.addOnSuccessListener {uri->
                            fullChatContent = fullChatContent.copy(message = uri.toString())
                            Log.d("AppSuccess", "invoke: $uri ")
                        }

                    }.await()
                }
            }
            Log.d("AppSuccess", "fullChat:$fullChatContent ")

            try {
                starChatChannel(
                    chatContent = fullChatContent
                )
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun starChatChannel(
        chatContent: ChatContent
    ) {
        val newChatId = firestore.collection(USERS_COLLECTION).document().id

        firestore.document("$USERS_COLLECTION/${chatContent.senderId}")
            .collection(Constants.SHARED_CHAT_COLLECTION)
            .document(chatContent.recipientId).get().addOnSuccessListener {
                if (it.exists()) {
                    val chatId = it["chatId"] as String

                    firestore.document("$USERS_COLLECTION/${chatContent.senderId}")
                        .collection(Constants.SHARED_CHAT_COLLECTION)
                        .document(chatContent.recipientId).set(chatContent.copy(chatId = chatId))

                    firestore.document("$USERS_COLLECTION/${chatContent.recipientId}")
                        .collection(Constants.SHARED_CHAT_COLLECTION)
                        .document(chatContent.senderId).set(chatContent.copy(chatId = chatId))

                    sendMessage(chatContent.copy(chatId = chatId))
                } else {
                    firestore.document("$USERS_COLLECTION/${chatContent.senderId}")
                        .collection(Constants.SHARED_CHAT_COLLECTION)
                        .document(chatContent.recipientId).set(chatContent.copy(chatId = newChatId))

                    firestore.document("$USERS_COLLECTION/${chatContent.recipientId}")
                        .collection(Constants.SHARED_CHAT_COLLECTION)
                        .document(chatContent.senderId).set(chatContent.copy(chatId = newChatId))

                    sendMessage(chatContent.copy(chatId = newChatId))
                }
            }
    }

    private fun sendMessage(chatContent: ChatContent) {
        firestore.collection(CHAT_CHANNELS_COLLECTION).document(chatContent.chatId)
            .collection(MESSAGES_COLLECTION).document().set(chatContent)
    }

    private suspend fun getUserName(userId: String): String {
        return suspendCoroutine { continuation ->
            firestore.document("$USERS_COLLECTION/$userId").get()
                .addOnSuccessListener { document ->
                    val userName = document["name"] as String
                    continuation.resume(userName) // Resume the coroutine with the retrieved username
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception) // Resume with exception if retrieval fails
                }
        }
    }
}
package com.feature.chat.domain.use_cases

import com.core.common.Constants
import com.core.common.Constants.USERS_COLLECTION
import com.core.common.utls.Resource
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.User
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
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GetContacts @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    operator fun invoke(currentUserId: Int): Flow<Resource<List<Pair<User, ChatContent>>>> {
        return callbackFlow {
            if (currentUserId == -1) return@callbackFlow
            val usersMap = mutableMapOf<User, ChatContent>()
            this.trySend(Resource.Loading(isLoading = true))
            val listenerRegistration =
                firestore.document("$USERS_COLLECTION/$currentUserId")
                    .collection(Constants.SHARED_CHAT_COLLECTION)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        launch(Dispatchers.IO) {
                            value?.documentChanges?.forEach { documentChange ->
                                val userId = documentChange.document.id

                                val user = getUserById(userId)
                                when (documentChange.type) {
                                    DocumentChange.Type.ADDED -> {
                                        // Handle new user
                                        usersMap[User(
                                            id = userId,
                                            name = user.name,
                                            profilePic = user.profilePic
                                        )] =
                                            documentChange.document.toObject(ChatContent::class.java)

                                    }

                                    DocumentChange.Type.MODIFIED -> {
                                        val modifiedChatContent =
                                            documentChange.document.toObject(ChatContent::class.java)

                                        val existingPair = usersMap.entries.find { it.key.id == userId }


                                        if (existingPair != null) {
                                            // Remove the existing entry
                                            usersMap.remove(existingPair.key)

                                            // Insert the modified entry at the beginning of the list
                                            usersMap[User(
                                                id = userId,
                                                name = user.name,
                                                profilePic = user.profilePic
                                            )] = modifiedChatContent
                                        }
                                    }

                                    DocumentChange.Type.REMOVED -> {
                                        val removedUserId = documentChange.document.id
                                        usersMap.remove(usersMap.keys.find { it.id == removedUserId })
                                    }
                                }
                                // Check if all async operations are completed
                                if (usersMap.size == value.documentChanges.size) {
                                    // All async operations are completed, emit the result
                                    this@callbackFlow.trySend(Resource.Success(usersMap.toList())).isSuccess
                                }
                            }
                        }
                    }

            awaitClose { listenerRegistration.remove() }



        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getUserById(userId: String): User {
        return suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION).document(userId).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(IllegalArgumentException("User not found"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}
package com.feature.chat.domain.use_cases

import com.core.common.Constants
import com.core.common.utls.Resource
import com.feature.chat.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GetRecipientProfile @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val fireStorage: StorageReference
) {

    operator fun invoke(recipientId:Int): Flow<Resource<User>> {
        return flow {
            if (recipientId < 0) return@flow
            emit(Resource.Loading(true))
            val recipient: User = getUserById(recipientId.toString())
            emit(Resource.Success(recipient))

        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getUserById(recipientId: String): User {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection(Constants.USERS_COLLECTION).document(recipientId).get()
                .addOnSuccessListener { document ->
                    val recipient = document.toObject(User::class.java)
                    if (recipient != null) {
                        continuation.resume(recipient)
                    } else {
                        continuation.resumeWithException(IllegalArgumentException("Recipient not found"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}

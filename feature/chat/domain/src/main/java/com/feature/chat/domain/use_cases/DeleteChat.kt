package com.feature.chat.domain.use_cases

import com.core.common.Constants
import com.core.common.Constants.USERS_COLLECTION
import com.core.common.utls.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteChat @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    operator fun invoke(
        currentUserId:String,
        recipientId:String,
    ): Flow<Resource<Unit>> {
        return flow {
            if (currentUserId.isNullOrEmpty() || recipientId.isNullOrEmpty()) return@flow

            try {
                deleteChat(currentUserId = currentUserId,recipientId = recipientId)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    private suspend fun deleteChat(currentUserId: String,recipientId: String){
        firestore.document("$USERS_COLLECTION/${currentUserId}")
            .collection(Constants.SHARED_CHAT_COLLECTION).document(recipientId).delete().await()
    }
}
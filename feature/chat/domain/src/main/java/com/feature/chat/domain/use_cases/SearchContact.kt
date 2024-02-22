package com.feature.chat.domain.use_cases

import android.util.Log
import com.core.common.Constants
import com.core.common.utls.Resource
import com.feature.chat.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchContact @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    operator fun invoke(currentUserId: String,searchQuery: String): Flow<Resource<List<User>>> {
        return flow {

            try {
                emit(Resource.Success(findUsers(currentUserId, searchQuery)))
            }catch (e:Exception){
               emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private suspend fun findUsers(currentUserId: String,searchQuery:String):List<User>{
        val usersList = mutableListOf<User>()

        val querySnapshot = firestore
            .collection("${Constants.USERS_COLLECTION}/$currentUserId/${Constants.SHARED_CHAT_COLLECTION}")
            .whereEqualTo("name", searchQuery)

        querySnapshot.get().addOnSuccessListener { documents ->
            documents.forEach {document->
                val user = document.toObject(User::class.java)
                usersList.add(user)
            }
        }.addOnFailureListener { exception ->
            Log.e("AppError", "Error getting documents: ", exception)
        }.await()

        return usersList
    }
}
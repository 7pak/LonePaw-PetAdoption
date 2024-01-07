package com.profile.ui.screens.add_post

import android.net.Uri

data class AddPostState(
    val categoryId: Int? = null,
    val petAge: Int? = null,
    val petBreed: String = "",
    val petDesc: String = "",
    val petGender: String = "",
    val petName: String = "",
    val petPhoto: Uri? = null,
    val petType: String = "",
    val isLoading:Boolean = false,
    val error:String?=null
)

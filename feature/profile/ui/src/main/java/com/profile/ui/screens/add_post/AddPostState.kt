package com.profile.ui.screens.add_post

data class AddPostState(
    val categoryId: Int? = null,
    val petAge: Int? = null,
    val petBreed: String = "",
    val petDesc: String = "",
    val petGender: String = "",
    val petName: String = "",
    val petPhoto: List<String>? = null,
    val petType: String = "",
    val isLoading:Boolean = false,
    val error:String?=null
)

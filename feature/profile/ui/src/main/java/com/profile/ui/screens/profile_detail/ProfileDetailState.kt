package com.profile.ui.screens.profile_detail

data class ProfileDetailState(
    val address: String = "",
    val contactNumber: String = "",
    val country: String = "",
    val email: String = "",
    val name: String = "",
    val username: String = "",
    val isLoading:Boolean = false,
    val error:String = ""
)

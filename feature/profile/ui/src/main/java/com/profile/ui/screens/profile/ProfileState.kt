package com.profile.ui.screens.profile

data class ProfileState(
    val profileName:String="",
    val profilePic:String="",
    val isLoading:Boolean = false,
    val error:String = ""
)

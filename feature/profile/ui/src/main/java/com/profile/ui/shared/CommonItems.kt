package com.profile.ui.shared

interface ProfileScreenNavigator {
    fun navigateToProfileDetailScreen()
    fun navigateToMyPetsScreen()
    fun navigateToAddPostScreen(id: Int? = -1)
    fun navigateToFavoriteScreen()
    fun navigateToLoginScreen()
}

class AddPostNavArgs(val id: Int)
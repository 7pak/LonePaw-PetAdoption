package com.home.ui.shared

interface HomeScreenNavigator {
    fun navigateToProfileScreen()
    fun navigateToPetDetailScreen(id:Int)
    fun navigateToChatScreen(id: Int)
    fun navigateToContactScreen()
}

 class PetDetailNavArgs(val id: Int)
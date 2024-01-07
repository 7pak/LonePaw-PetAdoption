package com.home.ui.shared

interface HomeScreenNavigator {
    fun navigateToProfileScreen()
    fun navigateToPetDetailScreen(id:Int)
}

 class PetDetailNavArgs(val id: Int)
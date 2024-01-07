package com.abdts.petadoption.navigation

import androidx.navigation.NavController
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.screens.destinations.LoginScreenDestination
import com.auth.ui.screens.destinations.SignUpScreenDestination
import com.home.ui.screens.destinations.HomeScreenDestination
import com.home.ui.screens.destinations.PetDetailScreenDestination
import com.home.ui.shared.HomeScreenNavigator
import com.profile.ui.screens.destinations.AddPetScreenDestination
import com.profile.ui.screens.destinations.FavoriteScreenDestination
import com.profile.ui.screens.destinations.MyPetsScreenDestination
import com.profile.ui.screens.destinations.ProfileDetailScreenDestination
import com.profile.ui.screens.destinations.ProfileScreenDestination
import com.profile.ui.shared.ProfileScreenNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

class CoreFeatureNavigator(
    private val navController: NavController
) : AuthScreenNavigator,HomeScreenNavigator,ProfileScreenNavigator{
    override fun navigateToProfileDetailScreen() {
        navController.navigate(ProfileDetailScreenDestination)
    }

    override fun navigateToMyPetsScreen() {
        navController.navigate(MyPetsScreenDestination)
    }

    override fun navigateToAddPostScreen(id: Int?) {
        navController.navigate(AddPetScreenDestination(id?:-1))
    }

    override fun navigateToFavoriteScreen() {
        navController.navigate(FavoriteScreenDestination)
    }

    override fun navigateToLoginScreen() {
        navController.navigate(LoginScreenDestination)
    }

    override fun navigateToSignUpScreen() {
        navController.navigate(SignUpScreenDestination)
    }

    override fun navigateToHomeScreen() {
        navController.navigate(HomeScreenDestination) {
            popUpTo(navController.graph.startDestinationId) { // Exclude up to the start destination
                inclusive = false
            }
        }
    }

    override fun navigateToProfileScreen() {
        navController.navigate(ProfileScreenDestination)
    }

    override fun navigateToPetDetailScreen(id:Int) {
        navController.navigate(PetDetailScreenDestination(id))
    }
}
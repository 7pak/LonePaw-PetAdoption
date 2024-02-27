package com.abdts.petadoption.navigation

import android.util.Log
import com.auth.ui.screens.destinations.EmailVerificationScreenDestination
import com.auth.ui.screens.destinations.LoginScreenDestination
import com.auth.ui.screens.destinations.OtpVerificationScreenDestination
import com.auth.ui.screens.destinations.PasswordResetScreenDestination
import com.auth.ui.screens.destinations.SignUpScreenDestination
import com.feature.chat.ui.screens.destinations.ChatScreenDestination
import com.feature.chat.ui.screens.destinations.ContactsScreenDestination
import com.home.ui.screens.destinations.HomeScreenDestination
import com.home.ui.screens.destinations.PetDetailScreenDestination
import com.profile.ui.screens.destinations.AddPetScreenDestination
import com.profile.ui.screens.destinations.FavoriteScreenDestination
import com.profile.ui.screens.destinations.MyPetsScreenDestination
import com.profile.ui.screens.destinations.ProfileDetailScreenDestination
import com.profile.ui.screens.destinations.ProfileScreenDestination
import com.profile.ui.screens.destinations.ResetPasswordScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

class NavGraphs(private val token: String) {


    val auth = object : NavGraphSpec {
        override val route: String
            get() = "auth"
        override val startRoute: Route
            get() = LoginScreenDestination
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf<DestinationSpec<*>>(
                LoginScreenDestination,
                SignUpScreenDestination,
                EmailVerificationScreenDestination,
                OtpVerificationScreenDestination,
                PasswordResetScreenDestination
            )
                .associateBy { it.route }

    }

    val home = object : NavGraphSpec {
        override val route: String
            get() = "home"
        override val startRoute: Route
            get() = HomeScreenDestination
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf<DestinationSpec<*>>(HomeScreenDestination, PetDetailScreenDestination)
                .associateBy { it.route }

    }

    val profile = object : NavGraphSpec {
        override val route: String
            get() = "profile"
        override val startRoute: Route
            get() = ProfileScreenDestination
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf<DestinationSpec<*>>(
                ProfileScreenDestination,
                ProfileDetailScreenDestination,
                MyPetsScreenDestination,
                FavoriteScreenDestination,
                AddPetScreenDestination,
                ResetPasswordScreenDestination
            )
                .associateBy { it.route }

    }

    val chat = object : NavGraphSpec {
        override val route: String
            get() = "chat"
        override val startRoute: Route
            get() = ContactsScreenDestination
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = listOf<DestinationSpec<*>>(
                ContactsScreenDestination,
                ChatScreenDestination
            )
                .associateBy { it.route }

    }

    val root = object : NavGraphSpec {
        override val route: String
            get() = "root"
        override val startRoute: Route
            get() = getStartedDestination()
        override val destinationsByRoute: Map<String, DestinationSpec<*>>
            get() = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs: List<NavGraphSpec>
            get() = listOf(auth, home, profile, chat)
    }

    private fun getStartedDestination(): NavGraphSpec {
        Log.d("AppToken", "getStartedDestination:$token ")
        if (token.isNotEmpty()) {
            return home
        }

        return auth
    }

}
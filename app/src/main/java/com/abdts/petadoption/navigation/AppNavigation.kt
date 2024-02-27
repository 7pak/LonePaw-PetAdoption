package com.abdts.petadoption.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency


@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    token: String
) {
    DestinationsNavHost(
        modifier = modifier,
        navController = navController,
        navGraph = NavGraphs(token).root,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
        })
}
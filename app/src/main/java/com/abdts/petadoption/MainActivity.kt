package com.abdts.petadoption

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.abdts.petadoption.navigation.AppNavigation
import com.abdts.petadoption.ui.theme.PetAdoptionTheme
import com.core.common.UserVerificationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userVerificationModel:UserVerificationModel
    private var currentToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            currentToken = userVerificationModel.tokenFlow.firstOrNull()
            Log.d(
                "AppToken",
                " Collected token in MAINaCTIVITY :${currentToken} "
            )

            setContent {

                val navController = rememberNavController()
                PetAdoptionTheme {
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        token = currentToken?:""
                    )
                }
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content))
            { view, insets ->
                val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                view.updatePadding(bottom = bottom) //complete on the manifests--> activity--> windowInput.. (adjustResize)
                insets
            }
        }
    }
}

package com.abdts.petadoption

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.abdts.petadoption.navigation.AppNavigation
import com.abdts.petadoption.ui.theme.PetAdoptionTheme
import com.core.common.utls.UserVerificationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userVerificationModel: UserVerificationModel
    private var currentToken: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch(Dispatchers.Main) {
            currentToken = userVerificationModel.tokenFlow.firstOrNull()

            setContent {

                val navController = rememberNavController()
                PetAdoptionTheme {
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        token = currentToken ?: ""
                    )
                }
            }

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content))
            { view, insets ->
                val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                view.updatePadding(bottom = bottom)
                insets
            }
        }
    }
}




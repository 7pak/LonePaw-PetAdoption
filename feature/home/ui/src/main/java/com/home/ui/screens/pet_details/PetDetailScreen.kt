package com.home.ui.screens.pet_details

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.home.ui.screens.pet_details.itmes.ImageDetailItem
import com.home.ui.screens.pet_details.itmes.PetDetailsItem
import com.home.ui.shared.HomeScreenNavigator
import com.home.ui.shared.PetDetailNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination(navArgsDelegate = PetDetailNavArgs::class)
@Composable
fun PetDetailScreen(
    petDetailModel: PetDetailModel = hiltViewModel(),
    navigator: HomeScreenNavigator
) {
    val state = petDetailModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        petDetailModel.getPetInfo()
    }
    Column(Modifier.fillMaxSize()) {
        ImageDetailItem(context = context, petInfo = state) {
            Log.d("AppSucc", "PetDetailScreen: id:${state.id}----boolean:$it")
            state.id?.let { id ->
                if (it) {
                    petDetailModel.removePets(id)
                } else {
                    petDetailModel.addFavorite(id)
                }
            }
        }
        PetDetailsItem(context = context, petInfo = state) { ownerId, phoneNumber ->
            scope.launch {
                if (ownerId != null && petDetailModel.registredTochat(ownerId)) {
                    navigator.navigateToChatScreen(ownerId)
                } else {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                String.format(
                                    "https://api.whatsapp.com/send?phone=%s&text=%s",
                                    phoneNumber,
                                    context.getString(R.string.whatsapp_message)
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}



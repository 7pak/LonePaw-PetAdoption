package com.home.ui.screens.pet_details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.home.ui.screens.pet_details.itmes.ImageDetailItem
import com.home.ui.screens.pet_details.itmes.PetDetailsItem
import com.home.ui.shared.PetDetailNavArgs
import com.home.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import java.lang.String
import com.core.common.R

@Destination(navArgsDelegate = PetDetailNavArgs::class)
@Composable
fun PetDetailScreen(petDetailModel: PetDetailModel = hiltViewModel()) {
    val state = petDetailModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        petDetailModel.getPetInfo()
    }
    Column (Modifier.fillMaxSize()){
        ImageDetailItem(context = context, petInfo = state) {
            state.id?.let { id ->
                if (it) {
                    petDetailModel.removePets(id)
                } else {
                    petDetailModel.addFavorite(id)
                }
            }
        }
        PetDetailsItem(context = context,petInfo = state){
            context.startActivity(
                Intent(
                    // on below line we are calling
                    // uri to parse the data
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        // on below line we are passing uri,
                        // message and whats app phone number.
                        String.format(
                            "https://api.whatsapp.com/send?phone=%s&text=%s",
                            it,
                            context.getString(R.string.whatsapp_message)
                        )
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetDetailScreenPreview() {
    PetAdoptionTheme {
       // PetDetailScreen()
    }
}


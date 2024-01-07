package com.profile.ui.screens.favorite

import com.core.database.model.PetInfo

data class FavoriteState(
    val petListings:List<PetInfo> = emptyList(),
    val isLoading:Boolean = false,
    val error:String = ""
)

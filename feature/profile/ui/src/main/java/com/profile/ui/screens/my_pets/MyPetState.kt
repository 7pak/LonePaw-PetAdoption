package com.profile.ui.screens.my_pets

import com.core.database.model.PetInfo

data class MyPetState(
    val petListings:List<PetInfo> = emptyList(),
    val isLoading:Boolean = false,
    val error:String = ""
)

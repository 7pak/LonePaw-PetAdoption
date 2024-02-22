package com.home.ui.screens.home

import com.core.database.model.PetInfo

data class PetStates(
    val petsListing:List<PetInfo>? = emptyList(),
    val profilePic:String = "",
    val profileName:String = "",
    val profileCountry:String = "",
    val isLoading:Boolean = false,
    val isRefreshing:Boolean = false,
    val searchQuery:String = "",
    val selectedCategory:String = "",
    val errorMessage:String? = ""
)

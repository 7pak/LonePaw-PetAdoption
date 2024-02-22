package com.feature.chat.domain.model

data class User(
    val id:String?=null,
    val name:String,
    val profilePic:String
){
    constructor() : this(null,"", "")
}

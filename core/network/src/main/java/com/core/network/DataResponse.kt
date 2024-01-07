package com.core.network

data class DataResponse<T: Any>(
    val data:T?=null,
    val message: String,
    val status: Int
)

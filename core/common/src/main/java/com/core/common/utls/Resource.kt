package com.core.common.utls

sealed class Resource<T>(val data:T?=null, val message:String?=null){
    class Loading<T>(val isLoading:Boolean = false): Resource<T>()
    class Success<T>(data: T?,message: String?=null): Resource<T>(data = data,message = message)
    class Error<T>(message: String): Resource<T>(message=message)
}

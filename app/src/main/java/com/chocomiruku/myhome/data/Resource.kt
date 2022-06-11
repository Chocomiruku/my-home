package com.chocomiruku.myhome.data

sealed class Resource<out T> {
    data class Success<T>(val data: T? = null) : Resource<T>()
    data class Failure(val errorMessage: String?) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
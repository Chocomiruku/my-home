package com.chocomiruku.myhome.util

import com.chocomiruku.myhome.R

val colors = listOf(
    R.color.profile_pic_1, R.color.profile_pic_2, R.color.profile_pic_3, R.color.profile_pic_4,
    R.color.profile_pic_5, R.color.profile_pic_6
)

enum class AuthState {
    LOADING,
    SIGN_IN_ERROR,
    SIGN_IN_SUCCESS,
    SIGN_UP_CONTRACT_ERROR,
    SIGN_UP_ERROR,
    SIGN_UP_SUCCESS
}

fun generateRandomColor(): Int {
    return colors.random()
}
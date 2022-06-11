package com.chocomiruku.myhome.util

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 6 && password.matches("^[A-Za-z0-9]+\$".toRegex())
}

fun isContractNumberValid(contract: String): Boolean {
    return contract.length >= 7
}
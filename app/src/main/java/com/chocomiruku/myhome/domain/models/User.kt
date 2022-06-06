package com.chocomiruku.myhome.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val contractNumber: String = "",
    val profilePicPath: String? = null,
    val isAdmin: Boolean = false
) : Parcelable {
    constructor() : this("", "", "", null, false)
}

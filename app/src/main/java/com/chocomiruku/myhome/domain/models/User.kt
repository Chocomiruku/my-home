package com.chocomiruku.myhome.domain.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @DocumentId
    val uid: String,
    val name: String,
    val email: String,
    val contractNumber: String = "",
    val defaultColorId: Int = 0,
    val imageUri: String? = null,
    val notifications: Boolean,
    val admin: Boolean
) : Parcelable {
    constructor() : this(
        uid = "",
        name = "",
        email = "",
        defaultColorId = 0,
        contractNumber = "",
        imageUri = null,
        notifications = true,
        admin = false
    )
}

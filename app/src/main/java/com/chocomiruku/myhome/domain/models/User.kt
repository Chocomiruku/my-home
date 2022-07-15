package com.chocomiruku.myhome.domain.models

import android.os.Parcelable
import com.chocomiruku.myhome.util.UserRole
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
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
    val userRole: String
) : Parcelable {
    constructor() : this(
        uid = "",
        name = "",
        email = "",
        defaultColorId = 0,
        contractNumber = "",
        imageUri = null,
        notifications = true,
        userRole = "default"
    )

    @get:Exclude
    val role: UserRole
        get() {
            return when (userRole) {
                "default" -> UserRole.DEFAULT
                "moderator" -> UserRole.MODERATOR
                "admin" -> UserRole.ADMIN
                else -> { throw Exception("Incorrect role") }
            }
        }
}

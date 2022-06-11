package com.chocomiruku.myhome.domain.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Message(
    @DocumentId
    val messageId: String,
    val text: String,
    val date: Long,
    val imageUri: String? = null,
    val userId: String
) {
    constructor() : this(
        messageId = "",
        text = "",
        date = 0L,
        imageUri = null,
        userId = ""
    )

    @get:Exclude
    var sender: User? = null
}

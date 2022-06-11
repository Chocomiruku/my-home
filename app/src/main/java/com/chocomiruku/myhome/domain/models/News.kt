package com.chocomiruku.myhome.domain.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    @DocumentId
    val newsId: String,
    val title: String,
    val date: Long,
    val text: String,
    val imageUri: String? = null
) : Parcelable {
    constructor() : this(
        newsId = "",
        title = "",
        date = 0L,
        text = "",
        imageUri = null,
    )
}

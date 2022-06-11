package com.chocomiruku.myhome.domain.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Poll(
    @DocumentId
    val pollId: String,
    val title: String,
    val options: MutableMap<String, Int>,
    val date: Long,
    val multipleChoice: Boolean,
    val closed: Boolean,
    val voted: MutableList<String>
) {
    constructor() : this(
        pollId = "",
        title = "",
        options = mutableMapOf<String, Int>(),
        date = 0L,
        multipleChoice = false,
        closed = false,
        voted = mutableListOf<String>()
    )

    @get:Exclude
    var hasVoted: Boolean? = null
}

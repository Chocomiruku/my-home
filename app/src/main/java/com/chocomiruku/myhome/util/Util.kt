package com.chocomiruku.myhome.util

import android.annotation.SuppressLint
import com.chocomiruku.myhome.R
import java.text.SimpleDateFormat

val colors = listOf(
    R.color.profile_pic_1, R.color.profile_pic_2, R.color.profile_pic_3, R.color.profile_pic_4,
    R.color.profile_pic_5, R.color.profile_pic_6
)

fun generateRandomColor(): Int {
    return colors.random()
}

@SuppressLint("SimpleDateFormat")
fun Long.convertToDateString(): String {
    return SimpleDateFormat("dd-MM HH:mm")
        .format(this).toString()
}
package com.chocomiruku.myhome.data.cache.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,

    val text: String,

    val date: Long,

    @ColumnInfo(defaultValue = "NULL")
    @Nullable
    val image: String?
)
package com.chocomiruku.myhome.data.cache.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users", foreignKeys = [ForeignKey(
    entity = UserEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user_id"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)])
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val text: String,

    val time: Long,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(defaultValue = "NULL")
    @Nullable
    val image: String?
)
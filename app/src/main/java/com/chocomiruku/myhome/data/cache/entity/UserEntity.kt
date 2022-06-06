package com.chocomiruku.myhome.data.cache.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    val phone: String,

    @ColumnInfo(name = "profile_pic_path", defaultValue = "NULL")
    @Nullable
    val profilePicPath: String?,

    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean
)
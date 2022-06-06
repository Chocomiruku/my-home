package com.chocomiruku.myhome.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chocomiruku.myhome.data.cache.entity.MessageEntity
import com.chocomiruku.myhome.data.cache.entity.NewsEntity
import com.chocomiruku.myhome.data.cache.entity.UserEntity

//@Database(entities = [UserEntity::class, NewsEntity::class, MessageEntity::class], version = 1)
//abstract class CharactersDatabase : RoomDatabase() {
//
//}
//
//private lateinit var INSTANCE: CharactersDatabase
//
//fun getDatabase(context: Context): CharactersDatabase {
//    synchronized(CharactersDatabase::class.java) {
//        if (!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                CharactersDatabase::class.java,
//                "characters"
//            ).build()
//        }
//    }
//    return INSTANCE
//}
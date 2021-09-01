package com.example.tvclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChannelCategoryEntity::class], version = 1)
abstract class TVClientDatabase : RoomDatabase() {
    abstract fun channelCategoryDao(): ChannelCategoryDao
}

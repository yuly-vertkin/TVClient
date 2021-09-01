package com.example.tvclient.di

import android.content.Context
import androidx.room.Room
import com.example.tvclient.data.database.ChannelCategoryDao
import com.example.tvclient.data.database.TVClientDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Provides
    fun provideChannelCategoryDao(db: TVClientDatabase): ChannelCategoryDao {
        return db.channelCategoryDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): TVClientDatabase {
        return Room.databaseBuilder(appContext, TVClientDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private const val DATABASE_NAME = "TVClientDatabase"
    }
}
package com.example.tvclient.di

import com.example.tvclient.data.RemoteDataSource
import com.example.tvclient.data.mvapi.MwDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindDataSource(
        dataSourceImpl: MwDataSource
    ): RemoteDataSource
}
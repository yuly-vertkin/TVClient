package com.example.mytest.di

import com.example.mytest.data.DataSource
import com.example.mytest.data.mvapi.MwDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindDataSource(
        dataSourceImpl: MwDataSource
    ): DataSource
}
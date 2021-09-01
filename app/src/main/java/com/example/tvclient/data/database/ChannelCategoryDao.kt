package com.example.tvclient.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class ChannelCategoryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "category_name") val categoryName: String?
)

@Dao
interface ChannelCategoryDao {
    @Query("SELECT * FROM ChannelCategoryEntity")
    fun getAll(): Flow<List<ChannelCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ChannelCategoryEntity>)

    @Delete
    suspend fun delete(item: ChannelCategoryEntity)
}
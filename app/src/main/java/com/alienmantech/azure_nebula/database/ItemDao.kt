package com.alienmantech.azure_nebula.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity): Int

    @Delete
    suspend fun delete(item: ItemEntity): Int

    @Query("SELECT * FROM items ORDER BY timestamp DESC")
    fun getAllItems(): LiveData<List<ItemEntity>>
}

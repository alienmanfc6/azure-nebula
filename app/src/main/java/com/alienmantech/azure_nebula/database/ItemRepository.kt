package com.alienmantech.azure_nebula.database

import androidx.lifecycle.LiveData

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: LiveData<List<ItemEntity>> = itemDao.getAllItems()

    suspend fun insert(item: ItemEntity) {
        itemDao.insert(item)
    }

    suspend fun update(item: ItemEntity) {
        itemDao.update(item)
    }

    suspend fun delete(item: ItemEntity) {
        itemDao.delete(item)
    }
}

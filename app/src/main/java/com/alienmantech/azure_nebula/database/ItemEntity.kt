package com.alienmantech.azure_nebula.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var cost: Int,
    var timestamp: Long,
)
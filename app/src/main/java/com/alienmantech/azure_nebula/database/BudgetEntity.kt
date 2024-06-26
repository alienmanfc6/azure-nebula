package com.alienmantech.azure_nebula.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var monthYear: String,
    var amount: Int,
)
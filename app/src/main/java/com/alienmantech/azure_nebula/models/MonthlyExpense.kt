package com.alienmantech.azure_nebula.models

import com.alienmantech.azure_nebula.database.BudgetEntity
import com.alienmantech.azure_nebula.database.ItemEntity

data class MonthlyExpense(
    val monthYear: String,
    val items: List<ItemEntity>,
    val budget: BudgetEntity? = null,
    val totalCost: Int,
)
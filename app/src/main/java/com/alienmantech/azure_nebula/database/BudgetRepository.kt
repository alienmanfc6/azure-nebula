package com.alienmantech.azure_nebula.database

import androidx.lifecycle.LiveData

class BudgetRepository(private val budgetDao: BudgetDao) {
    val allBudgets: LiveData<List<BudgetEntity>> = budgetDao.getAllBudgets()

    suspend fun insert(item: BudgetEntity) {
        budgetDao.insert(item)
    }

    suspend fun update(item: BudgetEntity) {
        budgetDao.update(item)
    }

    suspend fun delete(item: BudgetEntity) {
        budgetDao.delete(item)
    }
}

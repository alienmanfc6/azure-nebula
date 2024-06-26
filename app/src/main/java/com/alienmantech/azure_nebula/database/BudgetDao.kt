package com.alienmantech.azure_nebula.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    @Update
    suspend fun update(budget: BudgetEntity): Int

    @Delete
    suspend fun delete(budget: BudgetEntity): Int

    @Query("SELECT * FROM budgets ORDER BY monthYear DESC")
    fun getAllBudgets(): LiveData<List<BudgetEntity>>
}

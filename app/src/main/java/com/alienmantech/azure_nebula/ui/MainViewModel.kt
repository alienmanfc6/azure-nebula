package com.alienmantech.azure_nebula.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alienmantech.azure_nebula.StandardSnackbar
import com.alienmantech.azure_nebula.database.BudgetEntity
import com.alienmantech.azure_nebula.database.BudgetRepository
import com.alienmantech.azure_nebula.database.ItemEntity
import com.alienmantech.azure_nebula.database.ItemRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val itemRepository: ItemRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    val allItems: LiveData<List<ItemEntity>> = itemRepository.allItems
    val allBudgets: LiveData<List<BudgetEntity>> = budgetRepository.allBudgets

    var showBudgetDialog = mutableStateOf<String?>(null)
    val showItemEditDialog = mutableStateOf<ItemEntity?>(null)
    val showSnackbar = mutableStateOf<StandardSnackbar?>(null)

    fun showSnackbar(message: String, actionText: String? = null, action: (() -> Unit)? = null) {
        showSnackbar.value = StandardSnackbar(message, actionText, action)
    }

    fun insertItem(item: ItemEntity) = viewModelScope.launch {
        itemRepository.insert(item)
    }

    fun updateItem(item: ItemEntity) = viewModelScope.launch {
        itemRepository.update(item)
    }

    fun deleteItem(item: ItemEntity) = viewModelScope.launch {
        itemRepository.delete(item)
    }

    fun getBudget(monthYear: String?): BudgetEntity? {
        return allBudgets.value?.firstOrNull { it.monthYear == monthYear }
    }

    fun updateBudget(monthYear: String, amount: Int) = viewModelScope.launch {
        budgetRepository.insert(BudgetEntity(monthYear = monthYear, amount = amount))
    }
}

class ItemViewModelFactory(
    private val itemRepository: ItemRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(itemRepository, budgetRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
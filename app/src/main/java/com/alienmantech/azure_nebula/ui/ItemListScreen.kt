package com.alienmantech.azure_nebula.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alienmantech.azure_nebula.database.BudgetEntity
import com.alienmantech.azure_nebula.formatDisplayCost
import com.alienmantech.azure_nebula.database.ItemEntity
import com.alienmantech.azure_nebula.formatItemName
import com.alienmantech.azure_nebula.models.MonthlyExpense
import com.alienmantech.azure_nebula.ui.theme.ItemRowDeleteButtonBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.ItemRowEditButtonBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.TableHeaderBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.TableHeaderTextColor
import com.alienmantech.azure_nebula.ui.theme.TableRowBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.TableRowTextColor
import com.alienmantech.azure_nebula.ui.theme.TextColorDefault
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ItemListScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val items by viewModel.allItems.observeAsState(emptyList())
    val budgets by viewModel.allBudgets.observeAsState(emptyList())

    val showBudgetDialog by remember { viewModel.showBudgetDialog }
    val showItemEditDialog by remember { viewModel.showItemEditDialog }

    val groupedItems = items.groupBy { item ->
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(item.timestamp))
    }.map { (monthYear, items) ->
        MonthlyExpense(
            monthYear = monthYear,
            items = items,
            budget = budgets.firstOrNull { it.monthYear == monthYear },
            totalCost = items.sumOf { it.cost }
        )
    }

    LazyColumn(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
        ),
    ) {
        groupedItems.forEach { monthlyExpense ->
            item {
                MonthlyExpenseHeader(
                    month = monthlyExpense.monthYear,
                    totalSpent = monthlyExpense.totalCost,
                    budget = monthlyExpense.budget,
                    onClick = { month ->
                        viewModel.showBudgetDialog.value = month
                    }
                )
            }
            items(monthlyExpense.items) { item ->
                ItemRow(
                    item = item,
                    onEdit = { selectedItem ->
                        viewModel.showItemEditDialog.value = selectedItem
                    },
                    onDelete = { selectedItem ->
                        viewModel.deleteItem(selectedItem)

                        viewModel.showSnackbar("${selectedItem.name} deleted.", "Undo") {
                            viewModel.insertItem(selectedItem)
                        }
                    }
                )
            }
        }
    }

    if (showBudgetDialog != null) {
        val budget = viewModel.getBudget(showBudgetDialog)
        val monthYear = budget?.monthYear ?: showBudgetDialog
        val initialAmount = budget?.amount ?: 0

        if (monthYear != null) {
            BudgetDialog(
                monthYear = monthYear,
                initialAmount = initialAmount,
                onDismissRequest = {
                    viewModel.showBudgetDialog.value = null
                },
                onSaveBudget = { amount ->
                    viewModel.updateBudget(monthYear, amount)
                },
                totalSpent = groupedItems.firstOrNull { it.monthYear == showBudgetDialog }?.totalCost ?: 0,
            )
        }
    }

    showItemEditDialog?.let { selectedItem ->
        ItemEditDialog(
            item = selectedItem,
            onSave = {
                viewModel.updateItem(it)
            },
            onDismissRequest = {
                viewModel.showItemEditDialog.value = null
            },
        )
    }
}

@Composable
fun MonthlyExpenseHeader(month: String, totalSpent: Int, budget: BudgetEntity?, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = TableHeaderBackgroundColor)
            .clickable {
                onClick.invoke(month)
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StandardText(
            modifier = Modifier
                .padding(16.dp),
            text = month,
            color = TableHeaderTextColor,
            style = MaterialTheme.typography.titleLarge,
        )

        StandardText(
            modifier = Modifier
                .padding(16.dp),
            text = formatMonthlyHeaderTotal(totalSpent, budget),
            color = TableHeaderTextColor,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun formatMonthlyHeaderTotal(totalSpent: Int, budget: BudgetEntity?): String {
    return if (budget == null) {
        "Spent: ${formatDisplayCost(totalSpent, false)}"
    } else {
        "Spent: ${formatDisplayCost(totalSpent, false)}/${formatDisplayCost(budget.amount, false)}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRow(item: ItemEntity, onEdit: (ItemEntity) -> Unit, onDelete: (ItemEntity) -> Unit) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    onEdit.invoke(item)
                }

                DismissValue.DismissedToStart -> {
                    onDelete.invoke(item)
                }

                else -> return@rememberDismissState false
            }
            return@rememberDismissState true
        },
        positionalThreshold = { it * .25f }
    )

    if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) {
            dismissState.reset()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = if (dismissState.dismissDirection == DismissDirection.StartToEnd) ItemRowEditButtonBackgroundColor else ItemRowDeleteButtonBackgroundColor)
                    .padding(horizontal = 20.dp),
                contentAlignment = if (dismissState.dismissDirection == DismissDirection.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = if (dismissState.dismissDirection == DismissDirection.StartToEnd) Icons.Default.Edit else Icons.Default.Delete,
                    contentDescription = if (dismissState.dismissDirection == DismissDirection.StartToEnd) "Edit" else "Delete",
                    tint = TextColorDefault
                )
            }
        },
        dismissContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = TableRowBackgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StandardText(
                        text = formatItemName(item),
                        color = TableRowTextColor,
                    )
                    StandardText(
                        text = formatDisplayCost(item.cost),
                        color = TableRowTextColor,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun MonthlyExpenseHeaderPreview() {
    MonthlyExpenseHeader("June 2024", 100000, null) {}
}

@Preview
@Composable
private fun ItemRowPreview() {
    ItemRow(
        item = ItemEntity(
            id = 0,
            name = "Item 1",
            cost = 100,
            timestamp = System.currentTimeMillis()
        ),
        onEdit = {},
        onDelete = {}
    )
}

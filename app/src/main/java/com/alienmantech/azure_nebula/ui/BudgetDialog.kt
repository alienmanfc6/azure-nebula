package com.alienmantech.azure_nebula.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alienmantech.azure_nebula.Utils
import com.alienmantech.azure_nebula.formatDisplayCost
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun BudgetDialog(
    monthYear: String,
    initialAmount: Int,
    onDismissRequest: () -> Unit,
    onSaveBudget: (Int) -> Unit,
    totalSpent: Int
) {
    val formatInitialDisplayCost = formatDisplayCost(cost = initialAmount)
    var budgetDisplayCost by remember { mutableStateOf(formatInitialDisplayCost) }
    var budgetRawCost by remember { mutableIntStateOf(initialAmount) }

    StandardAlertDialog(
        title = "Set Budget for $monthYear",
        text = {
            Column {
                StandardTextField(
                    value = budgetDisplayCost,
                    onValueChange = {
                        budgetDisplayCost = it
                        budgetRawCost = Utils.parseCostInput(it)
                    },
                    labelText = "Monthly Budget",
                    keyboardType = KeyboardType.Number,
                )

                Spacer(modifier = Modifier.height(16.dp))

                StandardText(
                    text = "Report",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StandardText(text = "Monthly Budget:")
                    StandardText(text = formatDisplayCost(budgetRawCost))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StandardText(text = "Total Spent So Far:")
                    StandardText(text = formatDisplayCost(totalSpent))
                }

                Spacer(modifier = Modifier.height(4.dp))
                Divider(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StandardText(text = "Remaining Budget:")
                    StandardText(text = formatDisplayCost(budgetRawCost - totalSpent))
                }

                Spacer(modifier = Modifier.height(4.dp))
                Divider(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StandardText(text = "Projected Spending: (${if (formatProjected(monthYear, totalSpent, budgetRawCost) > budgetRawCost) "Over" else "Under"})")
                    StandardText(text = formatDisplayCost(formatProjected(monthYear, totalSpent, budgetRawCost)))
                }
            }
        },
        confirmButtonText = "Save",
        onConfirm = {
            onSaveBudget(budgetRawCost)
            onDismissRequest()
        },
        cancelButtonText = "Cancel",
        onCancel = { },
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun formatProjected(monthYear: String, totalSpent: Int, budgetValue: Int): Int {
    return remember(budgetValue) {
        val split = Utils.splitMonthYear(monthYear)
        val month = split.first
        val year = split.second
        (totalSpent / LocalDate.now().dayOfMonth) * YearMonth.of(year, month).lengthOfMonth()
    }
}

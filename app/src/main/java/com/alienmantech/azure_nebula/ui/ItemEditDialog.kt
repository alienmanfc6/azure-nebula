package com.alienmantech.azure_nebula.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alienmantech.azure_nebula.Utils
import com.alienmantech.azure_nebula.database.ItemEntity
import com.alienmantech.azure_nebula.formatDisplayCost

@Composable
fun ItemEditDialog(
    item: ItemEntity,
    onSave: (ItemEntity) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val formatInitialDisplayCost = formatDisplayCost(cost = item.cost)
    var itemName by remember { mutableStateOf(item.name) }
    var itemDisplayCost by remember { mutableStateOf(formatInitialDisplayCost) }
    var itemRawCost by remember { mutableIntStateOf(item.cost) }

    StandardAlertDialog(
        title = "Edit Item - ${item.name}",
        text = {
            Column {
                StandardTextField(
                    value = itemName,
                    onValueChange = {
                        itemName = it
                    },
                    labelText = "Name",
                    keyboardType = KeyboardType.Text,
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = itemDisplayCost,
                    onValueChange = {
                        itemDisplayCost = it
                        itemRawCost = Utils.parseCostInput(it)
                    },
                    labelText = "Cost",
                    keyboardType = KeyboardType.Number,
                )
            }
        },
        confirmButtonText = "Save",
        onConfirm = {
            item.name = itemName
            item.cost = itemRawCost
            onSave(item)
            onDismissRequest()
        },
        cancelButtonText = "Cancel",
        onCancel = { },
        onDismissRequest = onDismissRequest,
    )
}

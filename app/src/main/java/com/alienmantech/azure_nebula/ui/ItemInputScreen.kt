package com.alienmantech.azure_nebula.ui

import androidx.compose.foundation.background
import androidx.compose.ui.text.input.KeyboardType
import com.alienmantech.azure_nebula.database.ItemEntity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alienmantech.azure_nebula.Utils
import com.alienmantech.azure_nebula.ui.theme.ButtonBackgroundColorDefault
import com.alienmantech.azure_nebula.ui.theme.ButtonBackgroundColorDisabled
import com.alienmantech.azure_nebula.ui.theme.ButtonTextColorDefault
import com.alienmantech.azure_nebula.ui.theme.ButtonTextColorDisabled
import com.alienmantech.azure_nebula.ui.theme.InputContainerBackgroundColor

@Composable
fun ItemInputScreen(modifier: Modifier = Modifier, viewModel: MainViewModel?) {
    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .background(color = InputContainerBackgroundColor)
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            StandardTextField(
                modifier = Modifier
                    .weight(0.7f),
                value = name,
                onValueChange = { name = it },
                labelText = "Item Name",
                capitalization = KeyboardCapitalization.Words,
            )

            Spacer(modifier = Modifier.width(16.dp))

            StandardTextField(
                modifier = Modifier
                    .weight(0.3f),
                value = cost,
                onValueChange = { cost = it },
                labelText = "Cost",
                keyboardType = KeyboardType.Number,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp),
            enabled = name.isNotEmpty() && cost.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ButtonBackgroundColorDefault,
                contentColor = ButtonTextColorDefault,
                disabledBackgroundColor = ButtonBackgroundColorDisabled,
                disabledContentColor = ButtonTextColorDisabled,
            ),
            onClick = {
                val costValue = Utils.parseCostInput(cost)
                if (name.isNotEmpty() && costValue != 0) {
                    viewModel?.insertItem(
                        ItemEntity(
                            name = name,
                            cost = costValue,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    // clear form
                    name = ""
                    cost = ""
                }
            },
        ) {
            StandardText(
                text = "Add Item",
            )
        }
    }
}

@Preview
@Composable
private fun ItemInputScreenPreview() {
    ItemInputScreen(viewModel = null)
}
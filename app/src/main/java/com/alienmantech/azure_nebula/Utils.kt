package com.alienmantech.azure_nebula

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.alienmantech.azure_nebula.database.ItemEntity
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Date
import java.util.Locale

object Utils {

    fun parseCostInput(cost: String): Int {
        if (cost.isEmpty()) {
            return 0
        }

        // remove all chars except numbers and decimal point
        val cleanCost = cost.replace(Regex("[^\\d.]"), "")
        if (cleanCost.isEmpty()) {
            return 0
        }

        return if (cleanCost.contains(".")) {
            (cleanCost.toDouble() * 100).toInt()
        } else {
            cleanCost.toInt() * 100
        }
    }

    fun splitMonthYear(monthYear: String): Pair<Month, Int> {
        // format is "June 2024", split into month and year values and return
        val split = monthYear.split(" ")
        val month = Month.valueOf(split[0].uppercase(Locale.getDefault()))
        val year = split[1].toInt()

        return Pair(month, year)
    }
}

@Composable
fun formatDisplayCost(cost: Int, showCents: Boolean = true): String {
    return remember(cost) {
        val dollars = cost / 100
        val cents = cost % 100
        // ensure cents are always two digits
        val centsString = if (cents < 10) "0$cents" else cents.toString()
        // add commas to the dollars
        val dollarsString = dollars.toString().reversed().chunked(3).joinToString(",").reversed()

        if (showCents) {
            "$$dollarsString.$centsString"
        } else {
            "$$dollarsString"
        }
    }
}

@Composable
fun formatItemName(item: ItemEntity): String {
    return remember(item.name, item.timestamp) {
        val name = item.name
        val date = SimpleDateFormat("d", Locale.getDefault()).format(Date(item.timestamp))
        // add suffix to date
        val suffix = when (date.toInt()) {
            1, 21, 31 -> "st"
            2, 22 -> "nd"
            3, 23 -> "rd"
            else -> "th"
        }
        "$date$suffix - $name"
    }
}


data class StandardSnackbar(
    val message: String,
    val actionText: String? = null,
    val action: (() -> Unit)? = null,
)

package com.alienmantech.azure_nebula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.alienmantech.azure_nebula.database.AppDatabase
import com.alienmantech.azure_nebula.database.BudgetRepository
import com.alienmantech.azure_nebula.database.ItemRepository
import com.alienmantech.azure_nebula.ui.ItemInputScreen
import com.alienmantech.azure_nebula.ui.ItemListScreen
import com.alienmantech.azure_nebula.ui.MainViewModel
import com.alienmantech.azure_nebula.ui.ItemViewModelFactory
import com.alienmantech.azure_nebula.ui.StandardText
import com.alienmantech.azure_nebula.ui.theme.BackgroundColor
import com.alienmantech.azure_nebula.ui.theme.ToolbarBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create an instance of the database and the repository
        val database = AppDatabase.getDatabase(applicationContext)
        val itemRepository = ItemRepository(database.itemDao())
        val budgetRepository = BudgetRepository(database.budgetDao())

        // Create the ViewModelFactory
        val factory = ItemViewModelFactory(itemRepository, budgetRepository)

        // Obtain the ViewModel using the ViewModelProvider
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val showSnackbar by remember { viewModel.showSnackbar }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            StandardText(
                                text = "Gilt Free Expense Tracker",
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = ToolbarBackgroundColor,
                        ),
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = BackgroundColor),
                ) {
                    ItemInputScreen(
                        modifier = Modifier
                            .fillMaxWidth(),
                        viewModel = viewModel,
                    )
                    ItemListScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        viewModel = viewModel,
                    )
                }
            }

            LaunchedEffect(showSnackbar) {
                showSnackbar?.let { showSnackbar ->
                    val result = snackbarHostState.showSnackbar(
                        message = showSnackbar.message,
                        actionLabel = showSnackbar.actionText,
                        duration = SnackbarDuration.Long,
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        showSnackbar.action?.invoke()
                    }
                }
            }
        }
    }
}
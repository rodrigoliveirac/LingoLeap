package com.rodcollab.lingoleap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodcollab.lingoleap.core.database.AppDatabase
import com.rodcollab.lingoleap.history.HistoryScreen
import com.rodcollab.lingoleap.history.HistoryViewModel
import com.rodcollab.lingoleap.profile.ProfileScreen
import com.rodcollab.lingoleap.profile.SavedScreen
import com.rodcollab.lingoleap.profile.SavedViewModel
import com.rodcollab.lingoleap.profile.WordsSavedUseCaseImpl
import com.rodcollab.lingoleap.saved.WordsSavedRepositoryImpl
import com.rodcollab.lingoleap.search.SearchHistoryImpl
import com.rodcollab.lingoleap.search.SearchScreen
import com.rodcollab.lingoleap.search.SearchViewModel
import com.rodcollab.lingoleap.ui.theme.LingoLeapTheme

class MainActivity : ComponentActivity() {

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(this.applicationContext)
    }
    private val repository: WordsSavedRepositoryImpl by lazy {
        WordsSavedRepositoryImpl(db)
    }

    private val searchRepository: SearchHistoryImpl by lazy {
        SearchHistoryImpl(db)
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.MyViewModelFactory(searchRepository, repository)
    }

    private val savedViewModel: SavedViewModel by viewModels {
        val useCase = WordsSavedUseCaseImpl(repository)
        SavedViewModel.SavedViewModelFactory(useCase)
    }

    private val searchHistoryViewModel: HistoryViewModel by viewModels {
        HistoryViewModel.HistoryViewModelFactory(searchRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(MyObserver(searchHistoryViewModel, savedViewModel, viewModel))
        setContent {
            LingoLeapTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { LayoutBasicBottomNavigation(navController) },
                    scaffoldState = scaffoldState,
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "search") {
                        composable("search") {
                            SearchScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                viewModel = viewModel
                            )
                        }
                        composable("history") {
                            HistoryScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                searchHistoryViewModel
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            )
                        }
                        composable("saved_screen") {
                            SavedScreen(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues), savedViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavigationItemData(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavigationItems = listOf(
    BottomNavigationItemData("history", Icons.Default.History, "History"),
    BottomNavigationItemData("search", Icons.Default.Search, "Search"),
    BottomNavigationItemData("profile", Icons.Default.Person, "Profile")
)

@Composable
fun LayoutBasicBottomNavigation(navController: NavController, modifier: Modifier = Modifier) {

    var selectedItem by remember { mutableStateOf(1) }

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        bottomNavigationItems.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.route)
                    selectedItem = index
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LingoLeapTheme {

    }
}
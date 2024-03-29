package com.rodcollab.lingoleap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rodcollab.lingoleap.features.history.HistoryScreen
import com.rodcollab.lingoleap.features.search.SearchScreen
import com.rodcollab.lingoleap.features.word.detail.WordDetailScreen
import com.rodcollab.lingoleap.ui.theme.LingoLeapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LingoLeapTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {

                        if (currentRoute(navController = navController) == "search" || currentRoute(navController = navController) == "history") {
                            LayoutBasicBottomNavigation(navController)
                        }

                    },
                    scaffoldState = scaffoldState,
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "search") {
                        composable("search") {
                            SearchScreen(
                                onClickWord = { word ->
                                    navController.navigate("word_details/$word")
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                            )
                        }
                        composable("history") {
                            HistoryScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                toDetailsScreen = { word ->
                                    navController.navigate("word_details/$word")
                                },
                            )
                        }
                        composable(
                            "word_details/{word}",
                            arguments = listOf(navArgument("word") { type = NavType.StringType })
                        ) {
                             WordDetailScreen(
                                onNavigateBack = {
                                    navController.navigateUp()
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

data class BottomNavigationItemData(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavigationItems = listOf(
    BottomNavigationItemData("history", Icons.Default.History, "History"),
    BottomNavigationItemData("search", Icons.Default.Search, "Search"),
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
package com.rodcollab.lingoleap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.rodcollab.lingoleap.history.HistoryScreen
import com.rodcollab.lingoleap.profile.ProfileScreen
import com.rodcollab.lingoleap.search.SearchScreen
import com.rodcollab.lingoleap.ui.theme.LingoLeapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                    .padding(paddingValues)
                            )
                        }
                        composable("history") {
                            HistoryScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
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
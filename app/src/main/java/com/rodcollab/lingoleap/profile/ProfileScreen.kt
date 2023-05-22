package com.rodcollab.lingoleap.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodcollab.lingoleap.R

@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier) {

    Column(modifier = modifier.padding(8.dp).fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { navController.navigate("saved_screen") },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "Bookmark",
                    tint = Color.Gray
                )
            }
            Text(
                "Saved",
                color = Color.Gray
            )
        }
    }
}
package com.rodcollab.lingoleap.features.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    searching: Boolean
) {
    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(searching) {
        enabled = !searching
    }
    val keyBoard = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            trailingIcon = {
                IconButton(modifier = Modifier
                    .padding(end = 8.dp), onClick = {
                    keyBoard?.hide()
                    enabled = false
                    onSearch()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Light,
                    color = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                )
            },
            value = searchValue,
            enabled = enabled,
            onValueChange = onSearchValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyBoard?.hide()
                    enabled = false
                    onSearch()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SearchBarPreview(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.surface
        ),
        placeholder = { Text("Search") },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
    )
}
package com.rodcollab.lingoleap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TranslateComponent(
    selectedLanguage: (String) -> Unit,
    languages: List<LanguageOption>,
    isLoading: Boolean,
    text: String
) {

    var expanded by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("Portuguese") }

    Box(modifier = Modifier.padding(top = 24.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 16.dp)
                .background(Color(250, 128, 46), RoundedCornerShape(32.dp))
                .zIndex(1f)
                .clickable { expanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    color = Color.White,
                    modifier = Modifier
                        .padding(12.dp), text = language
                )
                Icon(
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .padding(start = 16.dp, top = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Square,
                    color = Color(255, 20, 147),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(72.dp)
                        .padding(24.dp)
                )
            } else {
                Text(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    ),
                    text = text
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        selectedLanguage(selectionOption.code)
                        language = selectionOption.name
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun TranslateComponentPreview() {
    Column() {
        Box() {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(bottom = 16.dp)
                    .background(Color(250, 128, 46), RoundedCornerShape(32.dp))
                    .zIndex(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        color = Color.White,
                        modifier = Modifier
                            .padding(12.dp), text = "Portuguese"
                    )
                    Icon(
                        tint = Color.White,
                        modifier = Modifier.padding(0.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 0.dp, end = 16.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(modifier = Modifier.weight(0.3f)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Maecenas eleifend accumsan ultricies. Nulla laoreet viverra erat nec suscipit. In pretium venenatis ex ac dapibus. Duis vitae in nulla vestibulum vulputa. Sed aliquam augue est, convallis tincidunt."
                    )
                    Divider(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
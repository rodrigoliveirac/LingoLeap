package com.rodcollab.lingoleap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rodcollab.lingoleap.features.word.detail.LanguageOption
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateComponent(
    selectedLanguage: (String) -> Unit,
    languages: List<LanguageOption>,
    isLoading: Boolean,
    text: String
) {

    var expanded by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf(Locale.getDefault().displayLanguage) }

    Box(modifier = Modifier.padding(top = 24.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 16.dp)
                .background(Color(250, 128, 46), RoundedCornerShape(32.dp))
                .zIndex(1f)
                .clickable {
                    expanded = true
                }
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = language,
                    color = Color.White,
                    modifier = Modifier
                        .padding(12.dp),
                )
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
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
        Box(modifier = Modifier.align(Alignment.Center)) {
            DropdownMenu(
                offset = DpOffset((-60).dp, (-190).dp),
                modifier = Modifier
                    .size(120.dp)
                    .zIndex(1f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = {
                            selectedLanguage(selectionOption.code.toString())
                            language = selectionOption.name
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBarOverDropDownMenu(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier,
    hint: String = "Search",
    shouldShowHint: Boolean = false,
    onFocusChanged: (FocusState) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = searchValue,
            onValueChange = onSearchValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    defaultKeyboardAction(ImeAction.Search)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(2.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(end = 16.dp)
                .onFocusChanged { onFocusChanged(it) }
        )
        if (shouldShowHint) {
            androidx.compose.material.Text(
                text = hint,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light,
                color = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }
        androidx.compose.material.IconButton(
            onClick = onSearch,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            androidx.compose.material.Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
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
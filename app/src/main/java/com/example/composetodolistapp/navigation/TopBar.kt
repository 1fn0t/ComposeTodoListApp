package com.example.composetodolistapp

import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composetodolistapp.navigation.Screen
import com.example.gym.ui.theme.Green700

@Composable
fun AppBar(
    backBtnEnabled: Boolean,
    backBtnColor: Color,
    goBackToScreen: () -> Screen,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {  },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(goBackToScreen().route)
            },
                enabled = backBtnEnabled
            ) {
                Icon(painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Toggle Drawer",
                    tint = backBtnColor
                )
            }
        },
        contentColor = Color.Transparent,
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = modifier
    )
}
package com.example.composetodolistapp.navigation


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import com.example.composetodolistapp.navigation.Screen
import com.example.gym.ui.theme.Green700

class NavViewModel(
): ViewModel() {
    var currentScreen: Screen by mutableStateOf(Screen.TodoScreen)
    var backButtonEnabled by mutableStateOf(false)
        private set
    var backButtonColor by mutableStateOf(Color.Transparent)
        private set
    var lastScreen: Screen by mutableStateOf(Screen.TodoScreen)
    var signedIn by mutableStateOf(false)
        private set
    var appBarColor by mutableStateOf(Color.Transparent)

    fun switchScreen(screen: Screen) {
        lastScreen = currentScreen
        currentScreen = screen
        if (screen == Screen.TodoDetails) {
            backButtonEnabled = true
            backButtonColor = Color.Black
        }
    }

    fun switchBackToScreen(): Screen {
        currentScreen = lastScreen
        backButtonEnabled = false
        backButtonColor = Color.Transparent
        appBarColor = Color.Transparent
        return lastScreen
    }

    fun changeAppBarColor(color: Color) {
        appBarColor = color
    }

    fun updateSignIn(newState: Boolean) {
        signedIn = newState
    }
}

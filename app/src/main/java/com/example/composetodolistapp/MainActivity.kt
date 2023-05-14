package com.example.composetodolistapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composetodolistapp.db.DatabaseViewModel
import com.example.composetodolistapp.db.TodoRepository
import com.example.composetodolistapp.navigation.NavViewModel
import com.example.composetodolistapp.navigation.Screen
import com.example.gym.ui.theme.ComposeTodoListAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var todoRepo: TodoRepository
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            signedIn = true
        }
    }

    private lateinit var auth: FirebaseAuth
    private var signedIn = false
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val firestoreDb = Firebase.firestore

        setContent {

            val navModel: NavViewModel = viewModel()
            val databaseModel: DatabaseViewModel = viewModel()
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                navModel.updateSignIn(signedIn)
            }

            ComposeTodoListAppTheme {
                Scaffold(
                    topBar = {
                        AppBar(
                            backBtnEnabled = navModel.backButtonEnabled,
                            backBtnColor = navModel.backButtonColor,
                            goBackToScreen = { navModel.switchBackToScreen() },
                            navController = navController
                        )
                    }
                ) { paddingValues ->
                    val mod = Modifier.fillMaxSize(1f).padding(paddingValues)
                    if (navModel.signedIn) {
                        NavHost(navController = navController, startDestination = Screen.TodoScreen.route) {
                            composable(route = Screen.TodoScreen.route) {
                                TodosScreen(
                                    navController = navController,
                                    dbModel = databaseModel,
                                    navModel = navModel,
                                    modifier = mod
                                )
                            }
                            composable(route = Screen.TodoDetails.route + "/{action}?colorId={colorId}&todoId={todoId}",
                                arguments = listOf(
                                    navArgument("todoId") {
                                        type = NavType.StringType
                                        defaultValue = null
                                        nullable = true
                                    },
                                    navArgument("colorId") {
                                        type = NavType.StringType
                                        defaultValue = null
                                        nullable = true
                                    },
                                    navArgument("action") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                                )
                            ) { entry ->
                                DetailsScreen(todoId = entry.arguments?.getString("todoId"),
                                    colorId = entry.arguments?.getString("colorId"),
                                    action = entry.arguments?.getString("action"),
                                    firestoreDb = firestoreDb, auth = auth,
                                    dbModel = databaseModel, modifier = mod
                                    )
                            }
                        }

                    } else {
                        NavHost(navController = navController, startDestination = Screen.SignIn.route) {
                            composable(route = Screen.SignIn.route) {
                                SignInScreen(auth = auth, navController = navController, navModel = navModel, mod)
                            }
                            composable(route = Screen.SignUp.route) {
                                SignUpScreen(auth = auth, navController = navController, mod)
                            }
                        }
                    }
                }
            }
        }
    }
}
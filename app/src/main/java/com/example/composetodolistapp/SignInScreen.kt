package com.example.composetodolistapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composetodolistapp.navigation.NavViewModel
import com.example.composetodolistapp.navigation.Screen
import com.example.gym.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "Sign In/ Sign Up"

@Composable
fun SignInScreen(
    auth: FirebaseAuth,
    navController: NavController,
    navModel: NavViewModel,
    modifier: Modifier = Modifier
) {
    var emailField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var passwordField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(text = "Sing In", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))
        TextField(value = emailField.text,
            onValueChange = {
                    change -> emailField = TextFieldValue(change)
            },
            placeholder = {
                Text(text = "enter email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = passwordField.text,
            onValueChange = {
                    change -> passwordField = TextFieldValue(change)
            },
            placeholder = {
                Text(text = "enter password")
            }
        )
        Spacer(Modifier.height(32.dp))
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
                ) {
            OutlinedButton(onClick = {
                auth.signInWithEmailAndPassword(emailField.text, passwordField.text)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            navModel.updateSignIn(true)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Blue
            ),
                border = BorderStroke(ButtonDefaults.outlinedButtonBorder.width, Green300)
                ) {
                Text(text = "Sign In")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                             navController.navigate(Screen.SignUp.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueGray200,
                contentColor = Color.White
            ),
                ) {
                Text(text = "Sign Up", color = Color.White)
            }
        }
    }
}

@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var emailField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var passwordField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(text = "Sing Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        TextField(value = emailField.text,
            onValueChange = {
                    change -> emailField = TextFieldValue(change)
            },
            placeholder = {
                Text(text = "enter email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = passwordField.text,
            onValueChange = {
                    change -> passwordField = TextFieldValue(change)
            },
            placeholder = {
                Text(text = "enter password")
            }
        )

        Spacer(Modifier.height(32.dp))
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = {
                auth.createUserWithEmailAndPassword(emailField.text, passwordField.text)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            navController.navigate(Screen.SignIn.route)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

            },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Blue
                ),
                border = BorderStroke(ButtonDefaults.outlinedButtonBorder.width, Green300)
            ) {
                Text(text = "Sign Up")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                navController.navigate(Screen.SignIn.route)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueGray200,
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Sign In", color = Color.White)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SignInPreview() {
    val auth = Firebase.auth
    val navController = rememberNavController()
    val navModel: NavViewModel = viewModel()
    ComposeTodoListAppTheme() {
        SignInScreen(
            auth = auth,
            navController = navController,
            navModel = navModel
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SignUpPreview() {

}
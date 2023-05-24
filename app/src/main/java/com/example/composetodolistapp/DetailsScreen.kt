package com.example.composetodolistapp

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composetodolistapp.db.DatabaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "Details Screen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    colorId: String?,
    dbModel: DatabaseViewModel,
    changeBarColor: (Color) -> Unit,
    action: String?,
    firestoreDb: FirebaseFirestore,
    uEmail: String?,
    todoId: String?,
    modifier: Modifier = Modifier
) {
    var todo: Todo? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var color: Color? by remember { mutableStateOf(null) }
    colorId?.let {
        color = colorResource(it.toInt())
    }
    LaunchedEffect(key1 = Unit) {
//        var todo: Todo? by mutableStateOf(null)
        color?.let {
            changeBarColor(it)
        }
        scope.launch {
            todoId?.let { todo = dbModel.retrieveTodoById(it.toLong()) }
            todo?.let {
                title = TextFieldValue(it.title)
                content = TextFieldValue(it.content)
            }
        }
    }

    if (colorId != null) {
        Card(
//            backgroundColor = Color.Blue,
            modifier = modifier
        ) {
            Box {
                todo?.let {
                    Text(text = it.dateCreated.toString(), modifier = Modifier.align(Alignment.BottomEnd))
                }
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(value = title.text, onValueChange = { change -> title = TextFieldValue(change) },
                        singleLine = true, placeholder = { Text(text = "Enter a title", style = MaterialTheme.typography.titleLarge) },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorResource(colorId.toInt()),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(value = content.text, onValueChange = { change -> content = TextFieldValue(change) },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorResource(colorId.toInt()),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            when(action) {
                "ADD" -> {
                    if (title.text.isNotEmpty() || content.text.isNotEmpty()) {
                        val generatedContents = dbModel.storeTodoInDB(
                            title = title.text,
                            content = content.text
                        )
                        uEmail?.let {
                            firestoreDb.collection(it).document(generatedContents.first.toString())
                                .set(
                                    hashMapOf(
                                        "id" to generatedContents.first,
                                        "title" to title.text,
                                        "content" to content.text,
                                        "dateCreated" to generatedContents.second
                                    )
                                )
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                        }
                    }
                }
                "UPDATE" -> {
                    if (title.text.isNotEmpty() || content.text.isNotEmpty()) {
                        var updatedTodo: Todo? = null
                        todo?.let {
                            updatedTodo = Todo(
                                title = title.text,
                                content = content.text,
                                id = it.id,
                                dateCreated = it.dateCreated
                            )
                        }
                        updatedTodo?.let { result ->
                            dbModel.updateTodoInDB(
                                result
                            )
                            uEmail?.let {
                                firestoreDb.collection(it).document(result.id.toString())
                                    .set(
                                        hashMapOf(
                                            "id" to result.id,
                                            "title" to result.title,
                                            "content" to result.content,
                                            "dateCreated" to result.dateCreated
                                        )
                                    )
                                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    val dateTime = LocalDateTime.now()
    val dbModel: DatabaseViewModel = viewModel()
    val todo = Todo(191919, "Supplies", "Toilet Paper, Books\n\n\n\n", dateTime)
//    ComposeTodoListAppTheme() {
//        DetailsScreen(todo = todo, dbModel = dbModel, action = "ADD")
//    }
}

package com.example.composetodolistapp


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composetodolistapp.db.DatabaseViewModel
import com.example.composetodolistapp.navigation.NavViewModel
import com.example.composetodolistapp.navigation.Screen
import com.example.gym.ui.theme.Green300
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Todo Screen"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodosScreen(
    navController: NavController,
    dbModel: DatabaseViewModel,
    navModel:NavViewModel,
    firestoreDb: FirebaseFirestore,
    uEmail: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var todos = dbModel.retrieveTodosFromDB().collectAsState(initial = listOf())

    Box {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(count = 2),
            modifier = modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(todos.value) { index, item ->
                val colorId = getColorId(index)
                TodoSection(
                    todo = item, deleteItem = { todo ->
                        dbModel.deleteTodoInDB(todo)
                        uEmail?.let {
                            firestoreDb.collection(it).document(todo.id.toString())
                                .delete()
                                .addOnSuccessListener {
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot successfully deleted!"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        TAG,
                                        "Error deleting document",
                                        e
                                    )
                                }
                        }

                    },
                    editItem = { id ->
                        navController.navigate(
                            Screen.TodoDetails.withOptArgs(
                                "UPDATE" to null,
                                null to null,
                                "todoId" to id.toString(),
                                "colorId" to colorId.toString()
                            )
                        )
                        navModel.switchScreen(Screen.TodoDetails)
                    },
                    color = colorId
                )

            }

            if (todos.value.isEmpty()) {
                item {
                    Text(
                        text = "No existing todos",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(
            onClick = {
                val colorId = getColorId(todos.value.size)
                navController.navigate(
                    Screen.TodoDetails.withOptArgs(
                        "ADD" to null,
                        null to null,
                        "colorId" to colorId.toString(),
                    )
                )
//            navController.navigate("details/ADD?colorId=${getColorId(todos.value.size)}")
                navModel.switchScreen(Screen.TodoDetails)
            },
            containerColor = Green300,
            modifier = Modifier
                .size(128.dp)
                .padding(32.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24), contentDescription = null,
                tint = Color.White, modifier = Modifier.size(56.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoSection(
    todo: Todo,
    deleteItem: (Todo) -> Unit,
    editItem: (Long) -> Unit,
    color: Int,
    modifier: Modifier = Modifier
) {
    Card(onClick = { editItem(todo.id) }, modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = colorResource(color))
        ) {
        Column (
            modifier = modifier.padding(8.dp)
                ){
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) {
                if (todo.title.isNotEmpty()) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .weight(1f)
                    )
                    DeleteButton(deleteItem = { item -> deleteItem(item) }, todo = todo,
                        modifier = Modifier.weight(0.1f))
                }

            }
            if (todo.content.isNotEmpty()) {
                Row (
                    verticalAlignment = Alignment.Top,
                ){
                    Text(text = todo.content, maxLines = 8, style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (todo.title.isEmpty()) {
                        DeleteButton(deleteItem = { item -> deleteItem(item) }, todo = todo, modifier = Modifier.weight(0.1f))
                    }
                }

            }
        }
    }

}

fun getColorId(index: Int): Int {
    if (index%5 == 0) {
        return R.color.light_purple
    } else if (index%4 == 0) {
        return R.color.light_red
    } else if (index%3 == 0) {
        return R.color.light_green
    } else if (index%2 == 0) {
        return R.color.light_blue
    }
    return R.color.light_yellow
}

@Composable
fun DeleteButton(
    deleteItem: (Todo) -> Unit,
    todo: Todo,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            deleteItem(todo)
        },
        modifier = modifier
    ) {
        Icon(
            Icons.Filled.Close, contentDescription = "Delete item"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ComposeTodoListAppTheme {
//        TodosScreen()
//    }
}
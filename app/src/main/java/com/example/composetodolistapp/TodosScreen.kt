package com.example.composetodolistapp


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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

@Composable
fun TodosScreen(
    navController: NavController,
    dbModel: DatabaseViewModel,
    navModel:NavViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var todos = dbModel.retrieveTodosFromDB().collectAsState(initial = listOf())

    Box {
        LazyColumn (
            modifier = modifier.padding(horizontal = 16.dp)
        ){
            itemsIndexed(todos.value) { index, item ->
                val color = getColorId(index)
                TodoSection(todo = item, deleteItem = { todo ->
                    dbModel.deleteTodoInDB(todo)
                },
                    editItem = { id ->
//                        navController.navigate(
////                        Screen.TodoDetails.withMultOptArgs(
////                        "todoId" to id.toString(),
////                        "colorId" to color.toString()
//                            "details/UPDATE?todoId=$id&colorId=$color"
//                    )
//                        )
                        navController.navigate(Screen.TodoDetails.withOptArgs(
                            "UPDATE" to null,
                            null to null,
                            "todoId" to id.toString(),
                            "colorId" to color.toString()
                        ))
                        navModel.switchScreen(Screen.TodoDetails)
                    },
                    color = color
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (todos.value.isEmpty()) {
                item {
                    Text(text = "No existing todos", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        FloatingActionButton(onClick = {
            navController.navigate(Screen.TodoDetails.withOptArgs(
                "ADD" to null,
                null to null,
                "colorId" to getColorId(todos.value.size).toString(),
            ))
//            navController.navigate("details/ADD?colorId=${getColorId(todos.value.size)}")
            navModel.switchScreen(Screen.TodoDetails)
        },
            containerColor = Green300,
            modifier = Modifier
                .size(128.dp)
                .padding(32.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(painter = painterResource(R.drawable.baseline_add_24), contentDescription = null,
                tint = Color.White, modifier = Modifier.size(56.dp)
            )
        }
    }

}

@Composable
fun TodoSection(
    todo: Todo,
    deleteItem: (Todo) -> Unit,
    editItem: (Long) -> Unit,
    color: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                editItem(todo.id)
            }
            .background(colorResource(color))
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(
                text = todo.title,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = {
                          deleteItem(todo)
                },
                modifier = Modifier.weight(0.1f)
            ) {
                Icon(
                    Icons.Filled.Close, contentDescription = "Delete item"
                )
            }
        }
        Text(text = todo.content, maxLines = 3)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ComposeTodoListAppTheme {
//        TodosScreen()
//    }
}
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composetodolistapp.ui.theme.ComposeTodoListAppTheme
import java.io.Closeable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTodoListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")
                    ListOptions()
                }
            }
        }
    }
}

@Composable
fun ListOptions() {
    val context = LocalContext.current

    val model: ListViewModel = viewModel()
    var enteredValue by rememberSaveable {
        mutableStateOf("")
    }

    var editing by rememberSaveable {
        mutableStateOf(false)
    }

    var addText by rememberSaveable {
        mutableStateOf("Add todo")
    }

    var editText by rememberSaveable {
        mutableStateOf("Edit todo")
    }

    Column (
        modifier = Modifier.padding(horizontal = 16.dp)
            ){
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Button(
                onClick = {
                    Log.d("List option button", "Add clicked")
                    if (editing) {
                        model.editItem(enteredValue)
                    } else {
                        model.addItem(enteredValue)
                    }
                }
            ) {
                Text(text = addText)
            }

            Button(
                onClick = {
                    if (editing) {
                        editing = false
                        addText = "Add todo"
                        editText = "Edit todo"
                        model.editing(null)
                    } else {
                        Toast.makeText(context, "Click on a list item to edit", Toast.LENGTH_LONG).show()
                        editing = true
                        addText = "edit"
                        editText = "stop editing"
                    }
                }
            ) {
                Text(text = editText)
            }
        }
        Row(
        ) {
            TextField(
                value = enteredValue,
                onValueChange = { enteredValue = it },
                placeholder = {
                    Text("Enter todo")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn() {
            itemsIndexed(model.todos) { index, item ->
                var color: Int
                if (index%5 == 0) {
                    color = R.color.light_purple
                } else if (index%4 == 0) {
                    color = R.color.light_red
                } else if (index%3 == 0) {
                    color = R.color.light_green
                } else if (index%2 == 0) {
                    color = R.color.light_blue
                } else {
                    color = R.color.light_yellow
                }
                TodoItem(str = item, deleteItem = { item -> model.removeItem(item) },
                    editItem = { item -> model.editing(item) },
                    color = color
                    )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun TodoItem(
    str: String,
    deleteItem: (String) -> Unit,
    editItem: (String) -> Unit,
    color: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                editItem(str)
            }
            .background(colorResource(color))
        ) {
        Text(
            text = str,
            modifier = Modifier.weight(1f)
                .padding(start = 8.dp)
        )
        IconButton(
            onClick = { deleteItem(str) },
            modifier = Modifier.weight(0.1f)
        ) {
            Icon(
                Icons.Filled.Close, contentDescription = "Delete item"
            )
        }
    }
}

class ListViewModel : ViewModel() {
    private val _todos: SnapshotStateList<String> = mutableStateListOf()
    val todos: List<String>
        get() = _todos

    private var currentlyEditing: MutableState<Int?> = mutableStateOf(null)

    fun addItem(item: String) {
        _todos.add(item)
    }

    fun removeItem(item: String) {
        _todos.remove(item)
    }

    fun editItem(changedItem: String) {
        currentlyEditing.value?.let { _todos.set(it, changedItem) }
    }

    fun editing(item: String?) {
        if (item != null) {
            currentlyEditing.value = _todos.indexOf(item)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTodoListAppTheme {
        ListOptions()
    }
}
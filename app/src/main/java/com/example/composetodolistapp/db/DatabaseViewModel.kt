package com.example.composetodolistapp.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetodolistapp.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel  @Inject constructor(private val todoRepo: TodoRepository): ViewModel() {

    fun retrieveTodosFromDB(): Flow<List<Todo>> {
        return todoRepo.readAllData.map { list ->
            list.map { item ->
                Todo(
                    id = item.id,
                    dateCreated = item.dateCreated,
                    title = item.title,
                    content = item.content
                )
            }
        }
    }

    fun storeTodoInDB(title: String, content: String): Pair<Long, LocalDateTime> {
        val dateTime = LocalDateTime.now()
        val id = generateUniqueId()
        viewModelScope.launch {
            todoRepo.addTodo(
                TodoItem(
                    id = id,
                    title = title,
                    content = content,
                    dateCreated = dateTime
                )
            )
        }
        return Pair(id, dateTime)
    }

    fun deleteTodoInDB(todo: Todo) {
        viewModelScope.launch {
            todoRepo.deleteTodo(
                TodoItem(
                    title = todo.title,
                    content = todo.content,
                    id = todo.id,
                    dateCreated = todo.dateCreated
                )
            )
        }
    }

    fun updateTodoInDB(todo: Todo) {
        viewModelScope.launch {
            todoRepo.updateTodo(
                TodoItem(
                    title = todo.title,
                    content = todo.content,
                    id = todo.id,
                    dateCreated = todo.dateCreated
                )
            )
        }
    }

    suspend fun retrieveTodoById(id: Long): Todo {
        val routineItem = todoRepo.readTodoById(id)
        return Todo(
            id = routineItem.id,
            dateCreated = routineItem.dateCreated,
            title = routineItem.title,
            content = routineItem.content
        )
    }

    fun generateUniqueId(): Long {
        val uuid = UUID.randomUUID()
        return uuid.mostSignificantBits and Long.MAX_VALUE
    }
}
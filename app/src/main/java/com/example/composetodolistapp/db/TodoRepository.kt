package com.example.composetodolistapp.db

import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val databaseDao: DatabaseDao
) {
    val readAllData: Flow<List<TodoItem>> = databaseDao.getAll()

    suspend fun addTodo(item: TodoItem) {
        databaseDao.insert(item)
    }

    suspend fun updateTodo(item: TodoItem) {
        databaseDao.update(item)
    }

    suspend fun deleteTodo(item: TodoItem) {
        databaseDao.delete(item)
    }

    suspend fun readTodoById(id: Long): TodoItem {
        return databaseDao.getTodoById(id)
    }
}
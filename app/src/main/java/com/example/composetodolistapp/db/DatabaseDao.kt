package com.example.composetodolistapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Query("SELECT * from todo_list")
    fun getAll(): Flow<List<TodoItem>>

    @Insert
    suspend fun insert(item: TodoItem)

    @Update
    suspend fun update(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("SELECT * FROM todo_list WHERE id=:id")
    suspend fun getTodoById(id: Long): TodoItem

}
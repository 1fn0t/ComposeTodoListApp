package com.example.composetodolistapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalDateTime

@Entity(tableName = "todo_list")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val content: String,
    @ColumnInfo(name = "date_created")
    val dateCreated: LocalDateTime
)

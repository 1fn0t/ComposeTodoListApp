package com.example.composetodolistapp

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class Todo(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val dateCreated: LocalDateTime
)
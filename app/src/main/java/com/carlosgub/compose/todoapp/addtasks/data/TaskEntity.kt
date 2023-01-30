package com.carlosgub.compose.todoapp.addtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val task: String,
    var selected: Boolean
)
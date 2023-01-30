package com.carlosgub.compose.todoapp.addtasks.ui.model

import java.util.*

data class TaskModel(
    val id: String = UUID.randomUUID().toString(),
    val task: String,
    var selected: Boolean = false
)

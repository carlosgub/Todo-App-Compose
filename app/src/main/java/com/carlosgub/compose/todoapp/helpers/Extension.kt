package com.carlosgub.compose.todoapp.helpers

import com.carlosgub.compose.todoapp.addtasks.data.TaskEntity
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel

fun TaskModel.mapToTaskEntity(): TaskEntity =
    TaskEntity(
        id = id,
        task = task,
        selected = selected
    )

fun TaskEntity.mapToTaskModel(): TaskModel =
    TaskModel(
        id = id,
        task = task,
        selected = selected
    )
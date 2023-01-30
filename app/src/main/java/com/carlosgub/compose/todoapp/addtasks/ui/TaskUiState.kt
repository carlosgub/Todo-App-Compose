package com.carlosgub.compose.todoapp.addtasks.ui

import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel

sealed interface TaskUiState {
    object Loading : TaskUiState
    data class Error(val message: String) : TaskUiState
    data class Success(val tasks: List<TaskModel>) : TaskUiState
}
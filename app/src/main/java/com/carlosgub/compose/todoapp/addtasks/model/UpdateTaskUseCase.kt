package com.carlosgub.compose.todoapp.addtasks.model

import com.carlosgub.compose.todoapp.addtasks.data.TaskRepository
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel): Unit = taskRepository.updateTask(taskModel)
}
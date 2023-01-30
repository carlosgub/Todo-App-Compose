package com.carlosgub.compose.todoapp.addtasks.model

import com.carlosgub.compose.todoapp.addtasks.data.TaskRepository
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel): Unit = taskRepository.addTask(taskModel)
}
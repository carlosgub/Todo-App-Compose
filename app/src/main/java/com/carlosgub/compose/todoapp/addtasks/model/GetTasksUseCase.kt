package com.carlosgub.compose.todoapp.addtasks.model

import com.carlosgub.compose.todoapp.addtasks.data.TaskRepository
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = taskRepository.tasks
}
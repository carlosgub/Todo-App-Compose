package com.carlosgub.compose.todoapp.addtasks.data

import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel
import com.carlosgub.compose.todoapp.helpers.mapToTaskEntity
import com.carlosgub.compose.todoapp.helpers.mapToTaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val tasks: Flow<List<TaskModel>> = taskDao.getTasks().map { items ->
        items.map {
            it.mapToTaskModel()
        }
    }

    suspend fun addTask(taskModel: TaskModel) {
        taskDao.addTask(taskModel.mapToTaskEntity())
    }

    suspend fun updateTask(taskModel: TaskModel) {
        taskDao.updateTask(taskModel.mapToTaskEntity())
    }

    suspend fun deleteTask(taskModel: TaskModel) {
        taskDao.deleteTask(taskModel.mapToTaskEntity())
    }
}
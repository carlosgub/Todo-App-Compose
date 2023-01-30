package com.carlosgub.compose.todoapp.addtasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.compose.todoapp.addtasks.model.AddTaskUseCase
import com.carlosgub.compose.todoapp.addtasks.model.DeleteTaskUseCase
import com.carlosgub.compose.todoapp.addtasks.model.GetTasksUseCase
import com.carlosgub.compose.todoapp.addtasks.model.UpdateTaskUseCase
import com.carlosgub.compose.todoapp.addtasks.ui.TaskUiState.*
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase()
        .map(::Success)
        .catch { Error(it.message.orEmpty()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun showDialog() {
        _showDialog.value = true
    }

    fun onTaskCreated(task: String) {
        dismissDialog()
        viewModelScope.launch {
            addTaskUseCase.invoke(
                TaskModel(
                    task = task
                )
            )
        }
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        viewModelScope.launch {
            updateTaskUseCase(
                taskModel.copy(
                    selected = !taskModel.selected
                )
            )
        }
    }

    fun removeTask(taskModel: TaskModel) {
        viewModelScope.launch {
            deleteTaskUseCase(
                taskModel
            )
        }
    }
}
package com.carlosgub.compose.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.carlosgub.compose.todoapp.addtasks.ui.model.TaskModel


@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    value = it
                }
            }
        }
    )


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (fab, rv) = createRefs()
        val showDialog by viewModel.showDialog.observeAsState(initial = false)

        RecyclerTask(modifier = Modifier.constrainAs(rv) {
            linkTo(start = parent.start, end = parent.end)
            linkTo(top = parent.top, bottom = parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }, uiState = uiState, viewModel = viewModel)

        TaskFab(
            modifier = Modifier.constrainAs(fab) {
                bottom.linkTo(parent.bottom, 16.dp)
                end.linkTo(parent.end, 16.dp)
            },
            viewModel = viewModel
        )

        if (showDialog) {
            TaskDialog(viewModel)
        }

    }
}

@Composable
fun TaskDialog(viewModel: TaskViewModel) {
    var myTask by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { viewModel.dismissDialog() }) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(24.dp))
        ) {
            val (text, textField, button) = createRefs()

            Text(text = "Añade tu tarea",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        startMargin = 16.dp,
                        endMargin = 16.dp,
                        end = parent.end
                    )
                    top.linkTo(parent.top, 16.dp)
                })

            TextField(value = myTask, onValueChange = {
                myTask = it
            }, placeholder = {
                Text(
                    "Ingresa tu tarea",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }, singleLine = true, maxLines = 1, textStyle = TextStyle(
                fontSize = 12.sp, fontWeight = FontWeight.Medium
            ), colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ), modifier = Modifier
                .height(48.dp)
                .constrainAs(textField) {
                    linkTo(
                        start = parent.start,
                        startMargin = 16.dp,
                        endMargin = 16.dp,
                        end = parent.end
                    )
                    top.linkTo(text.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                })

            Button(onClick = {
                viewModel.onTaskCreated(myTask)
                myTask = ""
            }, modifier = Modifier.constrainAs(button) {
                linkTo(
                    start = parent.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    end = parent.end
                )
                linkTo(
                    top = textField.bottom,
                    topMargin = 8.dp,
                    bottom = parent.bottom,
                    bottomMargin = 16.dp
                )
                width = Dimension.fillToConstraints
            }) {
                Text("Añadir tarea")
            }
        }
    }
}

@Composable
fun RecyclerTask(modifier: Modifier, uiState: TaskUiState, viewModel: TaskViewModel) {
    LazyColumn(modifier = modifier) {
        items(getListFromUiState(uiState), key = { it.id }) { task ->
            ItemTask(taskModel = task, viewModel = viewModel)
        }
    }
}

private fun getListFromUiState(
    uiState: TaskUiState,
): List<TaskModel> = when (uiState) {
    is TaskUiState.Error -> listOf()
    TaskUiState.Loading -> listOf()
    is TaskUiState.Success -> {
        uiState.tasks
    }
}


@Composable
fun TaskFab(modifier: Modifier, viewModel: TaskViewModel) {
    FloatingActionButton(
        onClick = { viewModel.showDialog() },
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, viewModel: TaskViewModel) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp, horizontal = 16.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    viewModel.removeTask(taskModel)
                })
            }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (text, checkbox, divider) = createRefs()

            Text(text = taskModel.task, modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = parent.start,
                    startMargin = 16.dp,
                    end = checkbox.start,
                    endMargin = 8.dp
                )
                linkTo(
                    top = parent.top,
                    topMargin = 16.dp,
                    bottom = parent.bottom,
                    bottomMargin = 16.dp
                )
                width = Dimension.fillToConstraints
            })

            Checkbox(checked = taskModel.selected, onCheckedChange = {
                viewModel.onCheckBoxSelected(taskModel)
            }, modifier = Modifier.constrainAs(checkbox) {
                end.linkTo(parent.end, 16.dp)
                linkTo(
                    top = parent.top,
                    topMargin = 16.dp,
                    bottom = parent.bottom,
                    bottomMargin = 16.dp
                )
            })
        }
    }

}
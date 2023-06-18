package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val taskTitleRepo : TaskTitlesRepo,
): ViewModel() {


    val lastTaskTitleID = taskTitleRepo.getLastTaskTitleID()


    fun allTasksOfDisplayedProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return tasksRepo.getTasksOfProject(projectID)
    }

    fun insertTaskTitle(taskTitle: TaskTitles){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.insertTaskTitle(taskTitle)
        }
    }

    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?> {
        return taskTitleRepo.getTaskTitlesOfProject(projectID)
    }

    fun deleteTaskTitle(taskTitle: TaskTitles){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.deleteTaskTitle(taskTitle)
        }
    }

    fun updateTaskTitle(taskTitle: TaskTitles, oldTitle: String?){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.updateTaskTitle(taskTitle)
            tasksRepo.updateTaskStatus(oldTitle!!,taskTitle.taskTitle)
        }
    }


}



package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val taskTitleRepo : TaskTitlesRepo,
    private val sharedPreference: SessionManager
): ViewModel() {

    val allTasksOfCurrentUser = tasksRepo.getTasksOfCurrentUser(getCurrentUserID())

    val lastTaskTitleID = taskTitleRepo.getLastTaskTitleID()


    fun allTasksOfDisplayedProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return tasksRepo.getTasksOfProject(projectID)
    }

    fun insertTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.insertTask(task)
        }
    }

    private fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
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

    fun updateTaskTitle(taskTitle: TaskTitles){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.updateTaskTitle(taskTitle)
        }
    }


}



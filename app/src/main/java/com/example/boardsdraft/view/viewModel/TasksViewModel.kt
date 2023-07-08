package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.User
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
    private val projectsRepo: ProjectsRepo,
    private val sharedPreferences: SessionManager
): ViewModel() {


    val lastTaskTitleID = taskTitleRepo.getLastTaskTitleID()

    var lastId = 0

    lateinit var taskTitlesOfProject: LiveData<List<String>>

    var taskTitles : List<String> = listOf()

    lateinit var membersOfProject : LiveData<List<User>>

    fun allTasksOfDisplayedProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return tasksRepo.getTasksOfProject(projectID)
    }

    fun getCurrentUserID():Int{
        return sharedPreferences.getLoggedInID()

    }

    fun getAllUsersOfProject(projectID:Int){
        viewModelScope.launch {
            membersOfProject = projectsRepo.getUsersByProjectId(projectID)
        }
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
            tasksRepo.deleteTaskByTitle(taskTitle.taskTitle)
        }
    }


    fun updateTaskTitle(taskTitle: TaskTitles, oldTitle: String?){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.updateTaskTitle(taskTitle)
            tasksRepo.updateTaskStatus(oldTitle!!,taskTitle.taskTitle)
        }
    }


    fun getAllTaskTitleNames(projectID: Int) {
        viewModelScope.launch {
            taskTitlesOfProject = taskTitleRepo.getAllTaskTitleNamesOfProject(projectID)
        }
    }


}



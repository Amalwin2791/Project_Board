package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val repo : ProjectsRepo,
    private val sharedPreferences: SessionManager,
    private val taskTitleRepo : TaskTitlesRepo
): ViewModel(){

    val lastTaskID = tasksRepo.getLastTaskID()

    lateinit var membersOfProject : LiveData<List<User>>

    fun getAllUsersOfProject(projectID: Int){
        membersOfProject = repo.getUsersByProjectId(projectID)
    }

    fun allTasksOfDisplayedProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return tasksRepo.getTasksOfProject(projectID)
    }

    fun insertTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.insertTask(task)
        }
    }
    fun getCurrentUserName(): String? {
        return sharedPreferences.getLoggedInName()
    }

    fun updateTask(task:Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.updateTask(task)
        }
    }

}
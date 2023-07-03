package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val repo : ProjectsRepo
): ViewModel(){

    lateinit var membersOfProject : LiveData<List<User>>

    fun getAllUsersOfProject(projectID: Int){
        membersOfProject = repo.getUsersByProjectId(projectID)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.updateTask(task)
        }
    }
}
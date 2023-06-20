package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTasksViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val sharedPreferences: SessionManager,
    private val taskTitleRepo : TaskTitlesRepo
) : ViewModel(){

    val allTasksOfCurrentUser = tasksRepo.getTasksOfCurrentUser(sharedPreferences.getLoggedInID())

    private var _task = MutableLiveData<Task>()

    val task: LiveData<Task>
        get() = _task

    fun getCurrentUserID(): Int{
        return sharedPreferences.getLoggedInID()
    }

    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?> {
        return taskTitleRepo.getTaskTitlesOfProject(projectID)
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.updateTask(task)
        }
    }

    fun getTaskByID(taskID: Int){
        viewModelScope.launch{
            _task.value = tasksRepo.getTaskByID(taskID)
        }
    }



}
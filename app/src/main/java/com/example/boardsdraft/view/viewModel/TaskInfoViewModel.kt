package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
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
class TaskInfoViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val sharedPreferences: SessionManager,
    private val taskTitleRepo : TaskTitlesRepo
) : ViewModel(){

    fun getCurrentUserID(): Int{
        return sharedPreferences.getLoggedInID()
    }

    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?> {
        return taskTitleRepo.getTaskTitlesOfProject(projectID)
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.deleteTask(task)
        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepo.updateTask(task)
        }
    }

}
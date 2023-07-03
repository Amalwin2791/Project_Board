package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskManagerViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val sharedPreferences: SessionManager,
    private val taskTitleRepo : TaskTitlesRepo
) : ViewModel(){

    private var _task = MutableLiveData<Task>()

    val task: LiveData<Task>
        get() = _task


    fun getCurrentUserID(): Int{
        return sharedPreferences.getLoggedInID()
    }

    fun getTaskByID(taskID: Int){
        viewModelScope.launch{
            _task.value = tasksRepo.getTaskByID(taskID)
        }
    }

}
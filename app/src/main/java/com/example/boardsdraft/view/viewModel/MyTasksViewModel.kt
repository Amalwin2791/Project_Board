package com.example.boardsdraft.view.viewModel


import androidx.lifecycle.ViewModel
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTasksViewModel @Inject constructor(
    tasksRepo: TasksRepo,
    sharedPreferences: SessionManager
) : ViewModel(){

    val allTasksOfCurrentUser = tasksRepo.getTasksOfCurrentUser(sharedPreferences.getLoggedInID())



}
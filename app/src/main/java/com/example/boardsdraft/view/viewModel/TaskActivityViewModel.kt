package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskActivityViewModel @Inject constructor(
    private val repo : ProjectsRepo,
    private val sharedPreference: SessionManager,
    private val tasksRepo: TasksRepo
) : ViewModel(){

    fun deleteUserProjectCrossRef(userID: Int, projectID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUserProjectCrossRef(UserProjectCrossRef(userID,projectID))
            tasksRepo.deleteTasksByProjectAndUser(projectID,userID)
        }
    }

    fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
    }
}
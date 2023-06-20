package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val repo : ProjectsRepo,
    private val sharedPreferences: SessionManager
) : ViewModel(){

    lateinit var membersOfProject : LiveData<List<User>>

    fun getAllUsersOfProject(projectID: Int){
        membersOfProject = repo.getUsersByProjectId(projectID)

    }

    fun getCurrentUserID():Int{
        return sharedPreferences.getLoggedInID()
    }

    fun removeUserFromProject(userID:Int,projectID: Int){
        viewModelScope.launch {
            repo.deleteUserProjectCrossRef(UserProjectCrossRef(userID,projectID))
        }
    }

}
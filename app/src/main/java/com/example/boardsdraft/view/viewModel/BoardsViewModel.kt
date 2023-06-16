package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repo : ProjectsRepo,
    private val sharedPreference: SessionManager
) : ViewModel(){

    val allBoardsOfUser: LiveData<List<ProjectsWithUsers>> = repo.getBoardsOfUser(getCurrentUserID())

    val lastProjectID = repo.getLastProjectID()
    suspend fun getProjectIdByProjectCode(projectCode: String): Int? {
        return withContext(Dispatchers.IO) {
            repo.getProjectIdByProjectCode(projectCode)
        }
    }


    fun insertBoard(project: Project){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertBoard(project)
        }
    }

    fun insertUserProjectCrossRef(projectID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUserProjectCrossRef(UserProjectCrossRef(sharedPreference.getLoggedInID(),projectID))
        }
    }

    fun deleteUserProjectCrossRef(userID: Int, projectID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUserProjectCrossRef(UserProjectCrossRef(userID,projectID))
        }
    }

    fun getCurrentUserName(): String?{
        return sharedPreference.getLoggedInName()
    }
    fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
    }


}
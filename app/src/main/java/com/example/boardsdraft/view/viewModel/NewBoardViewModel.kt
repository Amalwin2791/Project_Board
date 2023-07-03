package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBoardViewModel @Inject constructor(
    private val repo : ProjectsRepo,
    private val sharedPreference: SessionManager
) : ViewModel(){

    val lastProjectID = repo.getLastProjectID()

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

    fun getCurrentUserName(): String?{
        return sharedPreference.getLoggedInName()
    }

    fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
    }
}
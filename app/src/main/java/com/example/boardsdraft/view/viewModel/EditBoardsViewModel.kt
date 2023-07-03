package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBoardsViewModel @Inject constructor(
    private val repo: ProjectsRepo,
    private val tasksRepo: TasksRepo
): ViewModel() {

    private val _board = MutableLiveData<Project>()

    val board:LiveData<Project>
        get() = _board


    fun getBoardImage(projectID:Int){
        viewModelScope.launch {
            _board.value = repo.getProjectByID(projectID)
        }
    }

    fun updateBoard(board:Project){
        viewModelScope.launch() {
            repo.updateBoard(board)

        }
    }

    fun updateTasks(id:Int, name:String){
        viewModelScope.launch {
            tasksRepo.updateTasKProjectName(id, name)
        }
    }
}
package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.UserRepo
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowProfileViewModel @Inject constructor(
    private val repo: UserRepo,
): ViewModel() {

    private val _dataMonitor = MutableLiveData<Boolean>()
    val dataMonitor : LiveData<Boolean>
        get()=_dataMonitor

    private lateinit var currentUser: User

    val projectsInCommon: MutableLiveData<List<Project>> = MutableLiveData()

    fun getUser(userID: Int) {
        viewModelScope.launch {
            currentUser= repo.getUser(userID)
            _dataMonitor.value=true
        }
    }

    fun getCurrentUserName(): String {
        return currentUser.userName
    }

    fun getCurrentUserEmailID(): String {
        return currentUser.email
    }

    fun getCurrentUserImage(): ByteArray? {
        return currentUser.image
    }
    fun getCurrentUserDepartment():String?{
        return currentUser.department
    }

    fun getCurrentUserDesignation(): String?{
        return currentUser.designation
    }

    fun getProjectsInCommon(otherUserID: Int) {
        viewModelScope.launch {
            val commonProjects = repo.getCommonProjects(currentUser.userID, otherUserID)
            projectsInCommon.value = commonProjects
        }
    }
}
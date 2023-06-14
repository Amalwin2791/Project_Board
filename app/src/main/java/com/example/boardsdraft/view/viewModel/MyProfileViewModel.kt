package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.UserRepo
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val repo: UserRepo,
    private val sharedPreference: SessionManager
): ViewModel() {

    private val _dataMonitor = MutableLiveData<Boolean>()
    val dataMonitor : LiveData<Boolean>
        get()=_dataMonitor


    private lateinit var currentUser: User
    val projectsInCommon: MutableLiveData<List<Project>> = MutableLiveData()

    fun getCurrentUser() {
        viewModelScope.launch {
            currentUser= getCurrentUserEmailID()?.let { repo.getUser(it) }!!
            _dataMonitor.value=true
        }
    }

    fun getCurrentUserName(): String?{
        return sharedPreference.getLoggedInName()
    }

    fun getCurrentUserEmailID(): String?{
        return sharedPreference.getLoggedInEmail()
    }

    fun getCurrentUserImage(): ByteArray? {
        return currentUser.image
    }

    fun getCurrentUserPassword(): String?{
        return sharedPreference.getPassword()
    }

    fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
    }
    fun getCurrentUserDepartment():String?{
        return currentUser.department
    }

    fun getCurrentUserDesignation(): String?{
        return currentUser.designation
    }

    fun clearSession(){
        sharedPreference.clearSession()
    }

    fun getProjectsInCommon(otherUserID: Int) {
        viewModelScope.launch {
            val commonProjects = repo.getCommonProjects(getCurrentUserID(), otherUserID)
            projectsInCommon.value = commonProjects
        }
    }
}
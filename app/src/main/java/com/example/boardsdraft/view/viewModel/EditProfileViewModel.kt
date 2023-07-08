package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.UserRepo
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel  @Inject constructor(
    private val repo: UserRepo,
    private val sharedPreference: SessionManager,
    private val tasksRepo: TasksRepo,
    private val projectsRepo: ProjectsRepo
): ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    lateinit var currentUser: User

    fun getCurrentUser() {
        viewModelScope.launch {
            currentUser= getCurrentUserEmailID()?.let { repo.getUser(it) }!!
            _user.value = currentUser
        }
    }

    fun updateUser(user:User){
        viewModelScope.launch {
            sharedPreference.changeName(user.userName)
            sharedPreference.changeEmail(user.email)
            repo.updateUser(user)
        }
    }
    fun updateTaskAssignedToName(){
        viewModelScope.launch {
            tasksRepo.updateAssignedToName(currentUser.userID,currentUser.userName)

        }
    }
    fun updateTaskCreatedByName(){
        viewModelScope.launch {
            tasksRepo.updateCreatedByToName(currentUser.userID,currentUser.userName)
        }
    }
    fun updateCreatedByName(){
        viewModelScope.launch {
            projectsRepo.updateProjectCreatedByName(currentUser.userName,currentUser.userID)
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

    fun getCurrentUserDepartment():String?{
        return currentUser.department
    }

    fun getCurrentUserDesignation(): String?{
        return currentUser.designation
    }


}
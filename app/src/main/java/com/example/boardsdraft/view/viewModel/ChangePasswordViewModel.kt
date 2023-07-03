package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.UserRepo
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repo: UserRepo,
    private val sharedPreference: SessionManager
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

    private fun getCurrentUserEmailID(): String?{
        return sharedPreference.getLoggedInEmail()
    }

    fun updateUser(user:User){
        viewModelScope.launch {
            sharedPreference.setLoggedIn(true,user.email,user.userID,user.userName,user.password)
            repo.updateUser(user)

        }
    }

    fun getCurrentUserPassword(): String?{
        return sharedPreference.getPassword()
    }
}
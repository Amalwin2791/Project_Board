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

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    lateinit var currentUser: User

    fun getCurrentUser() {
        viewModelScope.launch {
             getCurrentUserEmailID()?.let {currentUser= repo.getUser(it)!! }
            _user.value = currentUser
        }
    }



    fun getCurrentUserName(): String?{
        return sharedPreference.getLoggedInName()
    }

    private fun getCurrentUserEmailID(): String?{
        return sharedPreference.getLoggedInEmail()
    }

    fun getCurrentUserImage(): ByteArray? {
        return currentUser.image
    }

    fun getCurrentUserID(): Int{
        return sharedPreference.getLoggedInID()
    }

    fun clearSession(){
        sharedPreference.clearSession()
    }

//    suspend fun doesEmailExist(email: String):Boolean{
//        return repo.doesEmailIdExists(email)
//    }
}
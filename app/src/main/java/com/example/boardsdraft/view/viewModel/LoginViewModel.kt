package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.UserRepo
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.SessionManager
import com.example.boardsdraft.view.enums.LoginResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo : UserRepo,
    private val sharedPreference: SessionManager
) : ViewModel(){

    private val _signInStatus = MutableLiveData<LoginResults>()
    val signInStatus: LiveData<LoginResults> = _signInStatus

    private val _signUpStatus = MutableLiveData<LoginResults>()
    val signUpStatus: LiveData<LoginResults> = _signUpStatus

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    fun signIn(email: String?, password: String?) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _signInStatus.value = LoginResults.FIELD_IS_NULL
            return
        }
        val emailRegex = Regex("^([a-zA-Z0-9_.+-]+)@([a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)$")
        if(!emailRegex.matches(email)){
            _signInStatus.value = LoginResults.INVALID_EMAIL
            return
        }

        viewModelScope.launch {
            val success = repo.signIn(email, password)

            if (success == LoginResults.LOGIN_SUCCESSFUL) {
                val user = repo.getUser(email)
                if (user != null) {
                    sharedPreference.setLoggedIn(true,user.email, user.userID,user.userName,password ,false)
                }
            }
            _signInStatus.value = success

        }
    }

    fun updateUser(user:User){
        viewModelScope.launch {
            repo.updateUser(user)
        }
    }
    fun getUserByEmailID(emailID:String){
        viewModelScope.launch {
            _user.value = repo.getUser(emailID)
        }
    }

    fun signUp(username: String?, email: String?, password: String?, retypePassword : String?) {
        if (username.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || retypePassword.isNullOrEmpty() ) {
            _signUpStatus.value = LoginResults.FIELD_IS_NULL
            return
        }
        if (password != retypePassword) {
            _signUpStatus.value = LoginResults.PASSWORDS_DONT_MATCH
            return
        }
        val emailRegex = Regex("^([a-zA-Z0-9_.+-]+)@([a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)$")
        if(!emailRegex.matches(email)){
            _signInStatus.value = LoginResults.INVALID_EMAIL
            return
        }

        val passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
        if (!passwordRegex.matches(password)) {
            _signUpStatus.value = LoginResults.PASSWORD_DONT_MEET_REQUIREMENT
            return
        }

        viewModelScope.launch {
            val success = repo.signUp(username, email, password, retypePassword)
            _signUpStatus.value = success
            if (success == LoginResults.LOGIN_SUCCESSFUL) {
                val user = repo.getUser(email)
                if (user != null) {
                    sharedPreference.setLoggedIn(true,email,user.userID,username,password,false)
                }
            }
        }
    }


}
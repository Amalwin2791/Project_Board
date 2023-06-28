package com.example.boardsdraft.db

import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.enums.LoginResults

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepoImp @Inject constructor(
    private val dao : UserDAO
) : UserRepo {

    override suspend fun signIn(email: String, password: String): LoginResults {
        return withContext(Dispatchers.IO) {
            val user = dao.getUserByEmail(email) ?: return@withContext LoginResults.USER_NOT_PRESENT

            if (user.password !=password){
                return@withContext LoginResults.INVALID_PASSWORD
            }
            else {
                return@withContext LoginResults.LOGIN_SUCCESSFUL
            }
        }
    }

    override suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        retypePassword: String
    ): LoginResults {
        return withContext(Dispatchers.IO) {
            val existingUser = dao.getUserByEmail(userName)
            if (existingUser != null) {
                return@withContext LoginResults.USER_ALREADY_PRESENT
            }
            else {
                val newUser = User(userName = userName, email = email, password = password, image = null, department = null,designation = null)
                dao.insertUser(newUser)
                return@withContext LoginResults.LOGIN_SUCCESSFUL
            }
        }
    }

    override suspend fun getUser(email: String): User? {
        return dao.getUserByEmail(email)
    }

    override suspend fun getUser(userID: Int): User {
        return dao.getUser(userID)
    }

    override suspend fun getCommonProjects(
        currentUserID: Int,
        otherUserID: Int
    ): List<Project> {
        return dao.getCommonProjects(currentUserID,otherUserID)
    }

//    override suspend fun doesEmailIdExists(emailId: String): Boolean {
//        return dao.doesEmailIdExists(emailId)
//    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }


}
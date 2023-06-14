package com.example.boardsdraft.db

import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.view.enums.LoginResults

interface UserRepo {
    suspend fun signIn(email: String, password: String): LoginResults

    suspend fun signUp(userName: String, email: String, password: String,retypePassword: String): LoginResults

    suspend fun getUser(email: String): User?

    suspend fun getUser(userID: Int): User

    suspend fun getCommonProjects(currentUserID: Int, otherUserID: Int): List<Project>

    suspend fun doesEmailIdExists(emailId: String): Boolean

    suspend fun updateUser(user:User)


}
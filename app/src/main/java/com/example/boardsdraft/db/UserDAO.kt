package com.example.boardsdraft.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers


@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Users WHERE email = :email ")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM Users WHERE userID = :userID ")
    suspend fun getUser(userID: Int): User

    @Transaction
    @Query("SELECT * FROM Projects INNER JOIN UserProjectCrossRef ON Projects.projectID = UserProjectCrossRef.projectID WHERE UserProjectCrossRef.userID = :currentUserID AND UserProjectCrossRef.userID = :otherUserID")
    suspend fun getCommonProjects(currentUserID: Int, otherUserID: Int): List<ProjectsWithUsers>
}
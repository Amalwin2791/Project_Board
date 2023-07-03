package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers

@Dao
interface ProjectsDao {

    @Insert
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateBoard(project: Project)

    @Query("SELECT * FROM Projects")
    fun getAllProjects():LiveData<List<Project>>

    @Upsert
    suspend fun insertUserProjectCrossRef(crossRef: UserProjectCrossRef)


    @Delete
    suspend fun deleteUserProjectCrossRef(crossRef: UserProjectCrossRef)

    @Query("DELETE FROM Projects WHERE projectID = :projectID")
    suspend fun deleteProject(projectID: Int)


    @Transaction
    @Query("SELECT * FROM Projects " +
            "INNER JOIN UserProjectCrossRef ON Projects.projectID = UserProjectCrossRef.projectID " +
            "WHERE UserProjectCrossRef.userID = :userID")
    fun getBoardsOfUser(userID: Int): LiveData<List<ProjectsWithUsers>>

    @Query("SELECT projectID FROM Projects ORDER BY projectID DESC LIMIT 1")
    fun getLastProjectID(): LiveData<Int?>

    @Query("SELECT projectID FROM Projects WHERE projectCode = :code")
    suspend fun getProjectIdByProjectCode(code: String): Int?

    @Transaction
    @Query("SELECT * FROM Users INNER JOIN UserProjectCrossRef ON Users.userID = UserProjectCrossRef.userID WHERE UserProjectCrossRef.projectID = :projectId")
    fun getUsersByProjectId(projectId: Int): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT 1 FROM UserProjectCrossRef WHERE userID = :userID AND projectID = :projectID LIMIT 1)")
    suspend fun exists(userID: Int, projectID: Int): Boolean

    @Query("SELECT * FROM Projects WHERE projectID = :projectID")
    suspend fun getProjectByID(projectID: Int): Project

}
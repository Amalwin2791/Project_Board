package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers

interface ProjectsRepo {

    suspend fun insertBoard(project: Project)

    fun allBoards() : LiveData<List<Project>>

    suspend fun updateBoard(project: Project)

    suspend fun insertUserProjectCrossRef(crossRef: UserProjectCrossRef)

    suspend fun deleteUserProjectCrossRef(crossRef: UserProjectCrossRef)

    fun getLastProjectID(): LiveData<Int?>

    fun getBoardsOfUser(userID: Int): LiveData<List<ProjectsWithUsers>>

    suspend fun deleteBoard(projectID: Int)

    suspend fun getProjectIdByProjectCode(projectCode: String): Int?

    fun getUsersByProjectId(projectId: Int): LiveData<List<User>>

    suspend fun exists(userID: Int, projectID: Int): Boolean

    suspend fun getProjectByID(projectID: Int): Project
    suspend fun updateProjectCreatedByName(newName:String,id:Int)
}
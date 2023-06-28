package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers
import javax.inject.Inject


class ProjectsRepoImp @Inject constructor(
    private val dao: ProjectsDao
) : ProjectsRepo{
    private val boards = dao.getAllProjects()

    private val lastBoardID = dao.getLastProjectID()

    override suspend fun insertBoard(project: Project){
        dao.insertProject(project)
    }

    override fun allBoards(): LiveData<List<Project>> {
        return boards
    }

    override suspend fun updateBoard(project: Project) {
        dao.updateBoard(project)
    }

    override fun getLastProjectID(): LiveData<Int?> {
        return lastBoardID
    }

    override suspend fun insertUserProjectCrossRef(crossRef: UserProjectCrossRef) {
        dao.insertUserProjectCrossRef(crossRef)
    }

    override suspend fun deleteUserProjectCrossRef(crossRef: UserProjectCrossRef) {
        dao.deleteUserProjectCrossRef(crossRef)
    }

    override fun getBoardsOfUser(userID: Int): LiveData<List<ProjectsWithUsers>> {
        return dao.getBoardsOfUser(userID)
    }

    override suspend fun deleteBoard(projectID: Int) {
        dao.deleteProject(projectID)
    }

    override suspend fun getProjectIdByProjectCode(projectCode: String): Int? {
        return dao.getProjectIdByProjectCode(projectCode)
    }

    override fun getUsersByProjectId(projectId: Int): LiveData<List<User>> {
        return dao.getUsersByProjectId(projectId)
    }

    override suspend fun exists(userID: Int, projectID: Int): Boolean {
        return dao.exists(userID,projectID)
    }

    override suspend fun getProjectByID(projectID: Int): Project {
        return dao.getProjectByID(projectID)
    }


}
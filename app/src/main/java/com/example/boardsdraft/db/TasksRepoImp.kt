package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

class TasksRepoImp(
    private val dao: TasksDAO
): TasksRepo {

    private val lastTaskID = dao.getLastTaskID()
    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override fun getTasksOfCurrentUser(userID: Int): LiveData<List<UserWithTasks>?> {
        return dao.getTaskOfUser(userID)
    }

    override fun getTasksOfProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return dao.getTaskOfProject(projectID)
    }

    override fun getLastTaskID(): LiveData<Int?> {
        return lastTaskID
    }
}
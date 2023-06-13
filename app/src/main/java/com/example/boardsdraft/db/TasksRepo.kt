package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

interface TasksRepo {

    suspend fun insertTask(task: Task)

    fun getTasksOfCurrentUser(userID: Int): LiveData<List<UserWithTasks>?>

    fun getTasksOfProject(projectID: Int): LiveData<List<ProjectWithTasks>?>
}
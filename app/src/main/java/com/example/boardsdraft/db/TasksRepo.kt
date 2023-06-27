package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

interface TasksRepo {

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun getTasksOfCurrentUser(userID: Int): LiveData<List<UserWithTasks>?>

    fun getTasksOfProject(projectID: Int): LiveData<List<ProjectWithTasks>?>

    fun getLastTaskID(): LiveData<Int?>
    suspend fun updateTaskStatus(oldStatus: String, newStatus: String)
    suspend fun getTaskByID(taskID:Int): Task

    suspend fun deleteTasksByProjectAndUser(projectID: Int, userID: Int)
}
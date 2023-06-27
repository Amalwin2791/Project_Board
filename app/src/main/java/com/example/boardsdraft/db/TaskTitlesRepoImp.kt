package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import javax.inject.Inject

class TaskTitlesRepoImp @Inject constructor(
    val dao: TaskTitlesDAO
): TaskTitlesRepo {

    private val lastTaskTitleID = dao.getLastTaskTitleID()

    override suspend fun insertTaskTitle(taskTitle: TaskTitles) {
        dao.insertTaskTitle(taskTitle)
    }

    override suspend fun updateTaskTitle(taskTitle: TaskTitles) {
        dao.updateTaskTitle(taskTitle)
    }

    override suspend fun deleteTaskTitle(taskTitle: TaskTitles) {
        dao.deleteTaskTitle(taskTitle)
    }

    override fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?> {
        return dao.getTaskTitlesOfProject(projectID)
    }
    override fun getLastTaskTitleID(): LiveData<Int?> {
        return lastTaskTitleID
    }

    override suspend fun getAllTaskTitleNamesOfProject(projectID: Int): List<String?> {
        return dao.getAllTaskTitleNamesOfProject(projectID)
    }




}
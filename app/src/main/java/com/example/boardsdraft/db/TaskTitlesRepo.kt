package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject

interface TaskTitlesRepo {

    suspend fun insertTaskTitle(taskTitle: TaskTitles)

    suspend fun updateTaskTitle(taskTitle: TaskTitles)

    suspend fun deleteTaskTitle(taskTitle: TaskTitles)

    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?>

    fun getLastTaskTitleID(): LiveData<Int?>

    fun getAllTaskTitleNamesOfProject(projectID:Int): LiveData<List<String>>


}
package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject

@Dao
interface TaskTitlesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskTitle(taskTitle: TaskTitles)

    @Update
    suspend fun updateTaskTitle(taskTitle: TaskTitles)

    @Delete
    suspend fun deleteTaskTitle(taskTitle: TaskTitles)

    @Transaction
    @Query("SELECT * FROM Projects WHERE projectID= :projectID")
    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?>

    @Query("SELECT taskTitleID FROM task_titles ORDER BY taskTitleID DESC LIMIT 1")
    fun getLastTaskTitleID(): LiveData<Int?>

    @Query("SELECT taskTitle FROM Task_Titles WHERE projectID = :projectID")
    fun getAllTaskTitleNamesOfProject(projectID:Int): LiveData<List<String>>


}
package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

@Dao
interface TasksDAO {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Transaction
    @Query("SELECT * FROM PROJECTS WHERE projectID = :projectID")
    fun getTaskOfProject(projectID: Int) : LiveData<List<ProjectWithTasks>?>

    @Transaction
    @Query("SELECT * FROM USERS WHERE userID = :userID")
    fun getTaskOfUser(userID : Int) : LiveData<List<UserWithTasks>?>
}
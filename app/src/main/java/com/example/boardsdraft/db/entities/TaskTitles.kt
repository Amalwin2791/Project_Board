package com.example.boardsdraft.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task_Titles")
data class TaskTitles(
    @PrimaryKey(autoGenerate = true)
    val taskTitleID: Int,
    var taskTitle: String,
    val projectID: Int
)


package com.example.boardsdraft.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Task(

    var taskName: String,
    val projectID : Int,
    val projectName: String,
    var assignedTo: Int,
    var assignedToName: String,
    val createdBy: String,
    var status: String,
    var priority: String,
    val createdDate: String,
    var deadLine: String,

    @PrimaryKey(autoGenerate = true)
    val taskID: Int
)

package com.example.boardsdraft.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskID: Int,
    val taskName: String,
    val projectID : Int,
    val assignedTo: Int,
    val createdBy: String,
    val status: String
)

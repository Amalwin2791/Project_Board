package com.example.boardsdraft.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Projects")
data class Project(

    @PrimaryKey(autoGenerate = true)
    val projectID: Int,
    var projectName: String,
    var image : ByteArray?,
    val createdBy: String,
    val projectCode: String
)

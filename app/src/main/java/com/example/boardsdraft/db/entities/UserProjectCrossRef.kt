package com.example.boardsdraft.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["userID", "projectID"])
data class UserProjectCrossRef(
    val userID : Int,
    val projectID : Int
)


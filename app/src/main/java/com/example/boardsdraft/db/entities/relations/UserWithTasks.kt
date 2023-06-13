package com.example.boardsdraft.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User

data class UserWithTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userID",
        entityColumn = "assignedTo"
    )
    val tasks: List<Task?>
)

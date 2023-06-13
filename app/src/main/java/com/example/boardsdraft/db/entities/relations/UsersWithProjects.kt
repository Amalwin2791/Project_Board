package com.example.boardsdraft.db.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef


data class UsersWithProjects(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userID",
        entityColumn = "projectID",
        associateBy = Junction(UserProjectCrossRef::class)
    )
    val projects: List<Project>

)

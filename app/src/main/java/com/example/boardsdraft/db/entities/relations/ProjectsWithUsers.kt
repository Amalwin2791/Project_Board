package com.example.boardsdraft.db.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef



data class ProjectsWithUsers(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "projectID",
        entityColumn = "userID",
        associateBy = Junction(UserProjectCrossRef::class)
    )
    val users: List<User>
)

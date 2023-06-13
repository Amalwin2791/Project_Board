package com.example.boardsdraft.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.Task

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "projectID",
        entityColumn = "projectID"
    )
    val tasks: List<Task?>
)

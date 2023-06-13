package com.example.boardsdraft.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.boardsdraft.db.entities.Project
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.UserProjectCrossRef


@Database(
    entities = [
        Project::class,
        User::class,
        UserProjectCrossRef::class,
        Task::class,
        TaskTitles::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val userDAO : UserDAO
    abstract val projectDao : ProjectsDao
    abstract val tasksDAO : TasksDAO
    abstract val taskTitlesDAO : TaskTitlesDAO

}
package com.example.boardsdraft.db

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.boardsdraft.view.SessionManager
import com.example.boardsdraft.view.viewModel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(app : Application) : Database{
        return  Room.databaseBuilder(
            app,
            Database::class.java,
            "database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesUserRepo(database: Database):  UserRepo{
        return UserRepoImp(database.userDAO)
    }
    @Provides
    @Singleton
    fun providesProjectsRepo(database: Database):  ProjectsRepo{
        return ProjectsRepoImp(database.projectDao)
    }

    @Provides
    @Singleton
    fun providesTasksRepo(database: Database):  TasksRepo{
        return TasksRepoImp(database.tasksDAO)
    }
    @Provides
    @Singleton
    fun providesTaskTitlesRepo(database: Database):  TaskTitlesRepo{
        return TaskTitlesRepoImp(database.taskTitlesDAO)
    }

    @Provides
    @Singleton
    fun provideSessionManager(context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    fun provideLoginViewModel(
        sharedPreference: SessionManager,
        userRepo: UserRepo
    ): LoginViewModel {
        return LoginViewModel(userRepo,sharedPreference)
    }
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}
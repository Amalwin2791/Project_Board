package com.example.boardsdraft.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardsdraft.db.ProjectsRepo
import com.example.boardsdraft.db.TaskTitlesRepo
import com.example.boardsdraft.db.TasksRepo
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepo: TasksRepo,
    private val taskTitleRepo : TaskTitlesRepo,
    private val projectsRepo: ProjectsRepo
): ViewModel() {


    val lastTaskTitleID = taskTitleRepo.getLastTaskTitleID()

    private val _monitor = MutableLiveData<Boolean>()
    val monitor: LiveData<Boolean> = _monitor

    lateinit var taskTitlesOfProject: List<String?>

    lateinit var membersOfProject : LiveData<List<User>>

    fun allTasksOfDisplayedProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return tasksRepo.getTasksOfProject(projectID)
    }

    fun getAllUsersOfProject(projectID:Int){
        viewModelScope.launch {
            membersOfProject = projectsRepo.getUsersByProjectId(projectID)
        }
    }

    fun insertTaskTitle(taskTitle: TaskTitles){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.insertTaskTitle(taskTitle)
        }
    }

    fun getTaskTitlesOfProject(projectID: Int): LiveData<List<TaskTitlesOfProject>?> {
        return taskTitleRepo.getTaskTitlesOfProject(projectID)
    }

    fun deleteTaskTitle(taskTitle: TaskTitles){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.deleteTaskTitle(taskTitle)
            tasksRepo.deleteTaskByTitle(taskTitle.taskTitle)
        }
    }

    fun updateTaskTitle(taskTitle: TaskTitles, oldTitle: String?){
        viewModelScope.launch(Dispatchers.IO) {
            taskTitleRepo.updateTaskTitle(taskTitle)
            tasksRepo.updateTaskStatus(oldTitle!!,taskTitle.taskTitle)
        }
    }

    fun getAllTaskTitleNames(projectID: Int){
        viewModelScope.launch{
            taskTitlesOfProject = taskTitleRepo.getAllTaskTitleNamesOfProject(projectID)
            _monitor.value = true
        }
    }


}



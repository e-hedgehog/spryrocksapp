package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import javax.inject.Inject

class TaskDetailsViewModel : ViewModel() {

    val projectName = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()

    var isNewTask = true

    @Inject
    lateinit var databaseManager: DatabaseManager

    init {
        Application.appComponent.injectTasksDetailsViewModel(this)
    }

    fun saveTask() {
        databaseManager.saveNewTask(projectName.value!!, taskDescription.value!!)
    }

    fun updateCurrentTask(task: Task) {
        task.projectName = projectName.value!!
        task.taskDescription = taskDescription.value!!
        databaseManager.updateTask(task)
    }

    fun initTask(task: Task) {
        projectName.value = task.projectName
        taskDescription.value = task.taskDescription
    }

}
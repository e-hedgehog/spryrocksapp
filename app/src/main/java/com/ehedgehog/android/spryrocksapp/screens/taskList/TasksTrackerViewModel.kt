package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import javax.inject.Inject

class TasksTrackerViewModel: ViewModel() {

    @Inject
    lateinit var databaseManager: DatabaseManager

    private val _listTasks = MutableLiveData<List<Task>>()
    val listTasks: LiveData<List<Task>> get() = _listTasks

    private val _currentTask = MutableLiveData<Task>()
    val currentTask: LiveData<Task> get() = _currentTask

    private val _navigateToSelectedTaskById = MutableLiveData<Int>()
    val navigateToSelectedTaskById: LiveData<Int> get() = _navigateToSelectedTaskById

    init {
        Application.appComponent.injectTasksTrackerViewModel(this)
    }

    fun loadStoredTasks() {
        _listTasks.value = databaseManager.getStoredTasks()
    }

    fun initCurrentTask() {
        _currentTask.value = databaseManager.getStartedTask()
    }

    fun displayTaskDetails(id: Int = -1) {
        _navigateToSelectedTaskById.value = id
    }

    fun displayTaskDetailsComplete() {
        _navigateToSelectedTaskById.value = null
    }

}
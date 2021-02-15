package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import com.ehedgehog.android.spryrocksapp.screens.TaskTimerManager
import javax.inject.Inject

class TasksTrackerViewModel: ViewModel() {

    @Inject
    lateinit var databaseManager: DatabaseManager

    @Inject
    lateinit var timerManager: TaskTimerManager

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

    fun onInitializeCurrentTask(task: Task?) {
        task?.let(timerManager::initTimer)
    }

    fun unpauseTimer() {
        currentTask.value?.let(timerManager::unpauseTimerIfStarted)
    }

    fun pauseTimer() {
        timerManager.pauseTimer(currentTask.value)
    }

    fun stopTimer() {
        timerManager.stopTimer(currentTask.value)
    }

    fun onContextMenuItemClicked(item: MenuItem, task: Task): Boolean {
        return when (item.itemId) {
            R.id.context_delete_item -> {
                deleteTask(task)
                true
            }
            else -> false
        }
    }

    private fun deleteTask(task: Task) {
        currentTask.value?.let {
            if (task.id == it.id)
                stopTimer()
        }

        _listTasks.value = listTasks.value?.minus(task)
        databaseManager.deleteTask(task)
    }

}
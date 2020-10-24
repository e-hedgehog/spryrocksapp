package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import com.ehedgehog.android.spryrocksapp.screens.TaskTimerManager
import javax.inject.Inject

class TaskDetailsViewModel : ViewModel() {

    val projectName = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()

    private val _currentTask = MutableLiveData<Task>()
    val currentTask: LiveData<Task> get() = _currentTask

    private val _isNewTask = MutableLiveData<Boolean>()
    val isNewTask: LiveData<Boolean> get() = _isNewTask

    @Inject
    lateinit var databaseManager: DatabaseManager

    @Inject
    lateinit var timerManager: TaskTimerManager

    init {
        Application.appComponent.injectTasksDetailsViewModel(this)
        _isNewTask.value = true
    }

    fun onInitializeTask(currentTask: Task?) {
        currentTask?.let {
            initTask(it)
            _isNewTask.value = false
        }
    }

    fun unpauseTimerIfStarted() {
        currentTask.value?.let {
            timerManager.unpauseTimerIfStarted(it)
        }
    }

    fun onStartTimer() {
        timerManager.startTimer(currentTask.value)
    }

    fun onPauseTimer() {
        timerManager.pauseTimer(currentTask.value)
    }

    fun onStopTimer() {
        timerManager.stopTimer(currentTask.value)
    }

    fun onResetTimer() {
        timerManager.resetTimer(currentTask.value)
    }

    fun onSaveCurrentTask() {
        if (isNewTask.value!!)
            saveTask()
        else
            updateCurrentTask()
    }

    fun initTaskById(id: Int) {
        _currentTask.value = databaseManager.getTaskById(id)
    }

    private fun saveTask() {
        databaseManager.saveNewTask(projectName.value!!, taskDescription.value!!)
    }

    private fun updateCurrentTask() {
        currentTask.value?.let { task ->
            task.projectName = projectName.value!!
            task.taskDescription = taskDescription.value!!
            databaseManager.updateTask(task)
        }
    }

    private fun initTask(task: Task) {
        projectName.value = task.projectName
        taskDescription.value = task.taskDescription
        timerManager.initTimer(task)
    }

}
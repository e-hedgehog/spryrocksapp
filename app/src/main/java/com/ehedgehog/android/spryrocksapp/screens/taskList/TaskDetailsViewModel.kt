package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.network.Time
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskDetailsViewModel : ViewModel() {

    val projectName = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()

    private val _currentTask = MutableLiveData<Task>()
    val currentTask: LiveData<Task> get() = _currentTask

    private val _time = MutableLiveData<Time>()
    val time: LiveData<Time> get() = _time

    private val _isTimerStarted = MutableLiveData<Boolean>()
    val isTimerStarted: LiveData<Boolean> get() = _isTimerStarted

    private val _isNewTask = MutableLiveData<Boolean>()
    val isNewTask: LiveData<Boolean> get() = _isNewTask

    private var timerDisposable: Disposable? = null

    @Inject
    lateinit var databaseManager: DatabaseManager

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
            if (it.isStarted) {
                _isTimerStarted.value = true
                val currentTime = Date().time
                val interval = (currentTime - it.lastPause) / 1000
                startTimer(it.time, interval)
            }
        }
    }

    fun onStartTimer() {
        currentTask.value?.let {task ->
            task.isStarted = true
            updateCurrentTask(task)
        }

        _isTimerStarted.value = true
        if (currentTask.value != null)
            startTimer(currentTask.value!!.time, 0)
        else startTimer(null, null)
    }

    fun onPauseTimer() {
        currentTask.value?.let {
            if (it.isStarted) {
                stopTimer()
                it.time = time.value
                it.lastPause = Date().time
                updateCurrentTask(it)
            }
        }
    }

    fun onStopTimer() {
        currentTask.value?.let {task ->
            task.isStarted = false
            task.time = time.value
            task.lastPause = 0
            updateCurrentTask(task)
        }

        _isTimerStarted.value = false
        stopTimer()
    }

    fun onResetTimer() {
        currentTask.value?.let {task -> resetCurrentTask(task)}

        _isTimerStarted.value = false
        stopTimer()
    }

    fun onSaveCurrentTask() {
        if (isNewTask.value!!)
            saveTask()
        else
            currentTask.value?.let { task -> updateCurrentTask(task) }
    }

    fun initTaskById(id: Int) {
        _currentTask.value = databaseManager.getTaskById(id)
    }

    private fun saveTask() {
        databaseManager.saveNewTask(projectName.value!!, taskDescription.value!!)
    }

    private fun updateCurrentTask(task: Task) {
        task.projectName = projectName.value!!
        task.taskDescription = taskDescription.value!!
        databaseManager.updateTask(task)
    }

    private fun resetCurrentTask(task: Task) {
        task.isStarted = false
        task.time = Time(0, 0, 0, 0)
        task.lastPause = 0
        updateCurrentTask(task)
        _time.value = task.time
    }

    private fun initTask(task: Task) {
        projectName.value = task.projectName
        taskDescription.value = task.taskDescription
        if (task.isStarted) {
            task.time?.let {
                val interval = (Date().time - task.lastPause) / 1000
                val newTime = interval + task.time!!.totalSeconds
                task.time!!.hours = newTime / 3600
                task.time!!.minutes = newTime / 60
                task.time!!.seconds = newTime % 60
            }
        }
        _time.value = task.time
    }

    private fun startTimer(storedTime: Time?, interval: Long?) {
        timerDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
            .subscribe { time ->

                var newTime = time
                if (interval != null && interval != 0L) {
                    newTime += interval
                }

                if (storedTime != null) {
                    newTime += storedTime.totalSeconds
                }

                val hours = newTime / 3600
                val minutes = newTime / 60
                val seconds = newTime % 60

                _time.postValue(Time(hours, minutes, seconds, newTime))
            }
    }

    private fun stopTimer() {
        if (timerDisposable != null) {
            timerDisposable!!.dispose()
            timerDisposable = null
        }
    }

}
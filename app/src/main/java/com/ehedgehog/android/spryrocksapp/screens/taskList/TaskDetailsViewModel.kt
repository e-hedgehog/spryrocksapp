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

    private val _time = MutableLiveData<Time>()
    val time: LiveData<Time> get() = _time

    var isNewTask = true

    private var timerDisposable: Disposable? = null

    @Inject
    lateinit var databaseManager: DatabaseManager

    init {
        Application.appComponent.injectTasksDetailsViewModel(this)
    }

    fun saveTask() {
        databaseManager.saveNewTask(projectName.value!!, taskDescription.value!!)
    }

    fun getTaskById(id: Int): Task? {
        return databaseManager.getTaskById(id)
    }

    fun updateCurrentTask(task: Task) {
        task.projectName = projectName.value!!
        task.taskDescription = taskDescription.value!!
        databaseManager.updateTask(task)
    }

    fun resetCurrentTask(task: Task) {
        task.isStarted = false
        task.time = Time(0, 0, 0, 0)
        task.lastPause = 0
        updateCurrentTask(task)
        _time.value = task.time
    }

    fun initTask(task: Task) {
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

    fun startTimer(storedTime: Time?, interval: Long?) {
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
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

    fun stopTimer() {
        if (timerDisposable != null) {
            timerDisposable!!.dispose()
            timerDisposable = null
        }
    }

}
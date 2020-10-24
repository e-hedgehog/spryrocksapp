package com.ehedgehog.android.spryrocksapp.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.network.Time
import java.util.*

class TaskTimerManager(private val timerUseCase: TimerUseCase, private val databaseManager: DatabaseManager) {

    private val _time = MutableLiveData<Time>()
    val time: LiveData<Time> get() = _time

    private val _isTimerStarted = MutableLiveData<Boolean>()
    val isTimerStarted: LiveData<Boolean> get() = _isTimerStarted

    fun initTimer(task: Task) {
        if (task.isStarted) {
            task.time?.let {
                val interval = (Date().time - task.lastPause) / 1000
                val newTime = interval + it.totalSeconds
                it.hours = newTime / 3600
                it.minutes = newTime / 60
                it.seconds = newTime % 60
            }
        }

        _time.value = task.time
        _isTimerStarted.value = task.isStarted
    }

    fun unpauseTimerIfStarted(task: Task): Boolean {
        if (task.isStarted) {
            _isTimerStarted.value = true
            val currentTime = Date().time
            val interval = (currentTime - task.lastPause) / 1000
            startTimerObservable(task.time, interval)
            return true
        }

        return false
    }

    fun startTimer(task: Task?) {
        _isTimerStarted.value = true
        task?.let {
            it.isStarted = true
            databaseManager.updateTask(it)
            startTimerObservable(task.time, 0)
            return
        }

        startTimerObservable(null, null)
    }

    fun stopTimer(task: Task?) {
        task?.let {
            it.isStarted = false
            it.time = time.value
            it.lastPause = 0
            databaseManager.updateTask(it)
        }

        _isTimerStarted.value = false
        stopTimerObservable()
    }

    fun pauseTimer(task: Task?) {
        task?.let {
            if (it.isStarted) {
                stopTimerObservable()
                it.time = time.value
                it.lastPause = Date().time
                databaseManager.updateTask(it)
            }
        }
    }

    fun resetTimer(task: Task?) {
        task?.let {
            it.isStarted = false
            it.time = Time()
            it.lastPause = 0
            databaseManager.updateTask(it)
            _time.value = it.time
        }

        _isTimerStarted.value = false
        stopTimerObservable()
    }

    private fun startTimerObservable(storedTime: Time?, interval: Long?) {
        timerUseCase.stopTimer()
        timerUseCase.startTimer(storedTime, interval) { time ->
            _time.postValue(time)
        }
    }

    private fun stopTimerObservable() {
        timerUseCase.stopTimer()
    }

}
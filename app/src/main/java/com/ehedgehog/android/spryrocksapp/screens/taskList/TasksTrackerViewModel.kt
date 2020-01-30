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

    init {
        Application.appComponent.injectTasksTrackerViewModel(this)
    }

    fun loadStoredTasks() {
        _listTasks.value = databaseManager.getStoredTasks()
    }

}
package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.LiveData
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.network.Time

data class TaskListItemModel(
    val timeLiveData: LiveData<Time>,
    val task: Task
)
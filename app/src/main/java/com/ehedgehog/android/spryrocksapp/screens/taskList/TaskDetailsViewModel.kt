package com.ehedgehog.android.spryrocksapp.screens.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskDetailsViewModel : ViewModel() {

    val projectName = MutableLiveData<String>()

    val taskDescription = MutableLiveData<String>()

}
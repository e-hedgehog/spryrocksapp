package com.ehedgehog.android.spryrocksapp.di

import com.ehedgehog.android.spryrocksapp.screens.employee.EmployeeInfoViewModel
import com.ehedgehog.android.spryrocksapp.screens.taskList.TaskDetailsViewModel
import com.ehedgehog.android.spryrocksapp.screens.taskList.TasksTrackerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun injectEmployeeInfoViewModel(viewModel: EmployeeInfoViewModel)
    fun injectTasksTrackerViewModel(viewModel: TasksTrackerViewModel)
    fun injectTasksDetailsViewModel(viewModel: TaskDetailsViewModel)

}
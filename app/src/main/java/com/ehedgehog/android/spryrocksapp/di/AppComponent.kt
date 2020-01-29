package com.ehedgehog.android.spryrocksapp.di

import com.ehedgehog.android.spryrocksapp.screens.employee.EmployeeInfoViewModel
import com.ehedgehog.android.spryrocksapp.screens.tracker.TasksTrackerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun injectEmployeeInfoViewModel(viewModel: EmployeeInfoViewModel)
    fun injectTasksTrackerViewModel(viewModel: TasksTrackerViewModel)

}
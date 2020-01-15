package com.ehedgehog.android.spryrocksapp.di

import com.ehedgehog.android.spryrocksapp.screens.employee.EmployeeInfoViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun injectEmpoyeeInfoViewModel(fragment: EmployeeInfoViewModel)

}
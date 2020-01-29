package com.ehedgehog.android.spryrocksapp.screens

import com.ehedgehog.android.spryrocksapp.network.EmployeeInfo
import com.ehedgehog.android.spryrocksapp.network.Task
import io.realm.Realm

class DatabaseManager(val realm: Realm) {

    fun saveCurrentEmployee(employeeInfo: EmployeeInfo) {
        realm.executeTransaction {realm ->
            realm.delete(EmployeeInfo::class.java)
            realm.insert(employeeInfo)
        }
    }

    fun getCurrentEmployee(): EmployeeInfo? {
        val employee = realm.where(EmployeeInfo::class.java).findFirst() ?: return EmployeeInfo()
        return realm.copyFromRealm(employee)
    }

    fun saveNewTask(task: Task) {
        realm.executeTransaction { realm ->
            realm.insert(task)
        }
    }

    fun getStoredTasks(): List<Task> {
        val listTasks = realm.where(Task::class.java).findAll()
        return realm.copyFromRealm(listTasks)
    }

}
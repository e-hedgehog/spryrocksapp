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

    fun saveNewTask(projectName: String, taskDescription:String) {
        realm.executeTransaction { realm ->
            val maxId: Number? = realm.where(Task::class.java).max("id")
            val nextId = maxId?.toInt()?.inc() ?: 1
            realm.insert(Task(nextId, projectName, taskDescription))
        }
    }

    fun updateTask(task: Task) {
        realm.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(task)
        }
    }

    fun getTaskById(id: Int): Task? {
        val storedTask = realm.where(Task::class.java).equalTo("id", id).findFirst()
        return realm.copyFromRealm(storedTask)
    }

    fun getStoredTasks(): List<Task> {
        val listTasks = realm.where(Task::class.java).findAll()
        return realm.copyFromRealm(listTasks)
    }

}
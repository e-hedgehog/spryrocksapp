package com.ehedgehog.android.spryrocksapp.network

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class BoardList(
    val id: String,
    val name: String,
    val closed: Boolean)

data class CardResponse(
    val id: String
)

@Parcelize
open class Task(
    @PrimaryKey var id: Int = 0,
    var projectName: String = "",
    var taskDescription: String = ""
): RealmObject(), Parcelable

open class EmployeeInfo(
    @PrimaryKey var name: String = "",
    var birthDate: String = "",
    var position: String = "",
    var phone: String = "",
    var telegram: String = "",
    var gmail: String = "",
    var github: String = "",
    var gitlab: String = ""
) : RealmObject() {
    override fun toString(): String {
        return "Name: $name\n" +
                "Birth date: $birthDate\n" +
                "Position: $position\n" +
                "Phone: $phone\n" +
                "Telegram: $telegram\n" +
                "Gmail: $gmail\n" +
                "Github: $github\n" +
                "Gitlab: $gitlab"
    }
}
package com.ehedgehog.android.spryrocksapp.network

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat

data class BoardList(
    val id: String,
    val name: String,
    val closed: Boolean)

data class CardResponse(
    val id: String
)

@Parcelize
open class Time(
    var hours: Long = 0,
    var minutes: Long = 0,
    var seconds: Long = 0,
    var totalSeconds: Long = 0
): RealmObject(), Parcelable {
    override fun toString(): String {
        val formatter = DecimalFormat("00")
        val hoursString = hours.toString()
        val minutesString = formatter.format(minutes)
        val secondsString = formatter.format(seconds)
        return "$hoursString:$minutesString:$secondsString"
    }
}

@Parcelize
open class Task(
    @PrimaryKey var id: Int = 0,
    var projectName: String = "",
    var taskDescription: String = "",
    var time: Time? = Time(),
    var lastPause: Long = 0,
    var isStarted: Boolean = false
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
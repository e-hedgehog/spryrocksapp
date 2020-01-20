package com.ehedgehog.android.spryrocksapp.network

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class BoardList(
    val id: String,
    val name: String,
    val closed: Boolean)

data class CardResponse(
    val id: String
)

open class EmployeeInfo(
    @PrimaryKey var name: String? = "",
    var position: String? = "",
    var phone: String? = "",
    var telegram: String? = "",
    var gmail: String? = "",
    var github: String? = "",
    var gitlab: String? = ""
) : RealmObject() {
    override fun toString(): String {
        return "Name: $name\n" +
                "Position: $position\n" +
                "Phone: $phone\n" +
                "Telegram: $telegram\n" +
                "Gmail: $gmail\n" +
                "Github: $github\n" +
                "Gitlab: $gitlab"
    }
}
package com.ehedgehog.android.spryrocksapp.network

data class BoardList(
    val id: String,
    val name: String,
    val closed: Boolean)

data class CardsResponse(
    val id: String
)

data class EmployeeInfo(
    val name: String,
    val position: String,
    val phone: String,
    val telegram: String,
    val gmail: String,
    val github: String,
    val gitlab: String
) {
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
package com.omelchenkoaleks.clonetelegram.models

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullName: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty"
)
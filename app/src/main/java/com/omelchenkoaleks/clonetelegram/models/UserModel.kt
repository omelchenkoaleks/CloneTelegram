package com.omelchenkoaleks.clonetelegram.models

/*
    Хорошо, когда эта модель при запуске приложения подключается к базе данных и обновляет свои
    свойства. Чтобы не было необходимости при каждом случае обращаться к базе данных. А данные
    хранились уже в User.
 */
data class UserModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullName: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty"
)
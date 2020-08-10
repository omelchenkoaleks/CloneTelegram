package com.omelchenkoaleks.clonetelegram.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.omelchenkoaleks.clonetelegram.models.User

/*
    Эта переменная будет работать на все приложение.
    Инициализируем ее один раз в MainActivity.
    И не надо будет создавать лишние экземпляры в классах.
 */
lateinit var AUTH: FirebaseAuth
lateinit var UID: String // Уникальный номер нашего пользователя.
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: User


const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULL_NAME = "fullName"


fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString() // Получаем уникальный идентификационный номер нашего пользгователя.
}
package com.omelchenkoaleks.clonetelegram.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.omelchenkoaleks.clonetelegram.models.UserModel

/*
    Эта переменная будет работать на все приложение.
    Инициализируем ее один раз в MainActivity.
    И не надо будет создавать лишние экземпляры в классах.
 */
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String // Уникальный номер нашего пользователя.
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: UserModel
const val NODE_USERS = "users"
const val NODE_MESSAGES = "messages"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"
const val NODE_MAIN_LIST = "main_list"
const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_FILES = "messages_files"
const val TYPE_TEXT = "text"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULL_NAME = "fullName"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_FILE_URL = "fileUrl"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIME_STAMP = "timeStamp"
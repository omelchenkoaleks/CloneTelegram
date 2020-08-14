package com.omelchenkoaleks.clonetelegram.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omelchenkoaleks.clonetelegram.models.User

/*
    Эта переменная будет работать на все приложение.
    Инициализируем ее один раз в MainActivity.
    И не надо будет создавать лишние экземпляры в классах.
 */
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String // Уникальный номер нашего пользователя.
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User


const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULL_NAME = "fullName"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"


fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID =
        AUTH.currentUser?.uid.toString() // Получаем уникальный идентификационный номер нашего пользгователя.
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference // Получаем ссылку на Storage.
}

fun putUrlToDatabase(url: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getUrlFromStorage(path: StorageReference, function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun putImageToStorage(uri: Uri, path: StorageReference, function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

package com.omelchenkoaleks.clonetelegram.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.models.UserModel
import com.omelchenkoaleks.clonetelegram.utils.APP_ACTIVITY
import com.omelchenkoaleks.clonetelegram.utils.AppValueEventListener
import com.omelchenkoaleks.clonetelegram.utils.showToast
import java.io.File

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID =
        AUTH.currentUser?.uid.toString() // Получаем уникальный идентификационный номер нашего пользгователя.
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference // Получаем ссылку на Storage.
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    /*
        addListenerForSingleValueEvent
        Слушатель, который подключится к базе, скачает нужные данные и закроется.
        Вешаем слушателя, который один раз подключится, а не будет слушать изменения постоянно.
     */
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            // getValue - метод (Firebase) в данном случае принимает весь класс полностью
            USER = it.getValue(UserModel::class.java)
                ?: UserModel() // Если вдруг чего-то нет (null) - мы просто инициализируем пустым User()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { snapshot ->
                arrayContacts.forEach { contact ->
                    if (snapshot.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_ID)
                            .setValue(snapshot.value.toString())
                            .addOnFailureListener { showToast(it.message.toString()) }
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_FULL_NAME)
                            .setValue(contact.fullName)
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
                }
            }
        })
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserId: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"
    // Получаем уникальный ключ сообщения.
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
                deleteOldUsername(newUserName)
            } else {
                showToast(it.exception?.message.toString())
            }
        }
}

private fun deleteOldUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            APP_ACTIVITY.supportFragmentManager.popBackStack() // возвращаемся назад по стеку
            USER.username = newUserName
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.bio = newBio // Обновляем данные (bio) для нашего пользователя.
            APP_ACTIVITY.supportFragmentManager.popBackStack() // Переходим по стеку назад.
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setNameToDatabase(fullName: String) {
    // Добавляем в базу данных.
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULL_NAME)
        .setValue(fullName)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            // Теперь нужно юзера обновить.
            USER.fullName = fullName
            APP_ACTIVITY.mAppDrawer.updateHeader() // Обновить после изменения.
            APP_ACTIVITY.supportFragmentManager.popBackStack() // Переходим по стеку назад.
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageAsFile(
    receivingUserId: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedId: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT
        .child(FOLDER_FILES)
        .child(messageKey)

    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(
                receivedId,
                it,
                messageKey,
                typeMessage,
                filename
            )
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun saveToMainList(id: String, type: String) {
    val refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$CURRENT_UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = CURRENT_UID
    mapReceived[CHILD_TYPE] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun deleteChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(id).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener { function() }
}

fun clearChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
        .removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(id).child(CURRENT_UID).removeValue()
                .addOnSuccessListener { function() }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}



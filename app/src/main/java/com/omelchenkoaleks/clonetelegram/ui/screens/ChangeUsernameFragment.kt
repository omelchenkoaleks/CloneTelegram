package com.omelchenkoaleks.clonetelegram.ui.screens

import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.database.*
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        // Подгружаем тот username что есть на данный момент.
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            /*
                Сначала нужно проверить hasChild() - есть ли уже в базе данных
                 такой пользователь (такой username)?!
             */
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast("Такой пользователь уже существует")
                    } else {
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        // Новый username прописываем в базу данных вместе с сылкой на id пользователя.
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername) // Нужно обновить в базе также на новый username у пользователя.
                }
            }
    }


}
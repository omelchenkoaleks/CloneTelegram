package com.omelchenkoaleks.clonetelegram.ui.screens

import androidx.fragment.app.Fragment
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.utils.APP_ACTIVITY
import com.omelchenkoaleks.clonetelegram.utils.hideKeyboard

/*
    Главный экран приложения.
    ChatsFragment - т.к. он основной фрагмент, он не наследуется от базового фрагмента. Он должен
    вести свою жизнь и не должен вести точно также, как другие фрагменты.
 */
class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }
}
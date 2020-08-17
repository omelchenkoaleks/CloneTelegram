package com.omelchenkoaleks.clonetelegram.ui.fragments

import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.utils.APP_ACTIVITY

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
    }

}
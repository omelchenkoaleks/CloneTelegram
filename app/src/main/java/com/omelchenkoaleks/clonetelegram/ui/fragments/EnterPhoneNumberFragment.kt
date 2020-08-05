package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.widget.Toast
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.replaceFragment
import com.omelchenkoaleks.clonetelegram.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {

    override fun onStart() {
        super.onStart()
        register_button_next_fab.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_input_phone_number_edit_text.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            replaceFragment(EnterCodeFragment())
        }
    }

}
package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.widget.Toast
import com.omelchenkoaleks.clonetelegram.R
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {

    override fun onStart() {
        super.onStart()
        register_button_next_fab.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_input_phone_number_edit_text.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.register_toast_enter_phone), Toast.LENGTH_SHORT).show()
        } else {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.register_data_container, EnterCodeFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

}
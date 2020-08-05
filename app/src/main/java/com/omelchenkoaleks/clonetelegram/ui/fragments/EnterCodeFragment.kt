package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.omelchenkoaleks.clonetelegram.R
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment : BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        register_inter_code_edit_text.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val code = register_inter_code_edit_text.text.toString()
                if (code.length == 6 ) {
                    verificationCode()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    fun verificationCode() {
        Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show()
    }

}
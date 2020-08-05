package com.omelchenkoaleks.clonetelegram.ui.fragments

import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.showToast
import com.omelchenkoaleks.clonetelegram.utils.AppTextWatcher
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment : BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        register_inter_code_edit_text.addTextChangedListener(AppTextWatcher {
            val code = register_inter_code_edit_text.text.toString()
            if (code.length == 6) {
                verificationCode()
            }
        })
    }

    private fun verificationCode() {
        showToast("OK")
    }

}
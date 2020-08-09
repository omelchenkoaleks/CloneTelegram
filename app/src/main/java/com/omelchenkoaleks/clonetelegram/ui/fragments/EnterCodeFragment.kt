package com.omelchenkoaleks.clonetelegram.ui.fragments

import com.google.firebase.auth.PhoneAuthProvider
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.replaceActivity
import com.omelchenkoaleks.clonetelegram.showToast
import com.omelchenkoaleks.clonetelegram.utils.AUTH
import com.omelchenkoaleks.clonetelegram.utils.AppTextWatcher
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val phoneNumber: String, private val id: String) :
    BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        register_inter_code_edit_text.addTextChangedListener(AppTextWatcher {
            val code = register_inter_code_edit_text.text.toString()
            if (code.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_inter_code_edit_text.text.toString()
        // Получаем объект с помощью которого можно получить авторизацию или создать нового пользователя.
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("Добро пожаловать")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else {
                showToast(it.exception?.message.toString())
            }
        }
    }

}
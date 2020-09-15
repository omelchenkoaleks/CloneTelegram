package com.omelchenkoaleks.clonetelegram.ui.screens.register

import com.google.firebase.auth.PhoneAuthProvider
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.database.*
import com.omelchenkoaleks.clonetelegram.ui.screens.BaseFragment
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val phoneNumber: String, private val id: String) :
    BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_inter_code_edit_text.addTextChangedListener(AppTextWatcher {
            val code = register_inter_code_edit_text.text.toString()
            if (code.length == 6) {
                enterCode()
            }
        })
    }

    /*
        Функция проверяет код, если все хорошо,
        то записывает информацию о пользователе в базе данных.
     */
    private fun enterCode() {
        val code = register_inter_code_edit_text.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener {

                        if (!it.hasChild(CHILD_USERNAME)) {
                            dateMap[CHILD_USERNAME] = uid
                        }

                        // Содаем NODE (узел в базе данных) для телефонов и их id.
                        REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                // Передаем теперь данные в базу данных.
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dateMap)
                                    .addOnCompleteListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })

            } else {
                showToast(authResult.exception?.message.toString())
            }
        }
    }

}
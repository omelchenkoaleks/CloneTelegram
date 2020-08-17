package com.omelchenkoaleks.clonetelegram.ui.fragments

import com.google.firebase.auth.PhoneAuthProvider
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.utils.*
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

    /*
        Функция проверяет код, если все хорошо,
        то записывает информацию о пользователе в базе данных.
     */
    private fun enterCode() {
        val code = register_inter_code_edit_text.text.toString()
        // Получаем объект с помощью которого можно получить авторизацию или создать нового пользователя.
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { it ->
            if (it.isSuccessful) {

                val uid = AUTH.currentUser?.uid.toString()
                /*
                    Будем записывать в эту коллекцию данные и передавать ее в базу данных.
                 */
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid

                // Содаем NODE (узел в базе данных) для телефонов и их id.
                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        // Передаем теперь данные в базу данных.
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnCompleteListener {task ->
                                if (task.isSuccessful) {
                                    showToast("Добро пожаловать")
                                    (activity as RegisterActivity).replaceActivity(MainActivity())
                                } else {
                                    showToast(task.exception?.message.toString())
                                }
                            }
                    }

            } else {
                showToast(it.exception?.message.toString())
            }
        }
    }

}
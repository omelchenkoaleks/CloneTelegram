package com.omelchenkoaleks.clonetelegram.ui.fragments

import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        initFullNameList()
    }

    private fun initFullNameList() {
        // Делим на две строки по пробелу. Заполняем поля: имя и фамилия.
        val fullNameList = USER.fullName.split(" ")

        // Нужно обязательно проветить какая длина массива - т.к. может не быть двух элементов.
        if (fullNameList.size > 1) {
            settings_input_name.setText(fullNameList[0])
            settings_input_surname.setText(fullNameList[1])
        } else {
            settings_input_name.setText(fullNameList[0])
        }
    }

    override fun change() {
        // Сначала при нажатии на кнопку мы должны считать данные.
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        // Мы не будем сохранять фамилию без имени, а имя без имени можем сохранить.
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_isEmpty))
        } else {
            val fullName = "$name $surname"
            // Добавляем в базу данных.
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULL_NAME)
                .setValue(fullName).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        // Теперь нужно юзера обновить.
                        USER.fullName = fullName
                        APP_ACTIVITY.mAppDrawer.updateHeader() // Обновить после изменения.
                        fragmentManager?.popBackStack() // Переходим по стеку назад.
                    }
                }
        }
    }

}
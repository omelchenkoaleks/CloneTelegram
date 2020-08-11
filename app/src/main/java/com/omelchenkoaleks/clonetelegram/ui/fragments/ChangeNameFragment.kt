package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        // Делим на две строки по пробелу. Заполняем поля: имя и фамилия.
        val fullNameList = USER.fullName.split(" ")
        settings_input_name.setText(fullNameList[0])
        settings_input_surname.setText(fullNameList[1])
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeName()
        }
        return true
    }

    private fun changeName() {
        // Сначала при нажатии на кнопку мы должны считать данные.
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        // Мы не будем сохранять фамилию без имени, а имя без имени можем сохранить.
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_isEmpty))
        } else {
            val fullName = "$name $surname"
            // Добавляем в базу данных.
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULL_NAME)
                .setValue(fullName).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        // Теперь нужно юзера обновить.
                        USER.fullName = fullName
                        fragmentManager?.popBackStack() // Переходим по стеку назад.
                    }
                }
        }
    }

}
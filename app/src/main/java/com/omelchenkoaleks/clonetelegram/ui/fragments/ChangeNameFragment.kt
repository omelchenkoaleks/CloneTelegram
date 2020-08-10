package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
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
    }

}
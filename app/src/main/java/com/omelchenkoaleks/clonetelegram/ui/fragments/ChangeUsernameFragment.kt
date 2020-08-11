package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.utils.USER
import kotlinx.android.synthetic.main.fragment_change_username.*

class ChangeUsernameFragment : BaseFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        // Подгружаем тот username что есть на данный момент.
        settings_input_username.setText(USER.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    private fun change() {
        TODO("Not yet implemented")
    }
}
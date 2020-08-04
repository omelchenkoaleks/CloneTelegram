package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import com.omelchenkoaleks.clonetelegram.R

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    /*
        В этом методе будет запускаться вся работа фрагмента.
     */
    override fun onResume() {
        super.onResume()

    }

    // Добавляем меную в фрагмент.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }
}
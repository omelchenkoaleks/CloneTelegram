package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.omelchenkoaleks.clonetelegram.MainActivity
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.utils.replaceActivity
import com.omelchenkoaleks.clonetelegram.utils.AUTH
import com.omelchenkoaleks.clonetelegram.utils.USER
import com.omelchenkoaleks.clonetelegram.utils.replaceFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true) // включает меню в контекте
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullName
        settings_phone_number.text = USER.phone
        settings_status.text = USER.status
        settings_username.text = USER.username

        // Активируем кнопку для перехода на изменение username.
        settings_button_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        // Активируем кнопку для перехода на изменение bio.
        settings_button_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        // Активируем кнопку для перехода на изменение photo.
        settings_user_photo.setOnClickListener { changePhotoUser() }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start((activity as MainActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut() // Выходим из авторизованного аккаунта
                /*
                    Теперь нужно обязательно запустить снова окно авторизации:
                 */
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }
}
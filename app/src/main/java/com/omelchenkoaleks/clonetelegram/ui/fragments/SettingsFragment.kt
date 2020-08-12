package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.utils.*
import com.squareup.picasso.Picasso
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
        settings_change_photo.setOnClickListener { changePhotoUser() }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    /*
        Этот метод должен отловить отправленный requestCode от CropImageActivity
        в методе changePhotoUser(), чтобы продолжить работу с изображением.
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            /*
                И вот теперь нужно получить uri на нашу картинку, который покажет нам путь
                к нашему обрезанному изображению.
             */
            val uri = CropImage.getActivityResult(data).uri
            // Путь создаем.
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            // Передаем туда полученный uri и вешаем слушатель.
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    // Получить само изображение можно как callback метода downloadUrl.
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            /*
                                В переменной photoUrl и есть то значение (адрес в Интернете) по
                                которому мы можем обратиться к нашей картинке.
                             */
                            val photoUrl = task2.result.toString()

                            // Сохраняем url в базе данных и в пользователе.
                            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                                .child(CHILD_PHOTO_URL).setValue(photoUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        // Устанавливаем нашу картинку в приложение.
                                        Picasso.get()
                                            .load(photoUrl)
                                            .placeholder(R.drawable.default_photo)
                                            .into(settings_user_photo)
                                        showToast(getString(R.string.toast_data_update))
                                        USER.photoUrl = photoUrl
                                    }
                                }
                        }
                    }
                }
            }
        }
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
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }
}
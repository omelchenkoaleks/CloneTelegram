package com.omelchenkoaleks.clonetelegram

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.databinding.ActivityMainBinding
import com.omelchenkoaleks.clonetelegram.models.User
import com.omelchenkoaleks.clonetelegram.ui.fragments.ChatsFragment
import com.omelchenkoaleks.clonetelegram.ui.objects.AppDrawer
import com.omelchenkoaleks.clonetelegram.utils.*
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        /*
            Как только запускается MainActivity мы присвоиваем ссылку на это активити константе.
            Теперь в любых частях этого приложения можно будет получить ссылку на это активити.
            И не нужно будет делать приведение типа (activity as MainActivity)!
         */
        APP_ACTIVITY = this
        initFields()
        initFunctionality()
    }

    private fun initFunctionality() {
        if (AUTH.currentUser != null) { // Если авторизован, то заходим
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
        initFirebase()
        initUser()
    }

    private fun initUser() {
        /*
            addListenerForSingleValueEvent
            Слушатель, который подключится к базе, скачает нужные данные и закроется.
            Вешаем слушателя, который один раз подключится, а не будет слушать изменения постоянно.
         */
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                // getValue - метод (Firebase) в данном случае принимает весь класс полностью
                USER = it.getValue(User::class.java)
                    ?: User() // Если вдруг чего-то нет (null) - мы просто инициализируем пустым User()
            })
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

    // Скрывает клавиатуру в любом месте приложения.
    fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

}
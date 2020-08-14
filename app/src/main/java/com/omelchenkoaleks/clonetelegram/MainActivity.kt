package com.omelchenkoaleks.clonetelegram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.omelchenkoaleks.clonetelegram.activities.RegisterActivity
import com.omelchenkoaleks.clonetelegram.databinding.ActivityMainBinding
import com.omelchenkoaleks.clonetelegram.models.User
import com.omelchenkoaleks.clonetelegram.ui.fragments.ChatsFragment
import com.omelchenkoaleks.clonetelegram.ui.objects.AppDrawer
import com.omelchenkoaleks.clonetelegram.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        /*
            Как только запускается MainActivity мы присвоиваем ссылку на это активити константе.
            Теперь в любых частях этого приложения можно будет получить ссылку на это активити.
            И не нужно будет делать приведение типа (activity as MainActivity)!
         */
        APP_ACTIVITY = this
        initFirebase() // Сначала должны проинициализироваться все наши переменные, доступные во всем приложении.
        initUser { // Потом нашего пользователя. Но нужно дождаться его инициализации с текущей базы данных, прежде чем пойдет дальше.
            // Эти функции начнут выполняться только после инициализации нашего пользователя.
            initFields()
            initFunctionality()
        }
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
    }

}
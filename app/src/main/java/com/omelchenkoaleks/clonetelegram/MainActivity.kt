package com.omelchenkoaleks.clonetelegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.omelchenkoaleks.clonetelegram.database.AUTH
import com.omelchenkoaleks.clonetelegram.database.initFirebase
import com.omelchenkoaleks.clonetelegram.database.initUser
import com.omelchenkoaleks.clonetelegram.databinding.ActivityMainBinding
import com.omelchenkoaleks.clonetelegram.ui.screens.main_list.MainListFragment
import com.omelchenkoaleks.clonetelegram.ui.screens.register.EnterPhoneNumberFragment
import com.omelchenkoaleks.clonetelegram.ui.objects.AppDrawer
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar

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
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }

            initFields()
            initFunctionality()
        }
    }

    private fun initFunctionality() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) { // Если авторизован, то заходим
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        // Когда разворачиваем приложение - передаем в базу данных наше состояние ONLINE
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        // Когда сворачивем приложение - передаем в базу данных наше состояние OFFLINE
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            initContacts()
        }
    }

}
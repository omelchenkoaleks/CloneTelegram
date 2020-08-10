package com.omelchenkoaleks.clonetelegram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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
    }

    override fun onStart() {
        super.onStart()
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
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                // getValue - метод (Firebase) в данном случае принимает весь класс полностью
                USER = it.getValue(User::class.java) ?: User() // Если вдруг чего-то нет (null) - мы просто инициализируем пустым User()
            })
    }
}
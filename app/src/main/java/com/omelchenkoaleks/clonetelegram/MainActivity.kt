package com.omelchenkoaleks.clonetelegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omelchenkoaleks.clonetelegram.databinding.ActivityMainBinding
import com.omelchenkoaleks.clonetelegram.ui.fragments.ChatsFragment
import com.omelchenkoaleks.clonetelegram.ui.objects.AppDrawer

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAppDrawer: AppDrawer
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
        setSupportActionBar(mToolbar)
        mAppDrawer.create()
        supportFragmentManager.beginTransaction()
            .replace(R.id.data_container,
                ChatsFragment()
            ).commit()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }
}
package com.omelchenkoaleks.clonetelegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.databinding.ActivityRegisterBinding
import com.omelchenkoaleks.clonetelegram.utils.replaceFragment
import com.omelchenkoaleks.clonetelegram.ui.fragments.EnterPhoneNumberFragment
import com.omelchenkoaleks.clonetelegram.utils.initFirebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(), false)
    }
}
package com.omelchenkoaleks.clonetelegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omelchenkoaleks.clonetelegram.databinding.ActivityRegisterBinding

/*
    Задача этой Activity произвести авторизацию или зарегистрировать нового пользогвателя.
 */
class RegisterActivity : AppCompatActivity() {

   private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}
package com.omelchenkoaleks.clonetelegram.utils

import com.google.firebase.auth.FirebaseAuth

/*
    Эта переменная будет работать на все приложение.
    Инициализируем ее один раз в MainActivity.
    И не надо будет создавать лишние экземпляры в классах.
 */
lateinit var AUTH: FirebaseAuth
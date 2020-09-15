package com.omelchenkoaleks.clonetelegram.ui.screens

import androidx.fragment.app.Fragment
import com.omelchenkoaleks.clonetelegram.utils.APP_ACTIVITY

open class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        /*
            Как только у нас запускается любой фрагмент, который наследуется от базового фрагмента =
            будет запущет этот код с методом отключения Drawer.
         */
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}
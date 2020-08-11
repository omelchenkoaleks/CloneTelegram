package com.omelchenkoaleks.clonetelegram.ui.fragments

import androidx.fragment.app.Fragment
import com.omelchenkoaleks.clonetelegram.MainActivity

open class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        /*
            Как только у нас запускается любой фрагмент, который наследуется от базового фрагмента =
            будет запущет этот код с методом отключения Drawer.
         */
        // TODO: Разобраться с багами = java.lang.ClassCastException
//        (activity as MainActivity).mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        /*
            Сработает перед закрытием фрагмента. Когда будет нажата кнопка "назад".
         */
//        (activity as MainActivity).mAppDrawer.enableDrawer()
    }

}
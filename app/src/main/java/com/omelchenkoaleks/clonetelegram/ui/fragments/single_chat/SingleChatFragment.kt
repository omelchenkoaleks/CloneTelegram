package com.omelchenkoaleks.clonetelegram.ui.fragments.single_chat

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.database.*
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.models.UserModel
import com.omelchenkoaleks.clonetelegram.ui.fragments.BaseFragment
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener // to avoid memory leaks
    private var mCountMessages = 10 // Переменная хранит сколько сообщений нужно загрузить.
    private var mIsScrolling = false
    private var mSmoothScrollToPosition =
        true // Как только мы первый раз получаем данные мы должны опуститься вниз
    private var mListListener = mutableListOf<AppChildEventListener>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter

        mMessagesListener = AppChildEventListener {
            mAdapter.addItem(it.getCommonModel(), mSmoothScrollToPosition) // передаем одно сообщение
            if (mSmoothScrollToPosition) {
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount) // Прокрутить адаптер на последний элемент списка.
            }
        }

        // Добавляем фильтр = ограничить последними 10 сообщениями.
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListener.add(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }
        })
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListener.add(mMessagesListener)
    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            // Как только происходят какие-то изменения - наму нужно обновить юзера
            mReceivingUser = it.getUserModel() // Получаем измененные данные из базы данных
            initInfoToolbar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        chat_btn_send_message.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = chat_input_message.text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    chat_input_message.setText("") // Очистим нашу строку после отправления сообщения.
                }
            }
        }
    }

    private fun initInfoToolbar() {
        if (mReceivingUser.fullName.isEmpty()) {
            mToolbarInfo.toolbar_chat_fullname.text = contact.fullName
        } else mToolbarInfo.toolbar_chat_fullname.text = mReceivingUser.fullName

        mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.toolbar_chat_status.text = mReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)

        mListListener.forEach {
            mRefMessages.removeEventListener(it)
        }
    }

}
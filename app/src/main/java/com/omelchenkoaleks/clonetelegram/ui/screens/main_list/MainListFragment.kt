package com.omelchenkoaleks.clonetelegram.ui.screens.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.database.*
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_main_list.*

/*
    Главный экран приложения.
    ChatsFragment - т.к. он основной фрагмент, он не наследуется от базового фрагмента. Он должен
    вести свою жизнь и не должен вести точно также, как другие фрагменты.
 */
class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycler_view
        mAdapter = MainListAdapter()

        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                when (model.type) {
                    TYPE_CHAT -> showChat(model)
                    TYPE_GROUP -> showGroup(model)
                }
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    private fun showGroup(model: CommonModel) {
        REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                val newModel = dataSnapshot1.getCommonModel()

                REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id).child(NODE_MESSAGES).limitToLast(1)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                        if (tempList.isEmpty()) {
                            newModel.lastMessage = "Чат очищен"
                        } else {
                            newModel.lastMessage = tempList[0].text
                        }
                        mAdapter.updateListItems(newModel)
                    })
            })
    }

    private fun showChat(model: CommonModel) {
        mRefUsers.child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                val newModel = dataSnapshot1.getCommonModel()

                mRefMessages.child(model.id).limitToLast(1)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                        if (tempList.isEmpty()) {
                            newModel.lastMessage = "Чат очищен"
                        } else {
                            newModel.lastMessage = tempList[0].text
                        }

                        if (newModel.fullName.isEmpty()) {
                            newModel.fullName = newModel.phone
                        }
                        mAdapter.updateListItems(newModel)
                    })
            })
    }
}
package com.omelchenkoaleks.clonetelegram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.database.*
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.ui.screens.base.BaseFragment
import com.omelchenkoaleks.clonetelegram.utils.*
import kotlinx.android.synthetic.main.fragment_add_contacts.*

/*
    Главный экран приложения.
    ChatsFragment - т.к. он основной фрагмент, он не наследуется от базового фрагмента. Он должен
    вести свою жизнь и не должен вести точно также, как другие фрагменты.
 */
class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участника"
        hideKeyboard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участника")
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycler_view
        mAdapter = AddContactsAdapter()

        // 1 request
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                // 2 request
                mRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getCommonModel()

                        // 3 request
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
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object {
        val listContacts = mutableListOf<CommonModel>()
    }
}
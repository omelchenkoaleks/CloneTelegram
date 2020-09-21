package com.omelchenkoaleks.clonetelegram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.ui.screens.base.BaseFragment
import com.omelchenkoaleks.clonetelegram.utils.APP_ACTIVITY
import com.omelchenkoaleks.clonetelegram.utils.hideKeyboard
import com.omelchenkoaleks.clonetelegram.utils.showToast
import kotlinx.android.synthetic.main.fragment_create_group.*

class CreateGroupFragment(private var listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        create_group_btn_complete.setOnClickListener {
            showToast("Click")
        }
        create_group_input_name.requestFocus()
    }

    private fun initRecyclerView() {
        mRecyclerView = create_group_recycler_view
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach {
            mAdapter.updateListItems(it)
        }
    }

}
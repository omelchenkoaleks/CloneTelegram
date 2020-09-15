package com.omelchenkoaleks.clonetelegram.ui.screens.single_chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.ui.message_recycler_view.view_holders.*
import com.omelchenkoaleks.clonetelegram.ui.message_recycler_view.views.MessageView

/*
    Работа адаптера должна заключаться только в том, чтобы принять какой-то массив
    и отобразить его в RecyclerView = это всё, что он должен делать.
 */
class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>() // returns immutable empty list
    private lateinit var mDiffResult: DiffUtil.DiffResult

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(mListMessagesCache[position])
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    fun addItemToBottom(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}
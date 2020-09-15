package com.omelchenkoaleks.clonetelegram.ui.fragments.single_chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.database.CURRENT_UID
import com.omelchenkoaleks.clonetelegram.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.omelchenkoaleks.clonetelegram.ui.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.omelchenkoaleks.clonetelegram.ui.fragments.message_recycler_view.view_holders.HolderTextImage
import com.omelchenkoaleks.clonetelegram.ui.fragments.message_recycler_view.views.MessageView
import com.omelchenkoaleks.clonetelegram.utils.asTime
import com.omelchenkoaleks.clonetelegram.utils.downloadAndSetImage

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
        when (holder) {
            is HolderImageMessage -> drawMessageImage(holder, position)
            is HolderTextImage -> drawMessageText(holder, position)
            else -> {

            }
        }
    }

    private fun drawMessageImage(holder: HolderImageMessage, position: Int) {

        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockReceivedImageMessage.visibility = View.GONE
            holder.blockUserImageMessage.visibility = View.VISIBLE
            holder.chatUserImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatUserImageMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blockReceivedImageMessage.visibility = View.VISIBLE
            holder.blockUserImageMessage.visibility = View.GONE
            holder.chatReceivedImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatReceivedImageMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        }

    }

    private fun drawMessageText(holder: HolderTextImage, position: Int) {

        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        }
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
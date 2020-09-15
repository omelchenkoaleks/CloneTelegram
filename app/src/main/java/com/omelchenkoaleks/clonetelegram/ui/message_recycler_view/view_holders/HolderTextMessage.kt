package com.omelchenkoaleks.clonetelegram.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.clonetelegram.database.CURRENT_UID
import com.omelchenkoaleks.clonetelegram.ui.message_recycler_view.views.MessageView
import com.omelchenkoaleks.clonetelegram.utils.asTime
import kotlinx.android.synthetic.main.message_item_text.view.*

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    val blockUserMessage: ConstraintLayout = view.block_user_message
    val chatUserMessage: TextView = view.chat_user_message
    val chatUserMessageTime: TextView = view.chat_user_message_time
    val blockReceivedMessage: ConstraintLayout = view.block_received_message
    val chatReceivedMessage: TextView = view.chat_received_message
    val chatReceivedMessageTime: TextView = view.chat_received_message_time

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blockReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}
package com.omelchenkoaleks.clonetelegram.ui.fragments.message_recycler_view.views

import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.utils.TYPE_MESSAGE_IMAGE

class AppViewFactory {
    companion object {
        fun getView(message: CommonModel): MessageView {
            return when (message.type) {
                TYPE_MESSAGE_IMAGE ->
                    ViewImageMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl
                    )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}
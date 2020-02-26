package ru.skillbranch.devintensive.models

import java.util.*

enum class MessageType {
    TEXT,
    IMAGE
}

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formateMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: MessageType = MessageType.TEXT,
            payload: Any?,
            isIncoming: Boolean = false
        ): BaseMessage {
            lastId++
            return when (type) {
                MessageType.TEXT -> TextMessage(
                    "$lastId",
                    from,
                    chat,
                    date = date,
                    text = payload as String,
                    isIncoming = isIncoming
                )

                MessageType.IMAGE -> ImageMessage(
                    "$lastId",
                    from,
                    chat,
                    date = date,
                    image = payload as String,
                    isIncoming = isIncoming
                )
            }
        }
    }
}
package com.framework.base.component.notify


enum class NotifyType {
    ADD,
    CHANGE,
    DELETE
}

data class Notification<T>(
    var type: NotifyType,
    var index:Int,
    var content: T
)
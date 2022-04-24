package com.framework.base.component.notify

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import com.framework.base.model.UserInfo

class UserNotify : MutableLiveData<Notification<UserInfo>>() {

    companion object {
        private lateinit var sInstance: UserNotify

        @MainThread
        fun get(): UserNotify {
            sInstance = if (Companion::sInstance.isInitialized) sInstance else UserNotify()
            return sInstance
        }
    }
}
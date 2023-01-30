package com.framework.base.component.chatinput

import androidx.lifecycle.ViewModel

enum class HandleType {
    ONLY_KEYBOARD_DOWN, ONLY_KEYBOARD_UP, ONLY_PANEL_UP, ONLY_PANEL_DOWN, KU_PD, KD_PU
}

class ChatInputViewModel : ViewModel() {
    /**
     * 表情面板与键盘做动作的类型
     */
    var handleType = HandleType.ONLY_KEYBOARD_UP

    /**
     * 是否显示表情面板
     */
    var isPanelShow = false
}
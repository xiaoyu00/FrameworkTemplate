package com.framework.base.component.chatinput

import androidx.lifecycle.ViewModel
enum class PanelState {
    HIDE,
    SHOW,
    HALF
}
class ChatInputViewModel : ViewModel() {
    var panelState = PanelState.HIDE
}
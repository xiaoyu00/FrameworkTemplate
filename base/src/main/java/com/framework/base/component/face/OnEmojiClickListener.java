package com.framework.base.component.face;

import com.framework.base.component.face.core.Emoji;

public interface OnEmojiClickListener {

    void onEmojiClick(Emoji emoji);

    void onCustomFaceClick(int groupIndex, Emoji emoji);
}

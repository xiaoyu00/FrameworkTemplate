package com.framework.base.keyboard;

public interface KeyBoardListener {
    void onAnimStart(int moveDistance);
    void onAnimDoing(int offsetX,int offsetY);
    void onAnimEnd();
}

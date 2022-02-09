package com.luck.picture.camerax.listener;

public interface CaptureListener {

    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();
}

package com.luck.picture.camerax.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

/**
 * author hbzhou
 * date 2019/12/16 12:06
 */
public interface FlowCameraListener {

    void captureSuccess(@NonNull String path);

    void recordSuccess(@NonNull String path);

    void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause);
}

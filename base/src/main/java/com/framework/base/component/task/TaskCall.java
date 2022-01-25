package com.framework.base.component.task;

public interface TaskCall {
    public void onStart(long size);
    public void onProgress(long progress,long total);
    public void onFinish();
    public void onError();
    public void onCancel();
}

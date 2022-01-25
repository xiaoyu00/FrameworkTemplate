package com.framework.base.component.task;

import java.io.Serializable;

public class TaskFileInfo implements Serializable {
    private String taskUrl;
    private String filePath;
    private String fileName;
    private boolean isCancel = false;
    private long size =0 ;
    private long progress =0;

    public TaskFileInfo(String downloadUrl, String filePath, String fileName) {
        this.taskUrl = downloadUrl;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}

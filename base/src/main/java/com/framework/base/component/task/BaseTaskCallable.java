package com.framework.base.component.task;

import java.util.concurrent.Callable;

public abstract class BaseTaskCallable implements Callable<Object> {
    private final  TaskCall taskCall;
    private TaskFileInfo taskFileInfo;

    public BaseTaskCallable(TaskFileInfo taskFileInfo,  TaskCall taskCall) {
        this.taskFileInfo = taskFileInfo;
        this.taskCall = taskCall;
    }

    public TaskFileInfo getTaskFileInfo() {
        return taskFileInfo;
    }

    @Override
    public Long call() throws Exception {
        doTask(taskFileInfo, taskCall);
        return taskFileInfo.getProgress();
    }

    public abstract void doTask(TaskFileInfo taskFileInfo,  TaskCall taskCall);
}


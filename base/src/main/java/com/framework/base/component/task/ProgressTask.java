package com.framework.base.component.task;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ProgressTask<V> extends FutureTask<V> {
    private TaskFileInfo taskFileInfo;

    public ProgressTask(Callable callable) {
        super(callable);
        taskFileInfo = ((BaseTaskCallable) callable).getTaskFileInfo();
    }

    public TaskFileInfo getTaskFileInfo() {
        return taskFileInfo;
    }
}

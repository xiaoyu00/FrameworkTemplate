package com.framework.base.component.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TaskManager {
    private static final int defaultThreadSize = 5;
    private final ExecutorService executorService;
    private static final ArrayList<ProgressTask<Object>> futureTasks = new ArrayList<>();

    private static class TaskManagerInstance {
        private static final TaskManager INSTANCE = new TaskManager(defaultThreadSize);
    }

    public static TaskManager getInstance() {
        return TaskManagerInstance.INSTANCE;
    }

    public TaskManager(int threadSize) {
        if (threadSize == 0) {
            threadSize = defaultThreadSize;
        }
        executorService = Executors.newFixedThreadPool(threadSize);
    }

    public List<ProgressTask<Object>> getTasks() {
        return futureTasks;
    }

    public static void addTask(ProgressTask<Object> task) {
        futureTasks.add(task);
    }

    public void stopTasks() {
        executorService.shutdownNow();
        futureTasks.clear();
    }

    public void startTasks() {
        futureTasks.forEach(executorService::execute);
    }

    public boolean removeTask(int index) {
        boolean isSuccess = false;
        if (index < futureTasks.size()) {
            FutureTask<Object> task = futureTasks.get(index);
            if (!task.isDone()) {
                isSuccess = task.cancel(true);
            }
        }
        return isSuccess;
    }

    public boolean isFinished() {
        return executorService.isShutdown();
    }

    public synchronized boolean checkAddThreadFinished() {

        //假设全部完成啦
        boolean allFinished = true;

        //遍历集合
        for (FutureTask<Object> task : futureTasks) {
            if (!task.isDone()) {
                allFinished = false;
                break;
            }
        }
        return allFinished;
    }
}

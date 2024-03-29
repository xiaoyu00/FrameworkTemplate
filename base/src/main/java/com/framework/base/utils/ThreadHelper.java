package com.framework.base.utils;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程工具，用于执行线程等
 */
public final class ThreadHelper {
    public static final ThreadHelper INST = new ThreadHelper();

    private ExecutorService executors;

    private ThreadHelper(){
    }
    /**
     * 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    /**
     * 在线程中执行
     * @param runnable 要执行的runnable
     */
    public void execute(Runnable runnable) {
        ExecutorService executorService = getExecutorService();
        if (executorService != null) {
            executorService.execute(runnable);
        } else {
            new Thread(runnable).start();
        }
    }

    /**
     * 获取缓存线程池
     * @return 缓存线程池服务
     */
    private ExecutorService getExecutorService(){
        if (executors == null) {
            executors = Executors.newCachedThreadPool();
        }

        return executors;
    }
}

package com.framework.base.component.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadTaskCallable extends BaseTaskCallable {
    private boolean cancelDownload = false;

    public DownloadTaskCallable(TaskFileInfo taskFileInfo, TaskCall taskCall) {
        super(taskFileInfo, taskCall);
    }

    @Override
    public void doTask(TaskFileInfo taskFileInfo, TaskCall taskCall) {
        okHttpDownload(taskFileInfo, taskCall);
    }

    private void normalDownload(TaskFileInfo downloadInfo, TaskCall taskCall) {
        try {
            // 获得存储卡的路径
            URL url = new URL(downloadInfo.getTaskUrl());
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.connect();
            // 获取文件大小
            int length = conn.getContentLength();
            taskCall.onStart(length);
            // 创建输入流
            InputStream is = conn.getInputStream();
            File file = new File(downloadInfo.getFilePath());
            // 判断文件目录是否存在
            if (!file.exists()) {
                file.mkdir();
            }
            File apkFile = new File(downloadInfo.getFilePath(), downloadInfo.getFileName());
            FileOutputStream fos = new FileOutputStream(apkFile);
            // 缓存
            byte[] buf = new byte[1024];
            // 写入到文件中
            int numRead;
            do {
                numRead = is.read(buf);
                downloadInfo.setProgress(downloadInfo.getProgress() + numRead);
                // 更新进度
                taskCall.onProgress(downloadInfo.getProgress(), length);
                if (numRead <= 0) {
                    // 下载完成
                    taskCall.onFinish();
                }
                if (numRead == -1) {
                    break;
                }
                // 写入文件
                fos.write(buf, 0, numRead);
            } while (!cancelDownload);// 点击取消就停止下载.
            if (numRead > 0) {
                // 下载中断
                taskCall.onCancel();
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            taskCall.onError();
        }
    }

    private void okHttpDownload(TaskFileInfo downloadInfo, TaskCall taskCall) {
        try {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30000, TimeUnit.MILLISECONDS).readTimeout(30000, TimeUnit.MILLISECONDS).build();
            Request request = new Request.Builder()
                    .url(downloadInfo.getTaskUrl())
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            //获取下载的内容输入流
            ResponseBody body = response.body();
            InputStream inputStream = body.byteStream();
            final long length = body.contentLength();
            taskCall.onStart(length);
            // 文件保存到本地
            File file = new File(downloadInfo.getFilePath(), downloadInfo.getFileName());
            FileOutputStream outputStream = new FileOutputStream(file);
            int lien = 0;
            byte[] bytes = new byte[1024];
            while ((lien = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, lien);
                downloadInfo.setProgress(downloadInfo.getProgress() + lien);
                // 更新进度
                taskCall.onProgress(downloadInfo.getProgress(), length);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            taskCall.onFinish();

        } catch (Exception e) {
            e.printStackTrace();
            taskCall.onError();
        }
    }
}


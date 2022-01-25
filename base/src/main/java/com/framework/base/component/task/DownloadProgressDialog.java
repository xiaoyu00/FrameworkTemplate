package com.framework.base.component.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

public class DownloadProgressDialog extends ProgressDialog {
    private Handler handler = new Handler();

    public DownloadProgressDialog(@NonNull Context context, List<TaskFileInfo> taskFileInfos) {
        super(context);
        addTaskInfos(taskFileInfos);
    }

    private void setDownloadInfo(int taskIndex, int type, long progress, long total) {
        View view = progressListView.getChildAt(taskIndex);
        if (view != null) {
            ProgressAdapter.ProgressViewHolder progressViewHolder = (ProgressAdapter.ProgressViewHolder) view.getTag();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case 0:
                            setStart(progressViewHolder, total);
                            break;
                        case 1:
                            setProgress(progressViewHolder, progress, total);
                            break;
                        case 2:
                            setFinish(progressViewHolder);
                            break;
                        case 3:
                            setError(progressViewHolder);
                            break;
                        default:
                            break;
                    }
                }
            });

        }
    }

    private void setStart( ProgressAdapter.ProgressViewHolder progressViewHolder, long size) {
        progressViewHolder.describe.setText("正在下载");
        if (size <= 0) {
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void setProgress( ProgressAdapter.ProgressViewHolder progressViewHolder, long progress, long total) {
        if (!progressViewHolder.progressBar.isIndeterminate()) {
            int progress2 = (int) (((float) progress / total) * 100);
            progressViewHolder.progressBar.setProgress(progress2);
        }
    }

    private void setFinish( ProgressAdapter.ProgressViewHolder progressViewHolder) {
        progressViewHolder.describe.setText("下载完成");
        if ( TaskManager.getInstance().checkAddThreadFinished()) {
            dismiss();
        }
    }

    private void setError( ProgressAdapter.ProgressViewHolder progressViewHolder) {
        progressViewHolder.describe.setText("下载出错");
    }

    @Override
    void initTasks() {
        for (int i = 0; i < taskFileInfos.size(); i++) {
            int finalI = i;
             TaskManager.addTask(new ProgressTask<Object>(new  DownloadTaskCallable(taskFileInfos.get(i), new TaskCall() {
                @Override
                public void onStart(long size) {
                    setDownloadInfo(finalI, 0, 0, size);

                }

                @Override
                public void onProgress(long progress, long total) {
                    setDownloadInfo(finalI, 1, progress, total);

                }

                @Override
                public void onFinish() {
                    setDownloadInfo(finalI, 2, 0, 0);
                }

                @Override
                public void onError() {
                    setDownloadInfo(finalI, 3, 0, 0);
                }

                @Override
                public void onCancel() {

                }
            })));
        }
         TaskManager.getInstance().startTasks();
    }
}

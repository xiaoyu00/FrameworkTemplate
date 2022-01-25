package com.framework.base.component.task;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.framework.base.R;

import java.util.ArrayList;
import java.util.List;

abstract class ProgressDialog extends Dialog {

    ListView progressListView;
    ProgressAdapter progressAdapter;
    List<TaskFileInfo> taskFileInfos = new ArrayList<>();

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        this.setCanceledOnTouchOutside(false);
        initViews();
        initTasks();
    }

    private void initViews() {
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        progressListView = findViewById(R.id.lv_progress);
        progressAdapter = new ProgressAdapter(taskFileInfos, position -> {
            boolean success =  TaskManager.getInstance().removeTask(position);
            if (success) {
                taskFileInfos.remove(position);
                progressAdapter.notifyDataSetChanged();
            }
        });
        progressListView.setAdapter(progressAdapter);
    }

    public void addTaskInfos(List<TaskFileInfo> taskFileInfoList) {
        taskFileInfos.addAll(taskFileInfoList);
    }

    abstract void initTasks();

    @Override
    public void dismiss() {
         TaskManager.getInstance().stopTasks();
        super.dismiss();
    }
}

package com.framework.base.component.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framework.base.R;

import java.util.List;

public class ProgressAdapter extends BaseAdapter {
    private final List<TaskFileInfo> taskFileInfoList;
    private final ProgressCancelListener progressCancelListener;

    ProgressAdapter(List<TaskFileInfo> taskFileInfoList, ProgressCancelListener progressCancelListener) {
        this.taskFileInfoList = taskFileInfoList;
        this.progressCancelListener = progressCancelListener;
    }

    @Override
    public int getCount() {
        return taskFileInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskFileInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskFileInfo taskFileInfo = taskFileInfoList.get(position);
        ProgressViewHolder progressViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progress_item, null);
            progressViewHolder = new ProgressViewHolder();
            progressViewHolder.progressBar = convertView.findViewById(R.id.progress);
            progressViewHolder.ivCancel = convertView.findViewById(R.id.iv_cancel);
            progressViewHolder.fileName = convertView.findViewById(R.id.tv_name);
            progressViewHolder.describe = convertView.findViewById(R.id.tv_describe);
            convertView.setTag(progressViewHolder);
        } else {
            progressViewHolder = (ProgressViewHolder) convertView.getTag();
        }
        progressViewHolder.progressBar.setProgress((int) taskFileInfo.getProgress());
        progressViewHolder.fileName.setText(taskFileInfo.getFileName());
        progressViewHolder.ivCancel.setVisibility(taskFileInfo.isCancel() ? View.VISIBLE : View.GONE);
        progressViewHolder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressCancelListener != null) {
                    progressCancelListener.onCancel(position);
                }
            }
        });
        return convertView;
    }

    interface ProgressCancelListener {
        void onCancel(int position);
    }

    static class ProgressViewHolder {
        ProgressBar progressBar;
        ImageView ivCancel;
        TextView fileName;
        TextView describe;
    }
}

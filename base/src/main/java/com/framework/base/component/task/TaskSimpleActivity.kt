package com.framework.base.component.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.framework.base.R
import java.util.ArrayList

class TaskSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_simple)
    }

    private fun testTask() {
        //https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component
        //https://codeload.github.com/tangpeng/VideoCompressor/zip/refs/heads/master
        val taskFileInfoList: MutableList<TaskFileInfo> = ArrayList()
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/tangpeng/VideoCompressor/zip/refs/heads/master",
                Environment.getExternalStorageDirectory().absolutePath, "aaaa.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "bbbbb.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "ccccc.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "ddddd.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "eeeee.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "fffff.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "ggggg.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "hhhhh.zip"
            )
        )
        taskFileInfoList.add(
            TaskFileInfo(
                "https://codeload.github.com/LuckSiege/PictureSelector/zip/refs/heads/version_component",
                Environment.getExternalStorageDirectory().absolutePath, "iiiii.zip"
            )
        )
        val progressDialog = DownloadProgressDialog(this@TaskSimpleActivity, taskFileInfoList)
        progressDialog.show()
    }
}
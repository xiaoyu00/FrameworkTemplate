package com.framework.template.simple;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.framework.base.utils.FileUtil;
import com.framework.template.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeChatDownloadActivity extends AppCompatActivity {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载中断*/
    private static final int DOWNLOAD_CANCEL = 3;

    ImageView downloadCancel;
    Button openFile;
    ProgressBar progressNormal;
    TextView tvFileName;
    LinearLayout activityJjjcFileDownload;
    LinearLayout progressLayout;
    ProgressBar progressCircle;
    /* 记录进度条数量 */
    private int progress = 0;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private int numread;
    private String mSavePath = Environment.getExternalStorageDirectory() + "/download";
    private String downPath = null, fileName = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
//                    progressNormal.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 下载完成
                    progressCircle.setVisibility(View.GONE);
                    openFile.setEnabled(true);
                    break;
                case DOWNLOAD_CANCEL:
                    // 下载中断
                    FileUtil.deleteFile(mSavePath + "/" + fileName);
                    break;
                default:
                    break;
            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_download);
        initViews();
        downPath = "http://jinzhong.grassmeta.cn/download/%E8%8B%8F%E5%B0%BC%E7%89%B9%E5%B7%A6%E6%97%97%E9%87%91%E4%B8%AD%E7%9F%BF%E5%8C%BA%E6%A4%8D%E8%A2%AB%E8%B0%83%E6%9F%A5%E6%8A%A5%E5%91%8A1006.pdf";//getIntent().getStringExtra("filePath");
        fileName = "苏尼特左旗金中矿区植被调查报告1006.pdf";//getIntent().getStringExtra("fileName");
        tvFileName.setText(fileName);
        progressLayout.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        downloadFile();
    }
    private void initViews(){
        downloadCancel =findViewById(R.id.download_cancel);
        downloadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelUpdate = true;
            }
        });
        openFile = findViewById(R.id.open_file);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.openFile(WeChatDownloadActivity.this, mSavePath + "/" + fileName, Manifest.permission.READ_EXTERNAL_STORAGE,fileName);
            }
        });
        progressNormal= findViewById(R.id.progress_normal);;
        tvFileName= findViewById(R.id.tv_file_name);;
        activityJjjcFileDownload= findViewById(R.id.activity_jjjc_file_download);;
        progressLayout= findViewById(R.id.progress_layout);;
        progressCircle= findViewById(R.id.progress_circle);
    }
//    private void initToolbar() {
//        TextView head_center_text = (TextView) findViewById(R.id.head_center_text);
//        head_center_text.setText("文件下载");
//        Button head_TitleBackBtn = (Button) findViewById(R.id.head_TitleBackBtn);
//        head_TitleBackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                exit();
//            }
//        });
//    }
    private void exit() {
        cancelUpdate = true;
        finish();
    }

    private void downloadFile() {
        new downloadThread().start();
    }
    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    URL url = null;
                    try {
                        url = new URL(downPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = null;
                    try {
                        apkFile = new File(mSavePath, fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        }
                        if (numread == -1) {
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    if (numread > 0) {
                        // 下载中断
                        mHandler.sendEmptyMessage(DOWNLOAD_CANCEL);
                    }

                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
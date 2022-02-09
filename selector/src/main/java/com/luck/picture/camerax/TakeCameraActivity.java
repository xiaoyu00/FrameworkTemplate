package com.luck.picture.camerax;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.luck.picture.camerax.listener.ClickListener;
import com.luck.picture.camerax.listener.FlowCameraListener;
import com.luck.picture.lib.R;


public class TakeCameraActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10010;
    private final String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String filePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_camera);
        if (checkSelfPermissions(this, permissions)) {
            initCamera();
        } else {
            requestPermission();
        }

    }

    public boolean checkSelfPermissions(Context ctx, String[] permissions) {
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ctx.getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }
        return isAllGranted;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                permissions,
                REQUEST_CODE);
    }

    private void initCamera() {
        FrameworkCameraView flowCamera = findViewById(R.id.flowCamera);
        // 设置拍照或拍视频回调监听
        flowCamera.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
        flowCamera.setFlowCameraListener(new FlowCameraListener() {
            @Override
            public void captureSuccess(@NonNull String path) {
                filePath=path;
                handleCameraSuccess();
            }

            @Override
            public void recordSuccess(@NonNull String path) {
                filePath=path;
                handleCameraSuccess();
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

            }
        });
    }

    private void handleCameraSuccess() {
        Uri uri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        Intent intent = new Intent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        setResult(RESULT_OK, getIntent());
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            } else {
                Toast.makeText(this, "请设置权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

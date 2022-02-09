package com.framework.mediaselect;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        findViewById(R.id.btn_album).setOnClickListener(view -> AlbumManager.showSelectDialog(ExampleActivity.this, new AlbumOperation(), null));
        findViewById(R.id.btn_gallery).setOnClickListener(view -> AlbumManager.openGallery(ExampleActivity.this, new AlbumOperation(), null));
        AlbumOperation albumOperation=new AlbumOperation();
        albumOperation.isCompress=true;
        findViewById(R.id.btn_camera).setOnClickListener(view -> AlbumManager.openCamera(ExampleActivity.this, albumOperation, new PathBackCall() {
            @Override
            public void onResult(List<String> paths) {
                Log.e("ssss","ssss::"+paths.toString());
            }
        }));
    }


}
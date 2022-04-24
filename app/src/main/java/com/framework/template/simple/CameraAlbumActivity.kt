package com.framework.template.simple

import android.os.Bundle
import com.framework.base.parent.basics.BaseBindingActivity
import com.framework.mediaselect.AlbumManager
import com.framework.mediaselect.AlbumOperation
import com.framework.template.R
import com.framework.template.databinding.ActivityCameraAlbumBinding

/**
 * 拍摄 相册（fragment 通用）
 */
class CameraAlbumActivity : BaseBindingActivity<ActivityCameraAlbumBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_text)
    }

    override fun contextViewId(): Int = R.layout.activity_camera_album

    override fun initialize() {
        dataBinding.openDialog.setOnClickListener {
            AlbumManager.showSelectDialog(this, AlbumOperation()) {

            }
        }
        dataBinding.openCamera.setOnClickListener {
            AlbumManager.openCamera(this, AlbumOperation()) {

            }

        }
        dataBinding.openGallery.setOnClickListener {
            AlbumManager.openGallery(this, AlbumOperation()) {

            }
        }
    }
}
package com.framework.template

import android.Manifest
import com.framework.base.parent.BaseViewModelActivity
import com.framework.mediaselect.AlbumManager
import com.framework.mediaselect.AlbumOperation
import com.framework.template.databinding.ActivityMainBinding

class MainActivity : BaseViewModelActivity<SimpleViewModel, ActivityMainBinding>() {
    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
    )

    override fun contextViewId() = R.layout.activity_main
    override fun initialize() {
        dataBinding.tvHello.setOnClickListener {
//            startActivity(Intent(this, WorkLoadingFragmentActivity::class.java))
//            startActivity(Intent(this, WorkLoadingActivity::class.java))

            AlbumManager.openCamera(this, AlbumOperation()){

            }
        }
    }

    override fun modelClass() = SimpleViewModel::class
}
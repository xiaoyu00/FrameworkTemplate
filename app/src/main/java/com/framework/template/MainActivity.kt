package com.framework.template

import android.Manifest
import android.content.Intent
import com.framework.base.parent.BaseViewModelActivity
import com.framework.share.ShareManager
import com.framework.template.databinding.ActivityMainBinding
import com.framework.template.simple.*

class MainActivity : BaseViewModelActivity<SimpleViewModel, ActivityMainBinding>() {
    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
    )

    override fun contextViewId() = R.layout.activity_main
    override fun initialize() {
        dataBinding.dataList.setOnClickListener {
            startActivity(Intent(this, DataListActivity::class.java))
        }
        dataBinding.dataLoadingFa.setOnClickListener {
            startActivity(Intent(this, WorkLoadingFragmentActivity::class.java))
        }
        dataBinding.dataLoadingA.setOnClickListener {
            startActivity(Intent(this, WorkLoadingActivity::class.java))
        }
        dataBinding.shapeLoading.setOnClickListener {
            startActivity(Intent(this, ShapeLoadingActivity::class.java))
        }
        dataBinding.openCamera.setOnClickListener {
            startActivity(Intent(this, CameraAlbumActivity::class.java))
        }
        dataBinding.permissionsCheck.setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }
        dataBinding.textHorse.setOnClickListener {
            startActivity(Intent(this, ScrollTextActivity::class.java))
        }
        dataBinding.textBanner.setOnClickListener {
            startActivity(Intent(this, TextBannerActivity::class.java))
        }
        dataBinding.weekUp.setOnClickListener {
            startActivity(Intent(this, WeekUpScreenActivity::class.java))
        }
        dataBinding.expandText.setOnClickListener {
            startActivity(Intent(this, ExpandableTextActivity::class.java))
        }
        dataBinding.btnNav.setOnClickListener {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    override fun modelClass() = SimpleViewModel::class
}
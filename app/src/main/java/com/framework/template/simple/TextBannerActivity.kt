package com.framework.template.simple

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.framework.base.views.textbanner.ITextBannerItemClickListener
import com.framework.base.views.textbanner.TextBannerView
import com.framework.template.R
import java.util.ArrayList

/**
 * text轮播
 */
class TextBannerActivity : AppCompatActivity() {
    private var mTvBanner: TextBannerView? = null
    private var mTvBanner1: TextBannerView? = null
    private var mTvBanner2: TextBannerView? = null
    private var mTvBanner3: TextBannerView? = null
    private var mTvBanner4: TextBannerView? = null
    private var mList= mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_banner)
        initView()
        initData()
        setListener()
    }
    private fun initView() {
        mTvBanner = findViewById(R.id.tv_banner)
        mTvBanner1 = findViewById(R.id.tv_banner1)
        mTvBanner2 = findViewById(R.id.tv_banner2)
        mTvBanner3 = findViewById(R.id.tv_banner3)
        mTvBanner4 = findViewById(R.id.tv_banner4)
    }

    private fun initData() {
        mList.add("学好Java、Android、C#、C、ios、html+css+js")
        mList.add("走遍天下都不怕！！！！！")
        mList.add("不是我吹，就怕你做不到，哈哈")
        mList.add("superluo")
        mList.add("你是最棒的，奔跑吧孩子！")
        /**
         * 设置数据，方式一
         */
        mTvBanner!!.setDatas(mList)
        mTvBanner!!.setDatas(mList)
        mTvBanner1!!.setDatas(mList)
        mTvBanner2!!.setDatas(mList)
        mTvBanner3!!.setDatas(mList)
        val drawable = resources.getDrawable(R.mipmap.ic_launcher)
        /**
         * 设置数据（带图标的数据），方式二
         */
        //第一个参数：数据 。第二参数：drawable.  第三参数drawable尺寸。第四参数图标位置
        mTvBanner4!!.setDatasWithDrawableIcon(mList, drawable, 18, Gravity.LEFT)
    }

    private fun setListener() {
        mTvBanner!!.setItemOnClickListener { data, position ->
            Toast.makeText(
                this@TextBannerActivity,
                "$position>>$data",
                Toast.LENGTH_SHORT
            ).show()
        }
        mTvBanner1!!.setItemOnClickListener { data, position ->
            Toast.makeText(
                this@TextBannerActivity,
                "$position>>$data",
                Toast.LENGTH_SHORT
            ).show()
        }
        mTvBanner2!!.setItemOnClickListener { data, position ->
            Toast.makeText(
                this@TextBannerActivity,
                "$position>>$data",
                Toast.LENGTH_SHORT
            ).show()
        }
        mTvBanner3!!.setItemOnClickListener { data, position ->
            Toast.makeText(
                this@TextBannerActivity,
                "$position>>$data",
                Toast.LENGTH_SHORT
            ).show()
        }
        mTvBanner4!!.setItemOnClickListener { data, position ->
            Toast.makeText(
                this@TextBannerActivity,
                "$position>>$data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onResume() {
        super.onResume()
        /**调用startViewAnimator()重新开始文字轮播 */
        mTvBanner!!.startViewAnimator()
        mTvBanner1!!.startViewAnimator()
        mTvBanner2!!.startViewAnimator()
        mTvBanner3!!.startViewAnimator()
        mTvBanner4!!.startViewAnimator()
    }

    override fun onStop() {
        super.onStop()
        /**调用stopViewAnimator()暂停文字轮播，避免文字重影 */
        mTvBanner!!.stopViewAnimator()
        mTvBanner1!!.stopViewAnimator()
        mTvBanner2!!.stopViewAnimator()
        mTvBanner3!!.stopViewAnimator()
        mTvBanner4!!.stopViewAnimator()
    }
}

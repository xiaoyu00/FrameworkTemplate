package com.framework.template.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.framework.base.views.expandabletext.ExpandableTextView
import com.framework.base.views.expandabletext.MoreLineTextView
import com.framework.template.R

/**
 * 可折叠textView
 */
class ExpandableTextActivity : AppCompatActivity() {
    val LOG_TAG = "logTag"
    val content1 = "       对阙人生，吟诵山水，在流光的年轮里，一颗素心，一抹浅笑，生活，自是从容！"
    val content2 = "       我于恬静中走来，踏着馥郁芳香，用悠长的思绪，酿造醇美的酒香。"
    val content3 =
        """       喜欢，一个人坐在午后阳光里，品一杯带着淡淡苦涩的茶香，听一曲温婉柔和的音乐，赏一处落满庭院的花瓣，任娴静温和的心情在暖阳下舒展、流淌。也许，有些心事来不及收起；也许，有些牵挂来不及放下，总会不时的激动和感慨。于是，将纷扰的时光在风轻云淡中释放，将冷暖的人生在云水禅心里生香。
       一度悠闲的时光，温柔了几许禅意的诗行，在一程简静的光阴里生香。人生，在历经生命种种后，呈现出一种豁达与从容，懂得了繁华过尽后的踏实和安稳，还原一个纯情本真的自己。学会在曲曲折折、绵绵密密的岁月里，将柔肠百转的心事洒落风中，旖旎一地落花的独白，一半是优雅，一半是人生。"""
    val content4 =
        """       季节的风，吹皱了遗留在屋檐下的纸鸢，风干了一季又一季的花瓣。旧时光里，依然会有婉转的心事零落，只是，不会再掀起浩荡的波澜，掠过眉弯轻轻滑过。原来，每个人心中都有一些无法言说的秘密，只是，岁月遗忘了彼此的真情，将一份禅缘，修行成一岸水墨青莲。只待风起，便是一季落花的深情，一场花开的欢喜。
       季节的风，吹皱了遗留在屋檐下的纸鸢，风干了一季又一季的花瓣。旧时光里，依然会有婉转的心事零落，只是，不会再掀起浩荡的波澜，掠过眉弯轻轻滑过。原来，每个人心中都有一些无法言说的秘密，只是，岁月遗忘了彼此的真情，将一份禅缘，修行成一岸水墨青莲。只待风起，便是一季落花的深情，一场花开的欢喜。"""
    private var mTvMoreLineShort: MoreLineTextView? = null
    private var mTvExpandableShort: ExpandableTextView? = null
    private var mTvMoreLineLong: MoreLineTextView? = null
    private var mTvExpandableLong: ExpandableTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_text)
        mTvMoreLineShort = findViewById(R.id.tv_more_line_short) as MoreLineTextView
        mTvExpandableShort = findViewById(R.id.tv_expandable_short) as ExpandableTextView
        mTvMoreLineLong = findViewById(R.id.tv_more_line_long) as MoreLineTextView
        mTvExpandableLong = findViewById(R.id.tv_expandable_long) as ExpandableTextView
        initData()
    }

    private fun initData() {
        mTvMoreLineShort?.setText(content1)
        mTvExpandableShort?.setText(content2)
        mTvMoreLineLong?.setText(content3)
        mTvExpandableLong?.setText(content4)
    }
}
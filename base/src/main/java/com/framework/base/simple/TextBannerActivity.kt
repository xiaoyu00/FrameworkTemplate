package com.framework.base.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.framework.base.R
import com.framework.base.views.textbanner.BaseBannerAdapter
import com.framework.base.views.textbanner.TextBannerView

/**
 * text轮播
 */
class TextBannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_banner)
        init()
    }

    private fun init() {
        val textBannerView = this.findViewById<TextBannerView>(R.id.text_banner)
        textBannerView.setAdapter(SimpleAdapter(listOf("1111111111111111", "22222222222222222222")))
    }
}

class SimpleAdapter(list: List<String>) : BaseBannerAdapter<String>(list) {
    override fun getView(parent: TextBannerView?): View {
        return LayoutInflater.from(parent!!.context).inflate(R.layout.item_simple_text_banner, null)
    }

    override fun setItem(view: View?, data: String?) {
        val textView = view!!.findViewById<View>(R.id.text_content) as TextView
        textView.text = data
    }


}
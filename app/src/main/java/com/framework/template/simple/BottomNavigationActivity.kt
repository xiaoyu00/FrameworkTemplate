package com.framework.template.simple

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.framework.base.adapter.ViewPagerAdapter
import com.framework.base.utils.DensityUtils
import com.framework.template.R
import com.framework.template.databinding.ActivityBottomNavigationBinding
import com.framework.template.simple.fragments.DashboardFragment
import com.framework.template.simple.fragments.HomeFragment
import com.framework.template.simple.fragments.MyFragment
import com.framework.template.simple.fragments.NotificationsFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeDrawable.TOP_START

/**
 *底部导航栏
 */
class BottomNavigationActivity : AppCompatActivity() {
    private val fragments =
        listOf(
            HomeFragment(),
            DashboardFragment(),
            NotificationsFragment(),
            MyFragment()
        )
    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }
    //BottomNavigationView可以根据三个材料主题子系统为主题：颜色，版式和形状。有两个从继承的样式变体Widget.MaterialComponents.BottomNavigationView，
    //每个样式变体都带有可选的样式后缀：Surface（默认，无后缀）和彩色（*.Colored）。实施全局自定义BottomNavigationView样式时，
    //请在您的应用程序主题中使用bottomNavigationStyle属性引用它。

    //徽章也可以是主题。现有的样式是一种；Widget.MaterialComponents.Badge。
    //实施全局自定义徽章样式时，请在您的应用程序主题中使用badgeStyle属性引用它。

    private fun initView() {
        binding.navViewPager.adapter = ViewPagerAdapter(this, fragments)
        binding.navViewPager.offscreenPageLimit = 3
        binding.navViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.navView.menu.getItem(position).isChecked = true
            }
        })
        binding.navViewPager.isUserInputEnabled = false
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> binding.navViewPager.setCurrentItem(0, false)
                R.id.navigation_dashboard -> binding.navViewPager.setCurrentItem(1, false)
                R.id.navigation_notifications -> binding.navViewPager.setCurrentItem(2, false)
                R.id.navigation_my-> binding.navViewPager.setCurrentItem(3, false)
            }
            true
        }
        // 图片的切换，不使用默认的修改图片颜色 (app:itemIconTint="@null" 无效 主题色会覆盖nav_icon颜色)
        binding.navView.itemIconTintList=null
        //Show badge(获取并创建角标)
//        val badge1 = binding.navView.getOrCreateBadge(R.id.navigation_home)
        val badge2 = binding.navView.getOrCreateBadge(R.id.navigation_dashboard)
        val badge3 = binding.navView.getOrCreateBadge(R.id.navigation_notifications)
        val badge4 = binding.navView.getOrCreateBadge(R.id.navigation_my)
        //Remove badge(角标)
//        binding.navView.removeBadge(R.id.item1)
        //获取角标
//        val badge = binding.navView.getBadge(R.id.item1)

        // setBadgeGravity/ getBadgeGravity：用于设置/获取它可以是徽章的严重性
        // TOP_END，TOP_START，BOTTOM_END或BOTTOM_START。默认值为TOP_END。
        badge3.badgeGravity= BadgeDrawable.TOP_START

        // setNumber/ getNumber/ hasNumber/ clearBadgeNumber：
        // 用于分配，检索，检查和清除徽章内显示的数字。默认情况下，显示的徽章没有数字。
        badge3.number=19
        badge4.number=1020
        badge4.backgroundColor= Color.BLUE
        // setMaxCharacterCount/ getMaxCharacterCount：
        // 用于设置/获取徽章数字中允许的最大字符数，然后将其用'+'截断。预设值为4。
        badge4.maxCharacterCount=3
        badge4.verticalOffset=DensityUtils.dpToPx(this,4.0f)
    }
}
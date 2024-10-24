package com.framework.template.simple;

import android.os.PowerManager;

/**
 * Android 定位SDK在手机黑屏后定位失败怎么办？
 * Android 设备在黑屏的分钟后可能会发生多种情况：
 * 应用程序切后台后进程资源被系统回收，导致不能持续定位。
 * 解决办法：
 * 1、对于原生Android系统可采用google给出的提升后台应用进程优先级的解决方案来解决，可参考google Android 开发者官网。
 * 2、对于国内厂商提供的Android系统需要联系到对应的厂商进行系统底层应用白名单授权，才可以保证App进程在后台处于活跃状态。
 * CPU会处于休眠状态（不同厂商生产的设备CPU休眠时间不尽相同）（包含AP［Application Processor，ARM架构的处理器，用于支撑Android系统运行］和BP［Baseband Processor，运行实时操作系统，通讯协议栈等］）。一旦当CPU处于休眠状态，设备将无法正常链接网络，APP的定位请求也将无法正常发送。
 * 解决办法：
 * 1、通过创建Timer来保持CPU唤醒状态：
 * Android 的 Timer 类可以用来计划需要执行的任务。但 Timer 的问题是比较消耗手机电量（实现是用 WakeLock 让 CPU 保持唤醒状态）；另外一点是：部分厂商将WakeLock也设置了休眠时间，就是说 Timer 很可能和CPU一起处于休眠状态。Timer 类只能解决一小部分问题。
 * 2、通过AlarmManager保持CPU处于唤醒状态：
 * AlarmManager 是 Android 系统封装的用于管理 RTC 的模块，RTC (Real Time Clock) 是一个独立的硬件时钟，可以在 CPU 休眠时正常运行，在预设的时间到达时，通过中断唤醒 CPU。用 AlarmManager 来定时执行任务，CPU 可以正常的休眠，需要运行定位时醒来即可。但部分厂商为了使设备更加省电，将AlarmManager也做出了修改，例如5s一次的响应更改为50s或者是几分钟，有些干脆在CPU休眠后彻底停掉了。
 * 3、通过心跳长链接保持client端CPU处于唤醒状态：
 * 最佳唤醒CPU的方法是通过server端与client端的长链接通信。例如每次长链接保持5分钟时间，每30s通信一次，这样可以有效确保CPU处于唤醒状态。
 *
 * ----------------------------------------------------------------------------------
 *
 * Android 锁屏后UI不刷新的问题通常是因为锁屏界面（Keyguard）会在显示后暂停所有应用程序的渲染。为了省电和性能优化，系统可能会限制在锁屏状态下的UI更新。
 * 要解决这个问题，可以尝试以下方法：
 * 1.使用WindowManager.LayoutParams的FLAG_KEEP_SCREEN_ON标志来保持屏幕常亮。
 * 2.使用PowerManager的WakeLock来保持CPU运转。
 * 3.如果是自定义的锁屏界面，可以考虑使用KeyguardManager来解锁屏幕。
 * 以下是一个简单的示例代码，展示如何在Activity中使用WakeLock保持CPU运转
 */
public class StayAliveActivity extends AppCompatActivity {
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xxx);

        // 获取PowerManager服务
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 创建一个WakeLock
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MyWakeLockTag");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取屏幕常亮权限并保持屏幕开启
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 获取并持有WakeLock
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 释放WakeLock
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}

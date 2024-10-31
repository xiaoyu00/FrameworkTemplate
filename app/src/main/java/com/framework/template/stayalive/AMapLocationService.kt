package com.framework.template.stayalive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.MapsInitializer


/**
 * 高德定位Service
 * 把定位放到service中，通过不断刷新Notification，来实现cpu唤醒，达到熄屏后持续定位效果,
 * 配合android:foregroundServiceType="location"使用效果更好（在华为手机省电模式下无效，一段时间后会被杀死）
 *
 *注：与StayAliveActivity熄屏保护配合效果更好
 * 为了进一步保活，可以使用音乐app的方式
 */
class AMapLocationService : Service() {

    private val TAG = AMapLocationService::class.java.simpleName
    private val RECEIVER_INTERVAL = 5000L

    // 启动notification的id，两次启动应是同一个id
    private val NOTIFICATION_ID = Process.myPid()
    private val channelId = "LocationServiceId"

    private var mLocationClient: AMapLocationClient?=null
    private var mLocationOption: AMapLocationClientOption = AMapLocationClientOption()
    private var count=0
    private var  mAMapLocationListener = AMapLocationListener {ap ->
        if (ap != null) {
            if (ap.errorCode == 0) {
                //解析定位结果
                Log.e(TAG,"定位成功：${ap.address}")
                count++
                refreshNotification("正在定位：：$count")
            }else{
                Log.e(TAG,"定位失败：${ap.errorCode}  ${ap.errorInfo}")
            }
        }else{
            Log.e(TAG,"定位失败：${ap?.errorCode}   ${ap?.errorInfo}")
        }
    }

    private fun initLocation(){
        MapsInitializer.updatePrivacyShow(applicationContext, true, true)
        MapsInitializer.updatePrivacyAgree(applicationContext, true)
        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.interval=2000 // 2秒定位一次
        mLocationClient?.setLocationOption(mLocationOption)
        //设置定位回调监听
        mLocationClient?.setLocationListener(mAMapLocationListener)
        //启动定位
        mLocationClient?.startLocation()
    }
    private fun destroyLocation(){
        mLocationClient?.onDestroy()
    }
    private fun sendNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //数字是随便写的“40”，
            nm.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "WorkService",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        startForeground(NOTIFICATION_ID, getNotification())
    }
    private fun getNotification(
        title: String = resources.getText(R.string.app_name).toString(),
        contentString:String = (resources.getText(R.string.app_name).toString() + "正在后台运行")): Notification? {
        val builder = Notification.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(contentString)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId)
        }
        return builder.build()
    }
    private fun refreshNotification(contentString: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //数字是随便写的“40”，
            nm.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "WorkService",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        startForeground(NOTIFICATION_ID, getNotification(contentString=contentString))
    }
    // 首次创建服务时调用 在onStartCommand 或 onBind 之前调用
    override fun onCreate() {
        super.onCreate()
        sendNotification()
        initLocation()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    // 每次通过startService()方法启动时都会被调用
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        destroyLocation()
    }
}
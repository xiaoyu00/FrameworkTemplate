package com.framework.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.framework.base.utils.FileUtil

///**
// * 首先在AndroidManifest.xml注册ACTION事件(接收文件的activity里)
// *  <activity
// *   android:name="com.test.app.MainActivity"
// *   android:configChanges="orientation|keyboardHidden|screenSize"
// *   android:label="这里的名称会对外显示"
// *   android:launchMode="singleTask"
// *   android:screenOrientation="portrait">
// *   //注册接收分享
// *   <intent-filter>
// *     <action android:name="android.intent.action.SEND" />
// *     <category android:name="android.intent.category.DEFAULT" />
// *     //接收分享的文件类型
// *     <data android:mimeType="image/*" />
// *     <data android:mimeType="application/msword" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
// *     <data android:mimeType="application/vnd.ms-excel" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
// *     <data android:mimeType="application/vnd.ms-powerpoint" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.presentation" />
// *     <data android:mimeType="application/pdf" />
// *     <data android:mimeType="text/plain" />
// *   </intent-filter>
// *   <intent-filter>
// *     <action android:name="android.intent.action.SEND" />
// *     <category android:name="android.intent.category.DEFAULT" />
// *     //接收分享的文件类型
// *     <data android:mimeType="image/*" />
// *     <data android:mimeType="application/msword" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
// *     <data android:mimeType="application/vnd.ms-excel" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
// *     <data android:mimeType="application/vnd.ms-powerpoint" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.presentation" />
// *     <data android:mimeType="application/pdf" />
// *     <data android:mimeType="text/plain" />
// *   </intent-filter>
// *
// *   //注册默认打开事件，微信、QQ的其他应用打开
// *   <intent-filter>
// *     <action android:name="android.intent.action.VIEW" />
// *     <category android:name="android.intent.category.DEFAULT" />
// *
// *     //接收打开的文件类型
// *     <data android:scheme="file" />
// *     <data android:scheme="content" />
// *     <data android:mimeType="image/*" />
// *     <data android:mimeType="application/msword" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
// *     <data android:mimeType="application/vnd.ms-excel" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
// *     <data android:mimeType="application/vnd.ms-powerpoint" />
// *     <data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.presentation" />
// *     <data android:mimeType="application/pdf" />
// *     <data android:mimeType="text/plain" />
// *   </intent-filter>
// * </activity>
// *
// * 当APP进程在后台时，会调用Activity的onNewIntent方法
// * 当APP进程被杀死时，会调用onCreate方法
// * 所以在两个方法中都需要监听事件
// * @Override
// * protected void onCreate(@Nullable Bundle savedInstanceState) {
// *  super.onCreate(savedInstanceState);
// *  receiveActionSend(intent);
// * }
// * @Override
// * protected void onNewIntent(Intent intent) {
// *  super.onNewIntent(intent);
// *  receiveActionSend(intent);
// * }
// */
object ReceiveManager {
    fun receiveActionSend(
        context: Context,
        intent: Intent,
        onReceive: (paths: List<String>) -> Unit
    ) {
        val action = intent.action
        val type = intent.type
        //判断action事件
        if (type == null || (Intent.ACTION_VIEW != action && Intent.ACTION_SEND != action && Intent.ACTION_SEND_MULTIPLE != action)) {
            return;
        }
        val paths = mutableListOf<String>()
        if (Intent.ACTION_SEND == action) {
            //取出文件uri
            var uri = intent.data
            if (uri == null) {
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM)

                FileUtil.getFileFromUri(context, uri)?.let {
                    paths.add(it)
                }
            }

        } else if (Intent.ACTION_SEND_MULTIPLE == action) {
            val arrayList: ArrayList<Uri>? = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
            arrayList?.forEach {
                FileUtil.getFileFromUri(context, it)?.let { path ->
                    paths.add(path)
                }
            }
        }
        onReceive.invoke(paths)
    }

}
package com.framework.base.component.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.framework.base.R;

public class WebViewActivity extends AppCompatActivity {
    private String url="https://www.baidu.com/";
    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initWebView();
    }
    private void initWebView(){
        webView=findViewById(R.id.view_web);
        webSettings=webView.getSettings();
        // 设置可以访问文件s
        webSettings.setAllowFileAccess(true);
        webView.loadUrl(url);
        //webView.loadData(); ///加载string类型数据 如html代码
        //webView.loadUrl("file:///android_asset/icon.png");  //工程目录assets 图片文件
        //webView.loadUrl("file:///android_asset/index.html");  //工程目录assets index.html文件
        //webView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");//本地sdcard文件。
//        webView.canGoBack() 当前页面是否可以返回
//        webView.goBack() 返回当前页面
//        webView.canGoForward() 当前页面是否可以向前
//        webView.goForward() 向前进入下一个页面
//        webView.canGoBackOrForward(int steps) 当前页面是否可以返回或者进入下一个页面 返回值为boolean
//        webView.goBackOrForward(int steps) 当前页面要前进或者后退几步

//        注意：JS代码调用一定要在 onPageFinished() 回调之后才能调用，否则不会调用
//        onPageFinished()属于WebViewClient类的方法，主要在页面加载结束时调用
        // 注意调用的JS方法名要对应上
        // 调用javascript的callJS()方法
//        webView.loadUrl("javascript:callJS()");

//        通过 WebView 的addJavascriptInterface()进行对象映射
//        通过 WebViewClient 的shouldOverrideUrlLoading()方法回调拦截 url
//        通过 WebChromeClient 的 onJsAlert()、onJsConfirm()、onJsPrompt()方法回调拦截JS对话框alert()、confirm()、prompt() 消息
// 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
//        webView.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象

        webView.setWebViewClient(new WebViewClient(){
            //监听到页面发生跳转的情况，默认打开web浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            //页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Log.e("aaaa","start;;;;;;");
            }
            //页面加载完成的回调方法
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("aaaa","finish;;;;;;");
                //在webView中加载js代码
//                webView.loadUrl("javascript:alert('hello')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            //监听网页进度 newProgress进度值在0-100
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e("aaaa","progress;;;;;;"+newProgress);
            }
            //设置Activity的标题与 网页的标题一致
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}

class JstoAndroid extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        System.out.println("JS调用了Android的hello方法");
    }
}

class JstoAndroid2 extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public String hello(String msg) {
        System.out.println("JS调用了Android的hello方法");
        return "shuju";
    }
}

//<!DOCTYPE html>
//<html lang="en">
//<head>
//<meta charset="UTF-8">
//<meta name="Generator" content="EditPlus®">
//<meta name="Author" content="">
//<meta name="Keywords" content="">
//<meta name="Description" content="">
//<title>Document</title>
//<script>
//	function callAndroid(){
//            // 由于对象映射，所以调用test对象等于调用Android映射的对象
//            test.hello("js调用了android中的hello方法");
//            }
//</script>
//</head>
//<body>
////点击按钮则调用callAndroid函数
//<button type="button" id="button1" onclick="callAndroid()"></button>
//</body>
//</html>

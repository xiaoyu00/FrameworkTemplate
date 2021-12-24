package com.framework.base.work;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Options;



public class OkHttpDo {
    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }

    private Request.Builder getRequestBuild() {
        return new Request.Builder().headers(null).url("");
    }

    public void doPost(String url, Map<Object, String> params, Callback callback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add()
        HttpBody.getPostBody(params);
        body
        config
        Request request = getRequestBuild().url(url).post(formBodyBuilder.build()).build();
        getOkHttpClient().newCall(request).enqueue(callback);
    }
    public void doGet(String url, Map<String, String> params){

    }
}

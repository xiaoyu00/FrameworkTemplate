package com.framework.base.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/**
 * 基础网络请求
 */
public class HttpUtil {

	public static String getData(String httpUrl, String postData) {
		try {
			StringBuilder resultData = new StringBuilder("");
			URL url = new URL(httpUrl);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(10000);
			urlConn.connect();

			OutputStream outputStream = urlConn.getOutputStream();// 向服务器写入
			DataOutputStream out = new DataOutputStream(outputStream);
			// DataOutputStream is objectOutputStream的子类，也可以用objectOutputStream类
			// 要上传的参数
			// 将要上传的内容写入流中
			out.writeBytes(postData);// 要是用objectOutputStream就是out.writeObject(content);//写入服务器的参数，但是放到内存中了
			// content = "&wife=" + URLEncoder.encode("lyx", "gb2312");
			// out.writeBytes(content);
			// 刷新、关闭
			out.flush();// 真正的写过去了
			out.close();

			// inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer
			// getInputStream是真正去连接网络获取数据
			InputStreamReader isr = new InputStreamReader(urlConn.getInputStream());
			// 使用缓冲一行行的读入，加速InputStreamReader的速度
			BufferedReader buffer = new BufferedReader(isr);
			String inputLine = null;

			while ((inputLine = buffer.readLine()) != null) {
				resultData.append(inputLine);
			}
			buffer.close();
			isr.close();
			urlConn.disconnect();
			return resultData.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("HttpGetError", httpUrl);
			e.printStackTrace();
			return null;
		}
	}

}

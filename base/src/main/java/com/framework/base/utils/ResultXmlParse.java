package com.framework.base.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class ResultXmlParse {

//	private static PublicInfos publicInfos = PublicInfos.getPublicInfos();
//
//	class errorType {
//		public static final String PASSWORD_ERROR = "STUDENT PASSWORD ERROR";
//		public static final String ACCOUNT_ERROR = "STUDENT IDENTITY ERROR";
//		public static final String TOKEN_ERROR = "STUDENT TOKEN ERROR";
//		public static final String PARAMETER_ERROR = "PARAMETER_ERROR";
//		public static final String TIMESTAMP_ERROR = "TIMESTAMP ERROR";
//	}
//
//	private static String resultIsError(String s) {
//		String errorString = null;
//		String returnString = null;
//		String errorTime = null;
//
//		InputStream is = new ByteArrayInputStream(s.getBytes());
//		XmlPullParser parser = Xml.newPullParser();
//		try {
//			parser.setInput(is, "UTF-8");
//			int eventType = parser.getEventType();
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//				switch (eventType) {
//				case (XmlPullParser.START_DOCUMENT):
//
//					break;
//				case (XmlPullParser.START_TAG):
//
//					String tagName = parser.getName();
//					switch (tagName) {
//					case "error":
//						errorString = parser.nextText();
//						break;
//					case "time":
//						errorTime = parser.nextText();
//						break;
//
//					default:
//						break;
//					}
//
//					break;
//				case (XmlPullParser.END_TAG):
//					break;
//				}
//				eventType = parser.next();
//			}
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.i("XmlPullParser ERROR", "xml解析异常");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			Log.i("IO ERROR", "IO异常");
//		}
//
//		switch (errorString) {
//		case errorType.PASSWORD_ERROR:
//			returnString = "用户名或密码错误";
//			break;
//		case errorType.ACCOUNT_ERROR:
//			returnString = "用户名或密码错误";
//			break;
//		case errorType.TOKEN_ERROR:
//			returnString = "身份已失效，请重新登录";
//			break;
//		case errorType.TIMESTAMP_ERROR:
//			returnString = "时间有误，请设置时间后重试！";
//			break;
//		case errorType.PARAMETER_ERROR:
//			returnString = "身份已失效，请重新登录";
//			break;
//
//		default:
//			returnString = "未知错误";
//			break;
//		}
//
//		return returnString;
//	}
//
//	// 登录
//	public static String loginCheckXmlParse(Context context, String parseString) {
//		String returnString = null;
//
//		if (parseString.contains("error")) {
//
//			returnString = resultIsError(parseString);
//
//		} else {
//			InputStream is = new ByteArrayInputStream(parseString.getBytes());
//			XmlPullParser parser = Xml.newPullParser();
//			try {
//				parser.setInput(is, "UTF-8");
//				int eventType = parser.getEventType();
//				while (eventType != XmlPullParser.END_DOCUMENT) {
//					switch (eventType) {
//					case (XmlPullParser.START_DOCUMENT):
//
//						break;
//					case (XmlPullParser.START_TAG):
//
//						String tagName = parser.getName();
//						switch (tagName) {
//						case "studentToken":
//							String s = parser.nextText();
//							publicInfos.setStudentToken(s);
//							FileUtil.saveToken(context, s);
//							break;
//						case "studentAvatar":
//							FileUtil.saveStudentAvatar(context, parser.nextText());
//							break;
//						default:
//							break;
//						}
//
//						break;
//					case (XmlPullParser.END_TAG):
//
//						break;
//					}
//					eventType = parser.next();
//				}
//
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.i("XmlPullParser ERROR", "xml解析异常");
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				Log.i("IO ERROR", "IO异常");
//			}
//			returnString = "success";
//		}
//
//		return returnString;
//	}
}

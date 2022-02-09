package com.luck.picture.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.luck.picture.basic.PictureContentResolver;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.interfaces.OnCallbackListener;
import com.luck.picture.thread.PictureThreadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author：luck
 * @date：2021/11/25 2:02 下午
 * @describe：DownloadFileUtils
 */
public class DownloadFileUtils {

    /**
     * 保存文件
     *
     * @param context  上下文
     * @param path     文件路径
     * @param mimeType 文件类型
     * @param listener 结果回调监听
     */
    public static void saveLocalFile(Context context, String path, String mimeType,
                                     OnCallbackListener<String> listener) {
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<String>() {

            @Override
            public String doInBackground() throws Throwable {
                Uri uri;
                ContentValues contentValues = new ContentValues();
                String time = ValueOf.toString(System.currentTimeMillis());
                if (PictureMimeType.isHasVideo(mimeType)) {
                    contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, DateUtils.getCreateFileName("VID_"));
                    contentValues.put(MediaStore.Video.Media.MIME_TYPE, TextUtils.isEmpty(mimeType) || mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE) ? PictureMimeType.MIME_TYPE_VIDEO : mimeType);
                    if (SdkVersionUtils.isQ()) {
                        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, time);
                        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
                    } else {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                        contentValues.put(MediaStore.MediaColumns.DATA, dir.getAbsolutePath() + File.separator
                                + DateUtils.getCreateFileName("VID_") + PictureMimeType.MP4);
                    }
                    uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, DateUtils.getCreateFileName("IMG_"));
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, TextUtils.isEmpty(mimeType) || mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO) ? PictureMimeType.MIME_TYPE_IMAGE : mimeType);
                    if (SdkVersionUtils.isQ()) {
                        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, time);
                        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, PictureMimeType.DCIM);
                    } else {
                        if (PictureMimeType.isHasGif(mimeType) || PictureMimeType.isUrlHasGif(path)) {
                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            contentValues.put(MediaStore.MediaColumns.DATA, dir.getAbsolutePath() + File.separator
                                    + DateUtils.getCreateFileName("IMG_") + PictureMimeType.GIF);
                        }
                    }
                    uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                }
                if (uri != null) {
                    InputStream inputStream;
                    if (PictureMimeType.isHasHttp(path)) {
                        inputStream = new URL(path).openStream();
                    } else {
                        if (PictureMimeType.isContent(path)) {
                            inputStream = PictureContentResolver.getContentResolverOpenInputStream(context, Uri.parse(path));
                        } else {
                            inputStream = new FileInputStream(path);
                        }
                    }
                    OutputStream outputStream = PictureContentResolver.getContentResolverOpenOutputStream(context, uri);
                    boolean bufferCopy = PictureFileUtils.writeFileFromIS(inputStream, outputStream);
                    if (bufferCopy) {
                        return PictureFileUtils.getPath(context, uri);
                    }
                }
                return null;
            }

            @Override
            public void onSuccess(String result) {
                PictureThreadUtils.cancel(this);
                if (listener != null) {
                    listener.onCall(result);
                }
            }
        });

    }
}

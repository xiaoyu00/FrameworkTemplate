package com.luck.picture.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.exifinterface.media.ExifInterface;

import com.luck.picture.app.PictureAppMaster;
import com.luck.picture.basic.PictureContentResolver;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.entity.MediaExtraInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * @author：luck
 * @date：2019-10-21 17:10
 * @describe：资源处理工具类
 */
public class MediaUtils {
    /**
     * get uri
     *
     * @param id
     * @return
     */
    public static String getRealPathUri(long id, String mimeType) {
        Uri contentUri;
        if (PictureMimeType.isHasImage(mimeType)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (PictureMimeType.isHasVideo(mimeType)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (PictureMimeType.isHasAudio(mimeType)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
            contentUri = MediaStore.Files.getContentUri("external");
        }
        return ContentUris.withAppendedId(contentUri, id).toString();
    }

    /**
     * 获取mimeType
     *
     * @param url
     * @return
     */
    public static String getMimeTypeFromMediaUrl(String url) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase());
        return TextUtils.isEmpty(mimeType) ? PictureMimeType.MIME_TYPE_JPEG : mimeType;
    }

    /**
     * 是否是长图
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @return
     */
    public static boolean isLongImage(int width, int height) {
        if (width <= 0 || height <= 0) {
            return false;
        }
        return height > width * 3;
    }

    /**
     * 生成BucketId
     *
     * @param context          上下文
     * @param cameraFile       拍照资源文件
     * @param outPutCameraDir 自定义拍照输出目录
     * @return
     */
    public static long generateCameraBucketId(Context context, File cameraFile, String outPutCameraDir) {
        long bucketId;
        if (TextUtils.isEmpty(outPutCameraDir)) {
            bucketId = getCameraFirstBucketId(context);
        } else {
            if (cameraFile.getParentFile() != null) {
                bucketId = cameraFile.getParentFile().getName().hashCode();
            } else {
                bucketId = getCameraFirstBucketId(context);
            }
        }
        return bucketId;
    }


    /**
     * 生成BucketId
     *
     * @param context        上下文
     * @param cameraFile     拍照资源文件
     * @param outPutAudioDir 自定义拍照输出目录
     * @return
     */
    public static long generateSoundsBucketId(Context context, File cameraFile, String outPutAudioDir) {
        long bucketId;
        if (TextUtils.isEmpty(outPutAudioDir)) {
            bucketId = getSoundsFirstBucketId(context);
        } else {
            if (cameraFile.getParentFile() != null) {
                bucketId = cameraFile.getParentFile().getName().hashCode();
            } else {
                bucketId = getSoundsFirstBucketId(context);
            }
        }
        return bucketId;
    }

    /**
     * 创建目录名
     *
     * @param absolutePath 资源路径
     * @return
     */
    public static String generateCameraFolderName(String absolutePath) {
        String folderName;
        File cameraFile = new File(absolutePath);
        if (cameraFile.getParentFile() != null) {
            folderName = cameraFile.getParentFile().getName();
        } else {
            folderName = PictureMimeType.CAMERA;
        }
        return folderName;
    }

    /**
     * get Local image width or height
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getImageSize(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        ExifInterface exifInterface;
        InputStream inputStream;
        try {
            if (PictureMimeType.isContent(url)) {
                inputStream = PictureContentResolver.getContentResolverOpenInputStream(context, Uri.parse(url));
                exifInterface = new ExifInterface(inputStream);
            } else {
                exifInterface = new ExifInterface(url);
            }
            mediaExtraInfo.setWidth(exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL));
            mediaExtraInfo.setHeight(exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaExtraInfo;
    }

    /**
     * get Local image width or height
     *
     * @param url
     * @return
     */
    public static MediaExtraInfo getImageSize(String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        InputStream inputStream;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            if (PictureMimeType.isContent(url)) {
                inputStream = PictureContentResolver.getContentResolverOpenInputStream(PictureAppMaster.getInstance().getAppContext(), Uri.parse(url));
            } else {
                inputStream = new FileInputStream(url);
            }
            BitmapFactory.decodeStream(inputStream, null, options);
            mediaExtraInfo.setWidth(options.outWidth);
            mediaExtraInfo.setHeight(options.outHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaExtraInfo;
    }

    /**
     * get Local video width or height
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getVideoSize(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (PictureMimeType.isContent(url)) {
                retriever.setDataSource(context, Uri.parse(url));
            } else {
                retriever.setDataSource(url);
            }
            String orientation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            int width, height;
            if (TextUtils.equals("90", orientation) || TextUtils.equals("270", orientation)) {
                height = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                width = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } else {
                width = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                height = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            }
            mediaExtraInfo.setWidth(width);
            mediaExtraInfo.setHeight(height);
            mediaExtraInfo.setDuration(ValueOf.toLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return mediaExtraInfo;
    }

    /**
     * get Local video width or height
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getAudioSize(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (PictureMimeType.isContent(url)) {
                retriever.setDataSource(context, Uri.parse(url));
            } else {
                retriever.setDataSource(url);
            }
            mediaExtraInfo.setDuration(ValueOf.toLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return mediaExtraInfo;
    }

    /**
     * 删除部分手机 拍照在DCIM也生成一张的问题
     *
     * @param id
     */
    public static void removeMedia(Context context, int id) {
        try {
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Images.Media._ID + "=?";
            cr.delete(uri, selection, new String[]{Long.toString(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取DCIM文件下最新一条拍照记录
     *
     * @return
     */
    public static int getDCIMLastImageId(Context context) {
        Cursor data = null;
        try {
            //selection: 指定查询条件
            String absolutePath = PictureFileUtils.getDCIMCameraPath();
            String selection = MediaStore.Images.Media.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {absolutePath + "%"};
            if (SdkVersionUtils.isR()) {
                Bundle queryArgs = MediaUtils.createQueryArgsBundle(selection, selectionArgs, 1, 0);
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, queryArgs, null);
            } else {
                String orderBy = MediaStore.Files.FileColumns._ID + " DESC limit 1 offset 0";
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, orderBy);
            }
            if (data != null && data.getCount() > 0 && data.moveToFirst()) {
                int id = data.getInt(data.getColumnIndex(MediaStore.Images.Media._ID));
                long date = data.getLong(data.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                int duration = DateUtils.dateDiffer(date);
                // DCIM文件下最近时间1s以内的图片，可以判定是最新生成的重复照片
                return duration <= 1 ? id : -1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    /**
     * 获取Camera文件下最新一条拍照记录
     *
     * @return
     */
    public static long getCameraFirstBucketId(Context context) {
        Cursor data = null;
        try {
            String absolutePath = PictureFileUtils.getDCIMCameraPath();
            //selection: 指定查询条件
            String selection = MediaStore.Files.FileColumns.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {absolutePath + "%"};
            if (SdkVersionUtils.isR()) {
                Bundle queryArgs = MediaUtils.createQueryArgsBundle(selection, selectionArgs, 1, 0);
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, queryArgs, null);
            } else {
                String orderBy = MediaStore.Files.FileColumns._ID + " DESC limit 1 offset 0";
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection, selectionArgs, orderBy);
            }
            if (data != null && data.getCount() > 0 && data.moveToFirst()) {
                return data.getLong(data.getColumnIndex("bucket_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return -1;
    }

    /**
     * 获取Sounds文件下最新一条录音记录
     *
     * @return
     */
    public static long getSoundsFirstBucketId(Context context) {
        Cursor data = null;
        try {
            String absolutePath = PictureFileUtils.getSoundsPath();
            //selection: 指定查询条件
            String selection = MediaStore.Files.FileColumns.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {absolutePath + "%"};
            if (SdkVersionUtils.isR()) {
                Bundle queryArgs = MediaUtils.createQueryArgsBundle(selection, selectionArgs, 1, 0);
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, queryArgs, null);
            } else {
                String orderBy = MediaStore.Files.FileColumns._ID + " DESC limit 1 offset 0";
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection, selectionArgs, orderBy);
            }
            if (data != null && data.getCount() > 0 && data.moveToFirst()) {
                return data.getLong(data.getColumnIndex("bucket_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return -1;
    }

    /**
     * Key for an SQL style {@code LIMIT} string that may be present in the
     * query Bundle argument passed to
     * {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}.
     *
     * <p><b>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher are strongly
     * encourage to use structured query arguments in lieu of opaque SQL query clauses.</b>
     *
     * @see #QUERY_ARG_LIMIT
     * @see #QUERY_ARG_OFFSET
     */
    public static final String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";

    /**
     * R  createQueryArgsBundle
     *
     * @param selection
     * @param selectionArgs
     * @param limitCount
     * @param offset
     * @return
     */
    public static Bundle createQueryArgsBundle(String selection, String[] selectionArgs, int limitCount, int offset) {
        Bundle queryArgs = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, MediaStore.Files.FileColumns._ID + " DESC");
            if (SdkVersionUtils.isR()) {
                queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, limitCount + " offset " + offset);
            }
        }
        return queryArgs;
    }

    /**
     * delete camera PATH
     *
     * @param context Context
     * @param path    path
     */
    public static void deleteUri(Context context, String path) {
        try {
            if (PictureMimeType.isContent(path)) {
                context.getContentResolver().delete(Uri.parse(path), null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

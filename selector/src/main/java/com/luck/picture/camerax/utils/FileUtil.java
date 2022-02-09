package com.luck.picture.camerax.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * author hbzhou
 * date 2019/12/13 10:49
 */
public class FileUtil {
    private static final String TAG = "CJT";
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static String DST_FOLDER_NAME = "JCamera";
    public static final String POSTFIX = ".jpeg";
    public static final String POST_VIDEO = ".mp4";

    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static String CAMERA = "Camera";
    public final static String DCIM_CAMERA = "DCIM/Camera";
    public final static String MIME_TYPE_PREFIX_VIDEO = "video";
    public final static String MIME_TYPE_IMAGE = "image/jpeg";
    public final static String MIME_TYPE_PREFIX_IMAGE = "image";
    public final static String MIME_TYPE_VIDEO = "video/mp4";
    public final static String JPEG = ".jpeg";
    public final static String MP4 = ".mp4";

    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + File.separator + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    public static String saveBitmap(String dir, Bitmap b) {
        DST_FOLDER_NAME = dir;
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param chooseMode
     * @param format
     * @param outCameraDirectory
     * @return
     */
    public static File createCameraFile(Context context, int chooseMode, String fileName, String format, String outCameraDirectory) {
        return createMediaFile(context, chooseMode, fileName, format, outCameraDirectory);
    }

    /**
     * 创建文件
     *
     * @param context
     * @param chooseMode
     * @param fileName
     * @param format
     * @param outCameraDirectory
     * @return
     */
    private static File createMediaFile(Context context, int chooseMode, String fileName, String format, String outCameraDirectory) {
        return createOutFile(context, chooseMode, fileName, format, outCameraDirectory);
    }

    /**
     * 创建文件
     *
     * @param ctx                上下文
     * @param chooseMode         选择模式
     * @param fileName           文件名
     * @param format             文件格式
     * @param outCameraDirectory 输出目录
     * @return
     */
    private static File createOutFile(Context ctx, int chooseMode, String fileName, String format, String outCameraDirectory) {
        Context context = ctx.getApplicationContext();
        File folderDir;
        if (TextUtils.isEmpty(outCameraDirectory)) {
            // 外部没有自定义拍照存储路径使用默认
            File rootDir;
            if (TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState())) {
                rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                folderDir = new File(rootDir.getAbsolutePath() + File.separator + CAMERA + File.separator);
            } else {
                rootDir = getRootDirFile(context, chooseMode);
                folderDir = new File(rootDir.getAbsolutePath() + File.separator);
            }
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
        } else {
            // 自定义存储路径
            folderDir = new File(outCameraDirectory);
            if (!Objects.requireNonNull(folderDir.getParentFile()).exists()) {
                folderDir.getParentFile().mkdirs();
            }
        }
        if (!folderDir.exists()) {
            folderDir.mkdirs();
        }

        boolean isOutFileNameEmpty = TextUtils.isEmpty(fileName);
        if (chooseMode == TYPE_VIDEO) {
            String newFileVideoName = isOutFileNameEmpty ? getCreateFileName("VID_") + POST_VIDEO : fileName;
            return new File(folderDir, newFileVideoName);
        }
        String suffix = TextUtils.isEmpty(format) ? POSTFIX : format;
        String newFileImageName = isOutFileNameEmpty ? getCreateFileName("IMG_") + suffix : fileName;
        return new File(folderDir, newFileImageName);
    }

    /**
     * 根据时间戳创建文件名
     *
     * @param prefix 前缀名
     * @return
     */
    public static String getCreateFileName(String prefix) {
        long millis = System.currentTimeMillis();
        return prefix + sf.format(millis);
    }

    /**
     * 文件根目录
     *
     * @param context
     * @param type
     * @return
     */
    private static File getRootDirFile(Context context, int type) {
        if (type == TYPE_VIDEO) {
            return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 生成uri
     *
     * @param context
     * @param cameraFile
     * @return
     */
    public static Uri parUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".luckProvider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    /**
     * is content://
     *
     * @param url
     * @return
     */
    public static boolean isContent(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("content://");
    }

    /**
     * delete camera PATH
     *
     * @param context Context
     * @param path    path
     */
    public static void deleteFile(Context context, String path) {
        try {
            if (isContent(path)) {
                context.getContentResolver().delete(Uri.parse(path), null, null);
            } else {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建图片的ContentValues,用于保存拍照后的照片
     *
     * @param cameraFileName 资源名称
     * @param mimeType       资源类型
     * @return
     */
    public static ContentValues buildImageContentValues(String cameraFileName, String mimeType) {
        String time = String.valueOf(System.currentTimeMillis());
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        if (TextUtils.isEmpty(cameraFileName)) {
            values.put(MediaStore.Images.Media.DISPLAY_NAME, getCreateFileName("IMG_"));
        } else {
            if (cameraFileName.lastIndexOf(".") == -1) {
                values.put(MediaStore.Images.Media.DISPLAY_NAME, getCreateFileName("IMG_"));
            } else {
                String suffix = cameraFileName.substring(cameraFileName.lastIndexOf("."));
                String fileName = cameraFileName.replaceAll(suffix, "");
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            }
        }
        values.put(MediaStore.Images.Media.MIME_TYPE, TextUtils.isEmpty(mimeType) || mimeType.startsWith(MIME_TYPE_PREFIX_VIDEO) ? MIME_TYPE_IMAGE : mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, time);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, DCIM_CAMERA);
        }
        return values;
    }
}

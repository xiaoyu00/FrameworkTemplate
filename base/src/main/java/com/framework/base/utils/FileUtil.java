package com.framework.base.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.framework.base.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
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

    public static final String DOCUMENTS_DIR = "documents";

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
     * 获取真实路径
     *
     * @param context
     */
    public static String getFileFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        switch (uri.getScheme()) {
            case ContentResolver.SCHEME_CONTENT:
                //Android7.0之后的uri content:// URI
                return getFilePathFromContentUri(context, uri);
            case ContentResolver.SCHEME_FILE:
            default:
                //Android7.0之前的uri file://
                return new File(uri.getPath()).getAbsolutePath();
        }
    }

    /**
     * 从uri获取path
     *
     * @param uri content://media/external/file/109009
     *            <p>
     *            FileProvider适配
     *            content://com.tencent.mobileqq.fileprovider/external_files/storage/emulated/0/Tencent/QQfile_recv/
     *            content://com.tencent.mm.external.fileprovider/external/tencent/MicroMsg/Download/
     */
    private static String getFilePathFromContentUri(Context context, Uri uri) {
        if (null == uri) return null;
        String path = null;

        String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (index > -1) {
                    path = cursor.getString(index);
                } else {
                    int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                    String fileName = cursor.getString(nameIndex);
                    path = getPathFromInputStreamUri(context, uri, fileName);
                }
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 用流拷贝文件一份到自己APP私有目录下
     *
     * @param context
     * @param uri
     * @param fileName
     */
    private static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTemporalFileFrom(context, inputStream, fileName);
                filePath = file.getPath();

            } catch (Exception e) {
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream, String fileName)
            throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            //自己定义拷贝文件路径
            targetFile = new File(context.getExternalCacheDir(), fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    //普通安装
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".fileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }
    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];

            int actualBytes;
            while ((actualBytes = is.read(buf)) != -1) {
                bos.write(buf, 0, actualBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    // 修复 android.webkit.MimeTypeMap 的 getFileExtensionFromUrl 方法不支持中文的问题
    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            // 去掉正则表达式判断以添加中文支持
//          if (!filename.isEmpty() && Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename))
            if (!filename.isEmpty()) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    // 后缀转为小写
                    return filename.substring(dotPos + 1).toLowerCase();
                }
            }
        }

        return "";
    }
    private static String getPathByCopyFile(Context context, Uri uri) {
        String fileName = getFileName(context, uri);
        File cacheDir = getDocumentCacheDir(context);
        File file = generateFileName(fileName, cacheDir);
        String destinationPath = null;
        if (file != null) {
            destinationPath = file.getAbsolutePath();
            saveFileFromUri(context, uri, destinationPath);
        }

        return destinationPath;
    }
    public static String getPathFromUri(Context context,Uri uri) {
        String path = "";
        try {
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) {
                path = getPathByCopyFile(context, uri);
            } else {
                path = getRealFilePath(context,uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path == null) {
            path = "";
        }
        return path;
    }
    public static String getRealFilePath(Context context,Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**
     * 专为Android4.4以上设计的从Uri获取文件路径
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return getPathByCopyFile(context, uri);
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    final String path = id.replaceFirst("raw:", "");
                    return path;
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null && Build.VERSION.SDK_INT < 29) {
                            return path;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 在某些android8+的手机上，无法获取路径，所以用拷贝的方式，获取新文件名，然后把文件发出去
                return getPathByCopyFile(context, uri);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                String path = getDataColumn(context, contentUri, selection, selectionArgs);
                if (TextUtils.isEmpty(path) || Build.VERSION.SDK_INT >= 29) {
                    path = getPathByCopyFile(context, uri);
                }
                return path;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String path = getDataColumn(context, uri, null, null);
            if (TextUtils.isEmpty(path) || Build.VERSION.SDK_INT >= 29) {
                // 在某些华为android9+的手机上，无法获取路径，所以用拷贝的方式，获取新文件名，然后把文件发出去
                path = getPathByCopyFile(context, uri);
            }
            return path;
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return file;
    }
    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            filename = getName(uri.toString());
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }
    private static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }
    public static void openFile(Context context,String path,String authority, String fileName) {

        Uri uri = FileProvider.getUriForFile(context,authority,new File(path));
        if (uri == null) {
            Log.e("FileUtil", "openFile failed , uri is null");
            return;
        }
        String fileExtension;
        if (TextUtils.isEmpty(fileName)) {
            fileExtension = FileUtil.getFileExtensionFromUrl(path);
        } else {
            fileExtension = FileUtil.getFileExtensionFromUrl(fileName);
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, mimeType);
        try {
            Intent chooserIntent = Intent.createChooser(intent, context.getString(R.string.open_file_tips));
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);
        } catch (Exception e) {
            Log.e("FileUtil", "openFile failed , " + e.getMessage());
        }
    }
}

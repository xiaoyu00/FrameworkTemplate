package com.framework.mediaselect;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.luck.picture.app.PictureAppMaster;
import com.luck.picture.basic.PictureSelectionModel;
import com.luck.picture.basic.PictureSelector;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.config.SelectMimeType;
import com.luck.picture.engine.CompressEngine;
import com.luck.picture.engine.CropEngine;
import com.luck.picture.entity.LocalMedia;
import com.luck.picture.entity.MediaExtraInfo;
import com.luck.picture.interfaces.OnResultCallbackListener;
import com.luck.picture.style.PictureSelectorStyle;
import com.luck.picture.utils.DateUtils;
import com.luck.picture.utils.MediaUtils;
import com.luck.picture.utils.SdkVersionUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 *  多媒体拍摄选择压缩（图片视频）
 */
public class AlbumManager {

    public static void showSelectDialog(Context context, AlbumOperation operation, PathBackCall callback) {

        AlertDialog mySelectDialog = new AlertDialog.Builder(context).create();

        mySelectDialog.setView(LayoutInflater.from(context).inflate(
                R.layout.dialog_select, null));
        mySelectDialog.getWindow().setBackgroundDrawableResource(R.color.ps_color_transparent);
        mySelectDialog.show();
        mySelectDialog.getWindow().setContentView(R.layout.dialog_select);
        mySelectDialog.getWindow().setGravity(Gravity.BOTTOM);
        mySelectDialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tv_one = (TextView) mySelectDialog.getWindow().findViewById(
                R.id.tv_one);
        TextView tv_three = (TextView) mySelectDialog.getWindow().findViewById(
                R.id.tv_three);
        TextView tv_cancel = (TextView) mySelectDialog.getWindow()
                .findViewById(R.id.tv_cancel);
        tv_three.setVisibility(View.VISIBLE);
        tv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(context, operation, callback);
            }
        });
        tv_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(context, operation, callback);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mySelectDialog.isShowing()) {
                    mySelectDialog.dismiss();
                }
            }
        });

    }

    public static void openGallery(Context context, AlbumOperation operation, PathBackCall callback) {
        PictureSelector.create(context)
                .openGallery(operation.selectMimeType)
                .setSelectorUIStyle(new PictureSelectorStyle())
                .setSelectMaxFileSize(operation.selectMaxFileSize)
                .setMaxSelectNum(operation.maxSelectNum)
                .isDisplayCamera(false)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {

                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        List<String> paths = new ArrayList<>();
                        result.forEach((r) -> {
                            paths.add(r.getRealPath());
                        });
                        Log.e("ssss", "路径:" + paths.toString());
                        logFiles(result);
                        if (callback != null) {
                            callback.onResult(paths);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    public static void openCamera(Context context, AlbumOperation operation, PathBackCall callback) {

        PictureSelectionModel pictureSelector = PictureSelector.create(context)
                .openCamera(SelectMimeType.ofAll())
                .setImageEngine(GlideEngine.createGlideEngine());

        if (operation.isCrop) {
            pictureSelector.setCropEngine(getCropEngine(context));
        }
        if (operation.isCompress) {
            pictureSelector.setCompressEngine(getCompressEngine(operation));
        }

        pictureSelector.forResult(new OnResultCallbackListener<LocalMedia>() {

            @Override
            public void onResult(ArrayList<LocalMedia> result) {
                if (callback != null) {
                    List<String> paths = resolvePaths(operation, result);
                    callback.onResult(paths);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private static List<String> resolvePaths(AlbumOperation operation, ArrayList<LocalMedia> result) {
        List<String> paths = new ArrayList<>();
        result.forEach((r) -> {
            if (operation.isCompress && PictureMimeType.isHasImage(r.getMimeType())) {
                paths.add(r.getCompressPath());
            } else if (operation.isCrop && PictureMimeType.isHasImage(r.getMimeType())) {
                paths.add(r.getCutPath());
            } else {
                paths.add(r.getRealPath());
            }
        });
        Log.e("ssss", "路径:" + paths.toString());
        logFiles(result);
        return paths;
    }

    private static String getSandboxPath(Context context) {
        File externalFilesDir = context.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    private static void logFiles(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i("TAG", "文件名: " + media.getFileName());
            Log.i("TAG", "是否压缩:" + media.isCompressed());
            Log.i("TAG", "压缩:" + media.getCompressPath());
            Log.i("TAG", "原图:" + media.getPath());
            Log.i("TAG", "绝对路径:" + media.getRealPath());
            Log.i("TAG", "是否裁剪:" + media.isCut());
            Log.i("TAG", "裁剪:" + media.getCutPath());
            Log.i("TAG", "是否开启原图:" + media.isOriginal());
            Log.i("TAG", "原图路径:" + media.getOriginalPath());
            Log.i("TAG", "沙盒路径:" + media.getSandboxPath());
            Log.i("TAG", "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i("TAG", "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i("TAG", "文件大小: " + media.getSize());
        }
    }

    private static CropEngine getCropEngine(Context context) {
        return (fragment, currentLocalMedia, dataSource, requestCode) -> {
            String currentCropPath = currentLocalMedia.getAvailablePath();
            Uri inputUri;
            if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(currentCropPath)) {
                inputUri = Uri.parse(currentCropPath);
            } else {
                inputUri = Uri.fromFile(new File(currentCropPath));
            }
            String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
            Uri destinationUri = Uri.fromFile(new File(getSandboxPath(context), fileName));
            ArrayList<String> dataCropSource = new ArrayList<>();
            for (int i = 0; i < dataSource.size(); i++) {
                LocalMedia media = dataSource.get(i);
                dataCropSource.add(media.getAvailablePath());
            }
            UCrop uCrop = UCrop.of(inputUri, destinationUri, dataCropSource);
            uCrop.setImageEngine((context1, url, imageView) -> Glide.with(context1).load(url).into(imageView));
            uCrop.withOptions(new UCrop.Options());
            uCrop.start(fragment.getActivity(), fragment, requestCode);
        };
    }

    private static CompressEngine getCompressEngine(AlbumOperation operation) {
        return (context, list, listener) -> {
            List<Uri> compress = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                LocalMedia media = list.get(i);
                String availablePath = media.getAvailablePath();
                Uri uri = PictureMimeType.isContent(availablePath) || PictureMimeType.isHasHttp(availablePath)
                        ? Uri.parse(availablePath)
                        : Uri.fromFile(new File(availablePath));
                compress.add(uri);
            }
            if (compress.size() == 0) {
                listener.onCall(list);
                return;
            }

            // 2、调用Luban压缩
            Luban.with(context)
                    .load(compress)
                    .ignoreBy(operation.compressSize)
                    .filter(path -> PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path))
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            int indexOf = filePath.lastIndexOf(".");
                            String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                            return DateUtils.getCreateFileName("CMP_") + postfix;
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(int index, File compressFile) {
                            LocalMedia media = list.get(index);
                            if (compressFile.exists() && !TextUtils.isEmpty(compressFile.getAbsolutePath())) {
                                media.setCompressed(true);
                                media.setCompressPath(compressFile.getAbsolutePath());
                                media.setSandboxPath(SdkVersionUtils.isQ() ? media.getCompressPath() : null);
                            }
                            if (index == list.size() - 1) {
                                listener.onCall(list);
                            }
                        }

                        @Override
                        public void onError(int index, Throwable e) {
                            // 压缩失败
                            if (index != -1) {
                                LocalMedia media = list.get(index);
                                media.setCompressed(false);
                                media.setCompressPath(null);
                                media.setSandboxPath(null);
                                if (index == list.size() - 1) {
                                    listener.onCall(list);
                                }
                            }
                        }
                    }).launch();
        };
    }
}

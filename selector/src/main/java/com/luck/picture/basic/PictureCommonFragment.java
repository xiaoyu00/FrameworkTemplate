package com.luck.picture.basic;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luck.picture.PictureOnlyCameraFragment;
import com.luck.picture.PictureSelectorPreviewFragment;
import com.luck.picture.app.PictureAppMaster;
import com.luck.picture.camerax.TakeCameraActivity;
import com.luck.picture.config.Crop;
import com.luck.picture.config.CustomIntentKey;
import com.luck.picture.config.PictureConfig;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.config.SelectLimitType;
import com.luck.picture.config.SelectMimeType;
import com.luck.picture.config.SelectModeConfig;
import com.luck.picture.dialog.PhotoItemSelectedDialog;
import com.luck.picture.dialog.PictureLoadingDialog;
import com.luck.picture.dialog.RemindDialog;
import com.luck.picture.engine.PictureSelectorEngine;
import com.luck.picture.entity.LocalMedia;
import com.luck.picture.entity.MediaExtraInfo;
import com.luck.picture.immersive.ImmersiveManager;
import com.luck.picture.interfaces.OnCallbackIndexListener;
import com.luck.picture.interfaces.OnCallbackListener;
import com.luck.picture.interfaces.OnItemClickListener;
import com.luck.picture.language.LanguageConfig;
import com.luck.picture.language.PictureLanguageUtils;
import com.luck.picture.lib.R;
import com.luck.picture.loader.IBridgeMediaLoader;
import com.luck.picture.manager.SelectedManager;
import com.luck.picture.permissions.PermissionChecker;
import com.luck.picture.permissions.PermissionConfig;
import com.luck.picture.permissions.PermissionResultCallback;
import com.luck.picture.permissions.PermissionUtil;
import com.luck.picture.service.ForegroundService;
import com.luck.picture.style.PictureWindowAnimationStyle;
import com.luck.picture.style.SelectMainStyle;
import com.luck.picture.thread.PictureThreadUtils;
import com.luck.picture.utils.ActivityCompatHelper;
import com.luck.picture.utils.BitmapUtils;
import com.luck.picture.utils.MediaStoreUtils;
import com.luck.picture.utils.MediaUtils;
import com.luck.picture.utils.PictureFileUtils;
import com.luck.picture.utils.SdkVersionUtils;
import com.luck.picture.utils.ToastUtils;
import com.luck.picture.utils.ValueOf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：luck
 * @date：2021/11/19 10:02 下午
 * @describe：PictureCommonFragment
 */
public abstract class PictureCommonFragment extends Fragment implements IPictureSelectorCommonEvent {

    /**
     * PermissionResultCallback
     */
    private PermissionResultCallback mPermissionResultCallback;

    /**
     * page
     */
    protected int mPage = 1;

    /**
     * Media Loader engine
     */
    protected IBridgeMediaLoader mLoader;

    /**
     * IBridgePictureBehavior
     */
    protected IBridgePictureBehavior iBridgePictureBehavior;

    /**
     * PictureSelector Config
     */
    protected PictureSelectionConfig config;

    /**
     * Loading Dialog
     */
    private PictureLoadingDialog mLoadingDialog;

    /**
     * click sound
     */
    private SoundPool soundPool;

    /**
     * click sound effect id
     */
    private int soundID;


    @Override
    public int getResourceId() {
        return 0;
    }

    @Override
    public void reStartSavedInstance(Bundle savedInstanceState) {

    }

    @Override
    public void onCheckOriginalChange() {

    }

    @Override
    public void dispatchCameraMediaResult(LocalMedia media) {

    }


    @Override
    public void onSelectedChange(boolean isAddRemove, LocalMedia currentMedia) {

    }

    @Override
    public void onFixedSelectedChange(LocalMedia oldLocalMedia) {

    }

    @Override
    public void sendChangeSubSelectPositionEvent(boolean adapterChange) {

    }

    @Override
    public void handlePermissionSettingResult() {

    }

    @Override
    public void onEditMedia(Intent intent) {

    }

    @Override
    public void onEnterFragmentAnimComplete() {

    }

    @Override
    public void onEnterFragment() {

    }

    @Override
    public void onExitFragment() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionResultCallback != null) {
            PermissionChecker.getInstance().onRequestPermissionsResult(grantResults, mPermissionResultCallback);
            mPermissionResultCallback = null;
        }
    }

    /**
     * Set PermissionResultCallback
     *
     * @param callback
     */
    public void setPermissionsResultAction(PermissionResultCallback callback) {
        mPermissionResultCallback = callback;
    }

    @Override
    public void handlePermissionDenied(String[] permissionArray) {
        boolean isReadWrite = permissionArray == PermissionConfig.READ_WRITE_EXTERNAL_STORAGE
                || permissionArray == PermissionConfig.WRITE_EXTERNAL_STORAGE;
        PermissionUtil.goIntentSetting(this, isReadWrite, PictureConfig.REQUEST_GO_SETTING);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getResourceId() != 0) {
            return inflater.inflate(getResourceId(), container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingDialog = new PictureLoadingDialog(getContext());
        if (savedInstanceState != null) {
            config = savedInstanceState.getParcelable(PictureConfig.EXTRA_PICTURE_SELECTOR_CONFIG);
        }
        if (config == null) {
            config = PictureSelectionConfig.getInstance();
        }
        if (config.isPreviewFullScreenMode) {
            SelectMainStyle selectMainStyle = PictureSelectionConfig.selectorStyle.getSelectMainStyle();
            ImmersiveManager.translucentStatusBar(getActivity(), selectMainStyle.isDarkStatusBarBlack());
        }
        if (config.isOpenClickSound && !config.isOnlyCamera) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            soundID = soundPool.load(getContext(), R.raw.ps_click_music, 1);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initAppLanguage();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (config != null) {
            outState.putParcelable(PictureConfig.EXTRA_PICTURE_SELECTOR_CONFIG, config);
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
        Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(),
                enter ? windowAnimationStyle.activityEnterAnimation : windowAnimationStyle.activityExitAnimation);
        if (enter) {
            onEnterFragment();
        } else {
            onExitFragment();
        }
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onEnterFragmentAnimComplete();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return loadAnimation;
    }


    @Override
    public int confirmSelect(LocalMedia currentMedia, boolean isSelected) {
        if (checkSelectLimit(currentMedia)) {
            return SelectedManager.INVALID;
        }
        String curMimeType = currentMedia.getMimeType();
        long curDuration = currentMedia.getDuration();
        List<LocalMedia> selectedResult = SelectedManager.getSelectedResult();
        if (config.selectionMode == SelectModeConfig.MULTIPLE) {
            if (config.isWithVideoImage) {
                // 共选型模式
                int selectVideoSize = 0;
                for (int i = 0; i < selectedResult.size(); i++) {
                    String mimeType = selectedResult.get(i).getMimeType();
                    if (PictureMimeType.isHasVideo(mimeType)) {
                        selectVideoSize++;
                    }
                }
                if (checkWithMimeTypeValidity(isSelected, curMimeType, selectVideoSize, curDuration)) {
                    return SelectedManager.INVALID;
                }
            } else {
                // 单一型模式
                if (checkOnlyMimeTypeValidity(isSelected, curMimeType, SelectedManager.getTopResultMimeType(), curDuration)) {
                    return SelectedManager.INVALID;
                }
            }
        }
        int resultCode;
        if (isSelected) {
            selectedResult.remove(currentMedia);
            resultCode = SelectedManager.REMOVE;
        } else {
            if (config.selectionMode == SelectModeConfig.SINGLE) {
                if (selectedResult.size() > 0) {
                    sendFixedSelectedChangeEvent(selectedResult.get(0));
                    selectedResult.clear();
                }
            }
            selectedResult.add(currentMedia);
            currentMedia.setNum(selectedResult.size());
            resultCode = SelectedManager.ADD_SUCCESS;
            playClickEffect();
        }
        sendSelectedChangeEvent(resultCode == SelectedManager.ADD_SUCCESS, currentMedia);
        return resultCode;
    }

    /**
     * 验证选择先决条件
     *
     * @param currentMedia
     */
    private boolean checkSelectLimit(LocalMedia currentMedia) {
        if (PictureMimeType.isHasVideo(currentMedia.getMimeType()) || PictureMimeType.isHasAudio(currentMedia.getMimeType())) {
            if (config.selectMaxDurationSecond > 0) {
                if (currentMedia.getDuration() > config.selectMaxDurationSecond) {
                    if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                        boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                                .onSelectLimitTips(getContext(), config,
                                        SelectLimitType.SELECT_MAX_SECOND_SELECT_LIMIT);
                        if (isSelectLimit) {
                            return true;
                        }
                    }
                    int second = config.selectMaxDurationSecond / 1000;
                    if (PictureMimeType.isHasVideo(currentMedia.getMimeType())) {
                        RemindDialog.showTipsDialog(getContext(),
                                getString(R.string.ps_select_video_max_second, second));
                    } else {
                        RemindDialog.showTipsDialog(getContext(),
                                getString(R.string.ps_select_audio_max_second, second));
                    }
                    return true;
                }
            }
            if (config.selectMinDurationSecond > 0) {
                int second = config.selectMinDurationSecond / 1000;
                if (currentMedia.getDuration() < config.selectMinDurationSecond) {
                    if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                        boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                                .onSelectLimitTips(getContext(), config,
                                        SelectLimitType.SELECT_MIN_SECOND_SELECT_LIMIT);
                        if (isSelectLimit) {
                            return true;
                        }
                    }

                    if (PictureMimeType.isHasVideo(currentMedia.getMimeType())) {
                        RemindDialog.showTipsDialog(getContext(),
                                getString(R.string.ps_select_video_min_second, second));
                    } else {
                        RemindDialog.showTipsDialog(getContext(),
                                getString(R.string.ps_select_audio_min_second, second));
                    }
                    return true;
                }
            }
        }
        if (config.selectMaxFileSize > 0) {
            if (currentMedia.getSize() > config.selectMaxFileSize) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config,
                                    SelectLimitType.SELECT_MAX_FILE_SIZE_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                String fileSize = PictureFileUtils.formatFileSize(config.selectMaxFileSize, 1);
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_select_max_size, fileSize));
                return true;
            }
        }
        if (config.selectMinFileSize > 0) {
            if (currentMedia.getSize() < config.selectMinFileSize) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config,
                                    SelectLimitType.SELECT_MIN_FILE_SIZE_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                String fileSize = PictureFileUtils.formatFileSize(config.selectMinFileSize, 1);
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_select_min_size, fileSize));
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    @Override
    public boolean checkWithMimeTypeValidity(boolean isSelected, String curMimeType, int selectVideoSize, long duration) {
        if (PictureMimeType.isHasVideo(curMimeType)) {
            if (config.maxVideoSelectNum <= 0) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_NOT_WITH_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                // 如果视频可选数量是0
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_rule));
                return true;
            }

            if (!isSelected && SelectedManager.getSelectedResult().size() >= config.maxSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_message_max_num, config.maxSelectNum));
                return true;
            }

            if (!isSelected && selectVideoSize >= config.maxVideoSelectNum) {
                // 如果选择的是视频
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_VIDEO_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getTipsMsg(getContext(), curMimeType, config.maxVideoSelectNum));
                return true;
            }

            if (!isSelected && config.filterVideoMinSecond > 0 && duration < config.filterVideoMinSecond) {
                // 视频小于最低指定的长度
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config,
                                    SelectLimitType.SELECT_MIN_VIDEO_SECOND_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_min_seconds, config.filterVideoMinSecond / 1000));
                return true;
            }

            if (!isSelected && config.filterVideoMaxSecond > 0 && duration > config.filterVideoMaxSecond) {
                // 视频时长超过了指定的长度
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config,
                                    SelectLimitType.SELECT_MAX_VIDEO_SECOND_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_max_seconds, config.filterVideoMaxSecond / 1000));
                return true;
            }
        } else {
            if (!isSelected && SelectedManager.getSelectedResult().size() >= config.maxSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config,
                                    SelectLimitType.SELECT_MAX_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_message_max_num, config.maxSelectNum));
                return true;
            }
        }
        return false;
    }


    @SuppressLint("StringFormatInvalid")
    @Override
    public boolean checkOnlyMimeTypeValidity(boolean isSelected, String curMimeType, String existMimeType, long duration) {
        boolean isSameMimeType = PictureMimeType.isMimeTypeSame(existMimeType, curMimeType);
        if (!isSameMimeType) {
            if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                        .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_NOT_WITH_SELECT_LIMIT);
                if (isSelectLimit) {
                    return true;
                }
            }
            RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_rule));
            return true;
        }
        if (PictureMimeType.isHasVideo(existMimeType) && config.maxVideoSelectNum > 0) {
            if (!isSelected && SelectedManager.getSelectedResult().size() >= config.maxVideoSelectNum) {
                // 如果先选择的是视频
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_VIDEO_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getTipsMsg(getContext(), existMimeType, config.maxVideoSelectNum));
                return true;
            }
            if (!isSelected && config.filterVideoMinSecond > 0 && duration < config.filterVideoMinSecond) {
                // 视频小于最低指定的长度
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_VIDEO_SECOND_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_min_seconds, config.filterVideoMinSecond / 1000));
                return true;
            }

            if (!isSelected && config.filterVideoMaxSecond > 0 && duration > config.filterVideoMaxSecond) {
                // 视频时长超过了指定的长度
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_VIDEO_SECOND_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_max_seconds, config.filterVideoMaxSecond / 1000));
                return true;
            }
        } else {
            if (!isSelected && SelectedManager.getSelectedResult().size() >= config.maxSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getTipsMsg(getContext(), existMimeType, config.maxSelectNum));
                return true;
            }
            if (PictureMimeType.isHasVideo(curMimeType)) {
                if (!isSelected && config.filterVideoMinSecond > 0 && duration < config.filterVideoMinSecond) {
                    // 视频小于最低指定的长度
                    if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                        boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                                .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_VIDEO_SECOND_SELECT_LIMIT);
                        if (isSelectLimit) {
                            return true;
                        }
                    }
                    RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_min_seconds, config.filterVideoMinSecond / 1000));
                    return true;
                }
                if (!isSelected && config.filterVideoMaxSecond > 0 && duration > config.filterVideoMaxSecond) {
                    // 视频时长超过了指定的长度
                    if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                        boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                                .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MAX_VIDEO_SECOND_SELECT_LIMIT);
                        if (isSelectLimit) {
                            return true;
                        }
                    }
                    RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_choose_max_seconds, config.filterVideoMaxSecond / 1000));
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 根据类型获取相应的Toast文案
     *
     * @param context
     * @param mimeType
     * @param maxSelectNum
     * @return
     */
    @SuppressLint("StringFormatInvalid")
    private static String getTipsMsg(Context context, String mimeType, int maxSelectNum) {
        if (PictureMimeType.isHasVideo(mimeType)) {
            return context.getString(R.string.ps_message_video_max_num, String.valueOf(maxSelectNum));
        } else if (PictureMimeType.isHasAudio(mimeType)) {
            return context.getString(R.string.ps_message_audio_max_num, String.valueOf(maxSelectNum));
        } else {
            return context.getString(R.string.ps_message_max_num, String.valueOf(maxSelectNum));
        }
    }

    @Override
    public void sendSelectedChangeEvent(boolean isAddRemove, LocalMedia currentMedia) {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof PictureCommonFragment) {
                    ((PictureCommonFragment) fragment).onSelectedChange(isAddRemove, currentMedia);
                }
            }
        }
    }

    @Override
    public void sendFixedSelectedChangeEvent(LocalMedia currentMedia) {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof PictureCommonFragment) {
                    ((PictureCommonFragment) fragment).onFixedSelectedChange(currentMedia);
                }
            }
        }
    }

    @Override
    public void sendSelectedOriginalChangeEvent() {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof PictureCommonFragment) {
                    ((PictureCommonFragment) fragment).onCheckOriginalChange();
                }
            }
        }
    }

    @Override
    public void openSelectedCamera() {
        switch (config.chooseMode) {
            case SelectMimeType.TYPE_ALL:
                if (config.ofAllCameraType == SelectMimeType.ofImage()) {
                    openImageCamera();
                } else if (config.ofAllCameraType == SelectMimeType.ofVideo()) {
                    openVideoCamera();
                } else {
//                    onSelectedOnlyCamera();
                    openAllCamera();
                }
                break;
            case SelectMimeType.TYPE_IMAGE:
                openImageCamera();
                break;
            case SelectMimeType.TYPE_VIDEO:
                openVideoCamera();
                break;
            case SelectMimeType.TYPE_AUDIO:
                openSoundRecording();
                break;
            default:
                break;
        }
    }


    @Override
    public void onSelectedOnlyCamera() {
        PhotoItemSelectedDialog selectedDialog = PhotoItemSelectedDialog.newInstance();
        selectedDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case PhotoItemSelectedDialog.IMAGE_CAMERA:
                        if (PictureSelectionConfig.onCameraInterceptListener != null) {
                            interceptCameraEvent(SelectMimeType.TYPE_IMAGE);
                        } else {
                            openImageCamera();
                        }
                        break;
                    case PhotoItemSelectedDialog.VIDEO_CAMERA:
                        if (PictureSelectionConfig.onCameraInterceptListener != null) {
                            interceptCameraEvent(SelectMimeType.TYPE_VIDEO);
                        } else {
                            openVideoCamera();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        selectedDialog.show(getChildFragmentManager(), "PhotoItemSelectedDialog");
    }

    @Override
    public void openImageCamera() {
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            PictureSelectionConfig.onPermissionsEventListener.requestPermission(this, PermissionConfig.CAMERA,
                    new OnCallbackListener<Boolean>() {
                        @Override
                        public void onCall(Boolean isResult) {
                            if (isResult) {
                                startCameraImageCapture();
                            } else {
                                handlePermissionDenied(PermissionConfig.CAMERA);
                            }
                        }
                    });
        } else {
            PermissionChecker.getInstance().requestPermissions(this, PermissionConfig.CAMERA,
                    new PermissionResultCallback() {
                        @Override
                        public void onGranted() {
                            startCameraImageCapture();
                        }

                        @Override
                        public void onDenied() {
                            handlePermissionDenied(PermissionConfig.CAMERA);
                        }
                    });
        }
    }
    private void openAllCamera(){
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            PictureSelectionConfig.onPermissionsEventListener.requestPermission(this, PermissionConfig.ALL,
                    new OnCallbackListener<Boolean>() {
                        @Override
                        public void onCall(Boolean isResult) {
                            if (isResult) {
                                startCameraAllCapture();
                            } else {
                                handlePermissionDenied(PermissionConfig.ALL);
                            }
                        }
                    });
        } else {
            PermissionChecker.getInstance().requestPermissions(this, PermissionConfig.ALL,
                    new PermissionResultCallback() {
                        @Override
                        public void onGranted() {
                            startCameraAllCapture();
                        }

                        @Override
                        public void onDenied() {
                            handlePermissionDenied(PermissionConfig.ALL);
                        }
                    });
        }
    }
    private void startCameraAllCapture(){
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            if (PictureSelectionConfig.onCameraInterceptListener != null) {
                ForegroundService.startForegroundService(getContext());
                interceptCameraEvent(SelectMimeType.TYPE_ALL);
            } else {
                Intent cameraIntent = new Intent(getContext(), TakeCameraActivity.class);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    ForegroundService.startForegroundService(getContext());
                    Uri imageUri = MediaStoreUtils.createCameraOutImageUri(getContext(), config);
                    if (imageUri != null) {
                        if (config.isCameraAroundState) {
                            cameraIntent.putExtra(PictureConfig.CAMERA_FACING, PictureConfig.CAMERA_BEFORE);
                        }
                        cameraIntent.putExtra(PictureConfig.EXTRA_QUICK_CAPTURE, config.isQuickCapture);
                        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.recordVideoMaxSecond);
                        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.videoQuality);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                    }
                }
            }
        }
    }
    /**
     * Start ACTION_IMAGE_CAPTURE
     */
    private void startCameraImageCapture() {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            if (PictureSelectionConfig.onCameraInterceptListener != null) {
                ForegroundService.startForegroundService(getContext());
                interceptCameraEvent(SelectMimeType.TYPE_IMAGE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    ForegroundService.startForegroundService(getContext());
                    Uri imageUri = MediaStoreUtils.createCameraOutImageUri(getContext(), config);
                    if (imageUri != null) {
                        if (config.isCameraAroundState) {
                            cameraIntent.putExtra(PictureConfig.CAMERA_FACING, PictureConfig.CAMERA_BEFORE);
                        }
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                    }
                }
            }
        }
    }


    @Override
    public void openVideoCamera() {
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            PictureSelectionConfig.onPermissionsEventListener.requestPermission(this, PermissionConfig.CAMERA,
                    new OnCallbackListener<Boolean>() {
                        @Override
                        public void onCall(Boolean isResult) {
                            if (isResult) {
                                startCameraVideoCapture();
                            } else {
                                handlePermissionDenied(PermissionConfig.CAMERA);
                            }
                        }
                    });
        } else {
            PermissionChecker.getInstance().requestPermissions(this, PermissionConfig.CAMERA,
                    new PermissionResultCallback() {
                        @Override
                        public void onGranted() {
                            startCameraVideoCapture();
                        }

                        @Override
                        public void onDenied() {
                            handlePermissionDenied(PermissionConfig.CAMERA);
                        }
                    });
        }
    }

    /**
     * Start ACTION_VIDEO_CAPTURE
     */
    private void startCameraVideoCapture() {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            if (PictureSelectionConfig.onCameraInterceptListener != null) {
                ForegroundService.startForegroundService(getContext());
                interceptCameraEvent(SelectMimeType.TYPE_VIDEO);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    ForegroundService.startForegroundService(getContext());
                    Uri videoUri = MediaStoreUtils.createCameraOutVideoUri(getContext(), config);
                    if (videoUri != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        if (config.isCameraAroundState) {
                            cameraIntent.putExtra(PictureConfig.CAMERA_FACING, PictureConfig.CAMERA_BEFORE);
                        }
                        cameraIntent.putExtra(PictureConfig.EXTRA_QUICK_CAPTURE, config.isQuickCapture);
                        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.recordVideoMaxSecond);
                        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.videoQuality);
                        startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                    }
                }
            }
        }
    }


    @Override
    public void openSoundRecording() {
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            PictureSelectionConfig.onPermissionsEventListener.requestPermission(this, PermissionConfig.RECORD_AUDIO,
                    new OnCallbackListener<Boolean>() {
                        @Override
                        public void onCall(Boolean isResult) {
                            if (isResult) {
                                startCameraRecordSound();
                            } else {
                                handlePermissionDenied(PermissionConfig.RECORD_AUDIO);
                            }
                        }
                    });
        } else {
            PermissionChecker.getInstance().requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionResultCallback() {
                        @Override
                        public void onGranted() {
                            startCameraRecordSound();
                        }

                        @Override
                        public void onDenied() {
                            handlePermissionDenied(PermissionConfig.RECORD_AUDIO);
                        }
                    });
        }
    }

    /**
     * Start RECORD_SOUND_ACTION
     */
    private void startCameraRecordSound() {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            if (PictureSelectionConfig.onCameraInterceptListener != null) {
                ForegroundService.startForegroundService(getContext());
                interceptCameraEvent(SelectMimeType.TYPE_AUDIO);
            } else {
                Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    ForegroundService.startForegroundService(getContext());
                    startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                } else {
                    ToastUtils.showToast(getContext(), "The system is missing a recording component");
                }
            }
        }
    }


    /**
     * 拦截相机事件并处理返回结果
     */
    private void interceptCameraEvent(int cameraMode) {
        ForegroundService.startForegroundService(getContext());
        PictureSelectionConfig.onCameraInterceptListener.openCamera(this, cameraMode, PictureConfig.REQUEST_CAMERA);
    }

    /**
     * 点击选择的音效
     */
    private void playClickEffect() {
        if (soundPool != null && config.isOpenClickSound) {
            soundPool.play(soundID, 0.1F, 0.5F, 0, 1, 1);
        }
    }

    /**
     * 释放音效资源
     */
    private void releaseSoundPool() {
        try {
            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ForegroundService.stopService(getContext());
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.REQUEST_CAMERA) {
                dispatchHandleCamera(data);
            } else if (requestCode == Crop.REQUEST_EDIT_CROP) {
                onEditMedia(data);
            } else if (requestCode == Crop.REQUEST_CROP) {
                List<LocalMedia> selectedResult = SelectedManager.getSelectedResult();
                try {
                    if (selectedResult.size() == 1) {
                        LocalMedia media = selectedResult.get(0);
                        Uri output = Crop.getOutput(data);
                        media.setCutPath(output != null ? output.getPath() : "");
                        media.setCut(!TextUtils.isEmpty(media.getCutPath()));
                        media.setCropImageWidth(Crop.getOutputImageWidth(data));
                        media.setCropImageHeight(Crop.getOutputImageHeight(data));
                        media.setCropOffsetX(Crop.getOutputImageOffsetX(data));
                        media.setCropOffsetY(Crop.getOutputImageOffsetY(data));
                        media.setCropResultAspectRatio(Crop.getOutputCropAspectRatio(data));
                        media.setCustomData(Crop.getOutputCustomExtraData(data));
                        media.setSandboxPath(media.getCutPath());
                    } else {
                        String extra = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
                        JSONArray array = new JSONArray(extra);
                        if (array.length() == selectedResult.size()) {
                            for (int i = 0; i < selectedResult.size(); i++) {
                                LocalMedia media = selectedResult.get(i);
                                JSONObject item = array.optJSONObject(i);
                                media.setCutPath(item.optString(CustomIntentKey.EXTRA_OUT_PUT_PATH));
                                media.setCut(!TextUtils.isEmpty(media.getCutPath()));
                                media.setCropImageWidth(item.optInt(CustomIntentKey.EXTRA_IMAGE_WIDTH));
                                media.setCropImageHeight(item.optInt(CustomIntentKey.EXTRA_IMAGE_HEIGHT));
                                media.setCropOffsetX(item.optInt(CustomIntentKey.EXTRA_OFFSET_X));
                                media.setCropOffsetY(item.optInt(CustomIntentKey.EXTRA_OFFSET_Y));
                                media.setCropResultAspectRatio((float) item.optDouble(CustomIntentKey.EXTRA_ASPECT_RATIO));
                                media.setCustomData(item.optString(CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA));
                                media.setSandboxPath(media.getCutPath());
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(getContext(), e.getMessage());
                }

                ArrayList<LocalMedia> result = new ArrayList<>(selectedResult);
                if (checkCompressValidity()) {
                    showLoading();
                    PictureSelectionConfig.compressEngine.onStartCompress(getContext(), result,
                            new OnCallbackListener<ArrayList<LocalMedia>>() {
                                @Override
                                public void onCall(ArrayList<LocalMedia> result) {
                                    onResultEvent(result);
                                }
                            });
                } else {
                    onResultEvent(result);
                }
            }
        } else if (resultCode == Crop.RESULT_CROP_ERROR) {
            Throwable throwable = data != null ? Crop.getError(data) : new Throwable("image crop error");
            if (throwable != null) {
                ToastUtils.showToast(getContext(), throwable.getMessage());
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == PictureConfig.REQUEST_CAMERA) {
                MediaUtils.deleteUri(getContext(), config.cameraPath);
            } else if (requestCode == PictureConfig.REQUEST_GO_SETTING) {
                handlePermissionSettingResult();
            }
        }
    }

    /**
     * 相机事件回调处理
     */
    private void dispatchHandleCamera(Intent intent) {
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<LocalMedia>() {

            @Override
            public LocalMedia doInBackground() {
                String outputPath = getOutputPath(intent);
                if (!TextUtils.isEmpty(outputPath)) {
                    config.cameraPath = outputPath;
                }
                if (TextUtils.isEmpty(config.cameraPath)) {
                    return null;
                }
                if (config.chooseMode == SelectMimeType.ofAudio()) {
                    copyOutputAudioToDir();
                }
                return buildLocalMedia();
            }

            @Override
            public void onSuccess(LocalMedia result) {
                PictureThreadUtils.cancel(this);
                if (result != null) {
                    dispatchCameraMediaResult(result);
                    onScannerScanFile(result);
                }
            }
        });
    }

    /**
     * copy录音文件至指定目录
     */
    private void copyOutputAudioToDir() {
        try {
            if (!TextUtils.isEmpty(config.outPutAudioDir) && PictureMimeType.isContent(config.cameraPath)) {
                InputStream inputStream = PictureContentResolver.getContentResolverOpenInputStream(getContext(),
                        Uri.parse(config.cameraPath));
                String audioFileName;
                if (TextUtils.isEmpty(config.outPutAudioFileName)) {
                    audioFileName = "";
                } else {
                    audioFileName = config.isOnlyCamera
                            ? config.outPutAudioFileName : System.currentTimeMillis() + "_" + config.outPutAudioFileName;
                }
                File outputFile = PictureFileUtils.createCameraFile(getContext(),
                        config.chooseMode, audioFileName, "", config.outPutAudioDir);
                FileOutputStream outputStream = new FileOutputStream(outputFile.getAbsolutePath());
                boolean isCopyStatus = PictureFileUtils.writeFileFromIS(inputStream, outputStream);
                if (isCopyStatus) {
                    MediaUtils.deleteUri(getContext(), config.cameraPath);
                    config.cameraPath = outputFile.getAbsolutePath();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试匹配查找自定义相机返回的路径
     *
     * @param data
     * @return
     */
    protected String getOutputPath(Intent data) {
        String outputPath = null;
        Uri outPutUri;
        if (data != null) {
            if (config.chooseMode == SelectMimeType.ofAudio()) {
                outPutUri = data.getData() != null ? data.getData() : data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            } else {
                outPutUri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            }
            if (outPutUri != null) {
                outputPath = PictureMimeType.isContent(outPutUri.toString()) ? outPutUri.toString() : outPutUri.getPath();
            }
        }
        return outputPath;
    }

    /**
     * 刷新相册
     *
     * @param media 要刷新的对象
     */
    private void onScannerScanFile(LocalMedia media) {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return;
        }
        if (SdkVersionUtils.isQ()) {
            if (PictureMimeType.isHasVideo(media.getMimeType()) && PictureMimeType.isContent(config.cameraPath)) {
                new PictureMediaScannerConnection(getActivity(), media.getRealPath());
            }
        } else {
            new PictureMediaScannerConnection(getActivity(),
                    PictureMimeType.isContent(config.cameraPath) ? media.getRealPath() : config.cameraPath);
            if (PictureMimeType.isHasImage(media.getMimeType())) {
                int lastImageId = MediaUtils.getDCIMLastImageId(getContext());
                if (lastImageId != -1) {
                    MediaUtils.removeMedia(getContext(), lastImageId);
                }
            }
        }
    }

    /**
     * buildLocalMedia
     */
    private LocalMedia buildLocalMedia() {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return null;
        }
        long id, bucketId;
        File cameraFile;
        String mimeType;
        if (PictureMimeType.isContent(config.cameraPath)) {
            Uri cameraUri = Uri.parse(config.cameraPath);
            String path = PictureFileUtils.getPath(getActivity(), cameraUri);
            cameraFile = new File(path);
            mimeType = MediaUtils.getMimeTypeFromMediaUrl(cameraFile.getAbsolutePath());
            int lastIndexOf = config.cameraPath.lastIndexOf("/") + 1;
            id = lastIndexOf > 0 ? ValueOf.toLong(config.cameraPath.substring(lastIndexOf)) : System.currentTimeMillis();
            if (PictureMimeType.isHasAudio(mimeType)) {
                bucketId = MediaUtils.generateSoundsBucketId(getContext(), cameraFile, "");
            } else {
                bucketId = MediaUtils.generateCameraBucketId(getContext(), cameraFile, "");
            }
        } else {
            cameraFile = new File(config.cameraPath);
            mimeType = MediaUtils.getMimeTypeFromMediaUrl(cameraFile.getAbsolutePath());
            id = System.currentTimeMillis();
            if (PictureMimeType.isHasAudio(mimeType)) {
                bucketId = MediaUtils.generateSoundsBucketId(getContext(), cameraFile, config.outPutCameraDir);
            } else {
                bucketId = MediaUtils.generateCameraBucketId(getContext(), cameraFile, config.outPutCameraDir);
            }
        }
        if (config.isCameraRotateImage && PictureMimeType.isHasImage(mimeType) && !PictureMimeType.isContent(config.cameraPath)) {
            BitmapUtils.rotateImage(getContext(), config.cameraPath);
        }
        MediaExtraInfo mediaExtraInfo;
        if (PictureMimeType.isHasVideo(mimeType)) {
            mediaExtraInfo = MediaUtils.getVideoSize(getContext(), config.cameraPath);
        } else if (PictureMimeType.isHasAudio(mimeType)) {
            mediaExtraInfo = MediaUtils.getAudioSize(getContext(), config.cameraPath);
        } else {
            mediaExtraInfo = MediaUtils.getImageSize(getContext(), config.cameraPath);
        }
        String folderName = MediaUtils.generateCameraFolderName(cameraFile.getAbsolutePath());
        LocalMedia media = LocalMedia.parseLocalMedia(id, config.cameraPath, cameraFile.getAbsolutePath(),
                cameraFile.getName(), folderName, mediaExtraInfo.getDuration(), config.chooseMode,
                mimeType, mediaExtraInfo.getWidth(), mediaExtraInfo.getHeight(), cameraFile.length(), bucketId,
                cameraFile.lastModified() / 1000);
        if (SdkVersionUtils.isQ()) {
            media.setSandboxPath(PictureMimeType.isContent(config.cameraPath) ? null : config.cameraPath);
        }
        return media;
    }

    /**
     * 验证完成选择的先决条件
     *
     * @return
     */
    private boolean checkCompleteSelectLimit() {
        if (config.selectionMode != SelectModeConfig.MULTIPLE || config.isOnlyCamera) {
            return false;
        }
        if (config.isWithVideoImage) {
            // 共选型模式
            ArrayList<LocalMedia> selectedResult = SelectedManager.getSelectedResult();
            int selectImageSize = 0;
            int selectVideoSize = 0;
            for (int i = 0; i < selectedResult.size(); i++) {
                String mimeType = selectedResult.get(i).getMimeType();
                if (PictureMimeType.isHasVideo(mimeType)) {
                    selectVideoSize++;
                } else {
                    selectImageSize++;
                }
            }
            if (config.minSelectNum > 0) {
                if (selectImageSize < config.minSelectNum) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                    RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_min_img_num, String.valueOf(config.minSelectNum)));
                    return true;
                }
            }
            if (config.minVideoSelectNum > 0) {
                if (selectVideoSize < config.minVideoSelectNum) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                    RemindDialog.showTipsDialog(getContext(),
                            getString(R.string.ps_min_video_num, String.valueOf(config.minVideoSelectNum)));
                    return true;
                }
            }
        } else {
            // 单类型模式
            String mimeType = SelectedManager.getTopResultMimeType();
            if (PictureMimeType.isHasImage(mimeType) && config.minSelectNum > 0
                    && SelectedManager.getCount() < config.minSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_min_img_num,
                        String.valueOf(config.minSelectNum)));
                return true;
            }
            if (PictureMimeType.isHasVideo(mimeType) && config.minVideoSelectNum > 0
                    && SelectedManager.getCount() < config.minVideoSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_min_video_num,
                        String.valueOf(config.minVideoSelectNum)));
                return true;
            }

            if (PictureMimeType.isHasAudio(mimeType) && config.minAudioSelectNum > 0
                    && SelectedManager.getCount() < config.minAudioSelectNum) {
                if (PictureSelectionConfig.onSelectLimitTipsListener != null) {
                    boolean isSelectLimit = PictureSelectionConfig.onSelectLimitTipsListener
                            .onSelectLimitTips(getContext(), config, SelectLimitType.SELECT_MIN_AUDIO_SELECT_LIMIT);
                    if (isSelectLimit) {
                        return true;
                    }
                }
                RemindDialog.showTipsDialog(getContext(), getString(R.string.ps_min_audio_num,
                        String.valueOf(config.minAudioSelectNum)));
                return true;
            }
        }
        return false;
    }

    /**
     * 分发处理结果，比如压缩、裁剪、沙盒路径转换
     */
    protected void dispatchTransformResult() {
        if (checkCompleteSelectLimit()) {
            return;
        }
        ArrayList<LocalMedia> selectedResult = SelectedManager.getSelectedResult();
        ArrayList<LocalMedia> result = new ArrayList<>(selectedResult);
        if (checkCropValidity()) {
            LocalMedia currentLocalMedia = null;
            for (int i = 0; i < result.size(); i++) {
                LocalMedia item = result.get(i);
                if (PictureMimeType.isHasImage(result.get(i).getMimeType())) {
                    currentLocalMedia = item;
                    break;
                }
            }
            PictureSelectionConfig.cropEngine.onStartCrop(this, currentLocalMedia, result, Crop.REQUEST_CROP);
        } else if (checkCompressValidity()) {
            showLoading();
            PictureSelectionConfig.compressEngine.onStartCompress(getContext(), result,
                    new OnCallbackListener<ArrayList<LocalMedia>>() {
                        @Override
                        public void onCall(ArrayList<LocalMedia> result) {
                            onResultEvent(result);
                        }
                    });

        } else {
            onResultEvent(result);
        }
    }

    /**
     * 验证裁剪的可行性
     *
     * @return
     */
    private boolean checkCropValidity() {
        if (PictureSelectionConfig.cropEngine != null) {
            if (SelectedManager.getCount() == 1) {
                return PictureMimeType.isHasImage(SelectedManager.getTopResultMimeType());
            } else {
                for (int i = 0; i < SelectedManager.getCount(); i++) {
                    LocalMedia media = SelectedManager.getSelectedResult().get(i);
                    if (PictureMimeType.isHasImage(media.getMimeType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 验证压缩的可行性
     *
     * @return
     */
    private boolean checkCompressValidity() {
        if (PictureSelectionConfig.compressEngine != null) {
            for (int i = 0; i < SelectedManager.getCount(); i++) {
                LocalMedia media = SelectedManager.getSelectedResult().get(i);
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * SDK > 29 把外部资源copy一份至应用沙盒内
     *
     * @param result
     */
    private void copyExternalPathToAppInDirFor29(ArrayList<LocalMedia> result) {
        showLoading();
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<ArrayList<LocalMedia>>() {
            @Override
            public ArrayList<LocalMedia> doInBackground() {
                for (int i = 0; i < result.size(); i++) {
                    LocalMedia media = result.get(i);
                    PictureSelectionConfig.sandboxFileEngine.onStartSandboxFileTransform(getContext(), config.isCheckOriginalImage, i,
                            media, new OnCallbackIndexListener<LocalMedia>() {
                                @Override
                                public void onCall(LocalMedia data, int index) {
                                    LocalMedia media = result.get(index);
                                    media.setSandboxPath(data.getSandboxPath());
                                    if (config.isCheckOriginalImage) {
                                        media.setOriginalPath(data.getOriginalPath());
                                        media.setOriginal(!TextUtils.isEmpty(data.getOriginalPath()));
                                    }
                                }
                            });
                }
                return result;
            }

            @Override
            public void onSuccess(ArrayList<LocalMedia> result) {
                PictureThreadUtils.cancel(this);
                callBackResult(result);
            }
        });
    }

    /**
     * 构造原图数据
     *
     * @param result
     */
    private void mergeOriginalImage(ArrayList<LocalMedia> result) {
        if (config.isCheckOriginalImage) {
            for (int i = 0; i < result.size(); i++) {
                LocalMedia media = result.get(i);
                media.setOriginal(true);
                media.setOriginalPath(media.getPath());
            }
        }
    }

    /**
     * 返回处理完成后的选择结果
     */
    @Override
    public void onResultEvent(ArrayList<LocalMedia> result) {
        if (PictureSelectionConfig.sandboxFileEngine != null) {
            copyExternalPathToAppInDirFor29(result);
        } else {
            mergeOriginalImage(result);
            callBackResult(result);
        }
    }


    /**
     * 返回结果
     */
    private void callBackResult(ArrayList<LocalMedia> result) {
        dismissLoading();
        if (PictureSelectionConfig.onResultCallListener != null) {
            PictureSelectionConfig.onResultCallListener.onResult(result);
        }
        SelectorResult selectorResult = getResult(RESULT_OK, result);
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            getActivity().setResult(selectorResult.mResultCode, selectorResult.mResultData);
        }
        if (config.isOnlyCamera) {
            if (!ActivityCompatHelper.isDestroy(getActivity())) {
                getActivity().getSupportFragmentManager().popBackStack();
                if (config.isActivityResultBack && iBridgePictureBehavior == null) {
                    throw new IllegalArgumentException(getActivity().toString()
                            + " please must implement IBridgePictureBehavior onSelectFinish");
                }
                if (config.isActivityResultBack) {
                    iBridgePictureBehavior.onSelectFinish(true, selectorResult);
                }
            }
        } else {
            boolean isForcedExit = this instanceof PictureSelectorPreviewFragment;
            iBridgePictureBehavior.onSelectFinish(isForcedExit, selectorResult);
        }
        PictureSelectionConfig.destroy();
    }

    /**
     * set app language
     */
    @Override
    public void initAppLanguage() {
        PictureSelectionConfig config = PictureSelectionConfig.getInstance();
        if (config.language != LanguageConfig.UNKNOWN_LANGUAGE && !config.isOnlyCamera) {
            PictureLanguageUtils.setAppLanguage(getActivity(), config.language);
        }
    }

    @Override
    public void onRecreateEngine() {
        createImageLoaderEngine();
        createCompressEngine();
        createSandboxFileEngine();
        createLoaderDataEngine();
        createResultCallbackListener();
        createLayoutResourceListener();
    }

    @Override
    public void onDestroy() {
        releaseSoundPool();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        try {
            if (ActivityCompatHelper.isDestroy(getActivity())) {
                return;
            }
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismissLoading() {
        try {
            if (ActivityCompatHelper.isDestroy(getActivity())) {
                return;
            }
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        initAppLanguage();
        onRecreateEngine();
        super.onAttach(context);

        if (getParentFragment() instanceof IBridgePictureBehavior) {
            iBridgePictureBehavior = (IBridgePictureBehavior) getParentFragment();
        } else if (context instanceof IBridgePictureBehavior) {
            iBridgePictureBehavior = (IBridgePictureBehavior) context;
        } else {
            if (this instanceof PictureOnlyCameraFragment || this instanceof PictureSelectorPreviewFragment) {
                /**
                 * {@link PictureSelector.openCamera or startPreview}
                 * <p>
                 *     不需要使用到IBridgePictureBehavior，可以忽略
                 * </p>
                 */
            } else {
                throw new IllegalArgumentException(context.toString()
                        + " please must implement IBridgePictureBehavior");
            }
        }
    }

    /**
     * Get the image loading engine again, provided that the user implements the IApp interface in the Application
     */
    private void createImageLoaderEngine() {
        if (PictureSelectionConfig.imageEngine == null) {
            PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
            if (baseEngine != null) {
                PictureSelectionConfig.imageEngine = baseEngine.createImageLoaderEngine();
            }
        }
    }


    /**
     * Get the image loader data engine again, provided that the user implements the IApp interface in the Application
     */
    private void createLoaderDataEngine() {
        if (PictureSelectionConfig.getInstance().isLoaderDataEngine) {
            if (PictureSelectionConfig.loaderDataEngine == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null)
                    PictureSelectionConfig.loaderDataEngine = baseEngine.createLoaderDataEngine();
            }
        }
    }

    /**
     * Get the image compress engine again, provided that the user implements the IApp interface in the Application
     */
    private void createCompressEngine() {
        if (PictureSelectionConfig.getInstance().isCompressEngine) {
            if (PictureSelectionConfig.compressEngine == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null)
                    PictureSelectionConfig.compressEngine = baseEngine.createCompressEngine();
            }
        }
    }


    /**
     * Get the Sandbox engine again, provided that the user implements the IApp interface in the Application
     */
    private void createSandboxFileEngine() {
        if (PictureSelectionConfig.getInstance().isSandboxFileEngine) {
            if (PictureSelectionConfig.sandboxFileEngine == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null)
                    PictureSelectionConfig.sandboxFileEngine = baseEngine.createSandboxFileEngine();
            }
        }
    }


    /**
     * Retrieve the result callback listener, provided that the user implements the IApp interface in the Application
     */
    private void createResultCallbackListener() {
        if (PictureSelectionConfig.getInstance().isResultListenerBack) {
            if (PictureSelectionConfig.onResultCallListener == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null) {
                    PictureSelectionConfig.onResultCallListener = baseEngine.getResultCallbackListener();
                }
            }
        }
    }

    /**
     * Retrieve the layout callback listener, provided that the user implements the IApp interface in the Application
     */
    private void createLayoutResourceListener() {
        if (PictureSelectionConfig.getInstance().isInjectLayoutResource) {
            if (PictureSelectionConfig.onLayoutResourceListener == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null) {
                    PictureSelectionConfig.onLayoutResourceListener = baseEngine.createLayoutResourceListener();
                }
            }
        }
    }

    /**
     * generate result
     *
     * @param data result
     * @return
     */
    protected SelectorResult getResult(int resultCode, ArrayList<LocalMedia> data) {
        return new SelectorResult(resultCode, PictureSelector.putIntentResult(data));
    }

    /**
     * SelectorResult
     */
    public static class SelectorResult {

        public int mResultCode;
        public Intent mResultData;

        public SelectorResult(int resultCode, Intent data) {
            mResultCode = resultCode;
            mResultData = data;
        }
    }
}

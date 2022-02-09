package com.luck.picture.basic;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.luck.picture.PictureOnlyCameraFragment;
import com.luck.picture.PictureSelectorPreviewFragment;
import com.luck.picture.lib.R;
import com.luck.picture.animators.AnimationType;
import com.luck.picture.config.PictureConfig;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.config.SelectMimeType;
import com.luck.picture.config.SelectModeConfig;
import com.luck.picture.config.VideoQuality;
import com.luck.picture.engine.CompressEngine;
import com.luck.picture.engine.CropEngine;
import com.luck.picture.engine.ExtendLoaderEngine;
import com.luck.picture.engine.ImageEngine;
import com.luck.picture.engine.SandboxFileEngine;
import com.luck.picture.entity.LocalMedia;
import com.luck.picture.entity.LocalMediaFolder;
import com.luck.picture.interfaces.OnCameraInterceptListener;
import com.luck.picture.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.interfaces.OnInjectLayoutResourceListener;
import com.luck.picture.interfaces.OnMediaEditInterceptListener;
import com.luck.picture.interfaces.OnPermissionsInterceptListener;
import com.luck.picture.interfaces.OnPreviewInterceptListener;
import com.luck.picture.interfaces.OnResultCallbackListener;
import com.luck.picture.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.language.LanguageConfig;
import com.luck.picture.manager.SelectedManager;
import com.luck.picture.style.PictureSelectorStyle;
import com.luck.picture.style.PictureWindowAnimationStyle;
import com.luck.picture.utils.ActivityCompatHelper;
import com.luck.picture.utils.DoubleUtils;
import com.luck.picture.utils.SdkVersionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author：luck
 * @date：2017-5-24 21:30
 * @describe：PictureSelectionModel
 */

public class PictureSelectionModel {
    private final PictureSelectionConfig selectionConfig;
    private final PictureSelector selector;

    public PictureSelectionModel(PictureSelector selector) {
        this.selector = selector;
        selectionConfig = PictureSelectionConfig.getCleanInstance();
    }

    public PictureSelectionModel(PictureSelector selector, int chooseMode) {
        this.selector = selector;
        selectionConfig = PictureSelectionConfig.getCleanInstance();
        selectionConfig.chooseMode = chooseMode;
        setMaxVideoSelectNum(selectionConfig.maxVideoSelectNum);
    }

    public PictureSelectionModel(PictureSelector selector, int chooseMode, boolean isOnlyCamera) {
        this.selector = selector;
        selectionConfig = PictureSelectionConfig.getCleanInstance();
        selectionConfig.isOnlyCamera = isOnlyCamera;
        selectionConfig.chooseMode = chooseMode;
        selectionConfig.isPreviewFullScreenMode = false;
        selectionConfig.isPreviewZoomEffect = false;
    }

    /**
     * PictureSelector theme style settings
     *
     * @param uiStyle <p>
     *                Use {@link  PictureSelectorStyle
     *                It consists of the following parts and can be set separately}
     *                {@link com.luck.picture.style.TitleBarStyle}
     *                {@link com.luck.picture.style.AlbumWindowStyle}
     *                {@link com.luck.picture.style.SelectMainStyle}
     *                {@link com.luck.picture.style.BottomNavBarStyle}
     *                {@link com.luck.picture.style.PictureWindowAnimationStyle}
     *                <p/>
     * @return PictureSelectorStyle
     */
    public PictureSelectionModel setSelectorUIStyle(PictureSelectorStyle uiStyle) {
        if (uiStyle != null) {
            PictureSelectionConfig.selectorStyle = uiStyle;
        }
        return this;
    }

    /**
     * Set App Language
     *
     * @param language {@link LanguageConfig}
     * @return PictureSelectionModel
     */
    public PictureSelectionModel setLanguage(int language) {
        selectionConfig.language = language;
        return this;
    }

    /**
     * Image Load the engine
     *
     * @param engine Image Load the engine
     *               <p>
     *               <a href="https://github.com/LuckSiege/PictureSelector/blob/version_component/app/src/main/java/com/luck/pictureselector/GlideEngine.java">
     *               </p>
     * @return
     */
    public PictureSelectionModel setImageEngine(ImageEngine engine) {
        if (PictureSelectionConfig.imageEngine != engine) {
            PictureSelectionConfig.imageEngine = engine;
        }
        return this;
    }

    /**
     * Image Compress the engine
     *
     * @param engine Image Compress the engine
     * @return
     */
    public PictureSelectionModel setCompressEngine(CompressEngine engine) {
        if (PictureSelectionConfig.compressEngine != engine) {
            PictureSelectionConfig.compressEngine = engine;
            selectionConfig.isCompressEngine = true;
        } else {
            selectionConfig.isCompressEngine = false;
        }
        return this;
    }

    /**
     * Image Crop the engine
     *
     * @param engine Image Crop the engine
     * @return
     */
    public PictureSelectionModel setCropEngine(CropEngine engine) {
        if (PictureSelectionConfig.cropEngine != engine) {
            PictureSelectionConfig.cropEngine = engine;
        }
        return this;
    }

    /**
     * App Sandbox file path transform
     *
     * @param engine App Sandbox path transform
     * @return
     */
    public PictureSelectionModel setSandboxFileEngine(SandboxFileEngine engine) {
        if (SdkVersionUtils.isQ() && PictureSelectionConfig.sandboxFileEngine != engine) {
            PictureSelectionConfig.sandboxFileEngine = engine;
            selectionConfig.isSandboxFileEngine = true;
        } else {
            selectionConfig.isSandboxFileEngine = false;
        }
        return this;
    }


    /**
     * Users can implement some interfaces to access their own query data
     * The premise is that you need to comply with the model specification of PictureSelector
     * {@link ExtendLoaderEngine}
     * {@link LocalMediaFolder}
     * {@link LocalMedia}
     *
     * @param engine
     * @return
     */
    public PictureSelectionModel setExtendLoaderEngine(ExtendLoaderEngine engine) {
        if (PictureSelectionConfig.loaderDataEngine != engine) {
            PictureSelectionConfig.loaderDataEngine = engine;
            selectionConfig.isLoaderDataEngine = true;
        } else {
            selectionConfig.isLoaderDataEngine = false;
        }
        return this;
    }


    /**
     * Intercept camera click events, and users can implement their own camera framework
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setCameraInterceptListener(OnCameraInterceptListener listener) {
        PictureSelectionConfig.onCameraInterceptListener = listener;
        return this;
    }

    /**
     * Intercept preview click events, and users can implement their own preview framework
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setPreviewInterceptListener(OnPreviewInterceptListener listener) {
        PictureSelectionConfig.onPreviewInterceptListener = listener;
        return this;
    }

    /**
     * Intercept external preview click events, and users can implement their own preview framework
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setExternalPreviewEventListener(OnExternalPreviewEventListener listener) {
        PictureSelectionConfig.onExternalPreviewEventListener = listener;
        return this;
    }

    /**
     * Intercept custom inject layout events, Users can implement their own layout
     * on the premise that the view ID must be consistent
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setInjectLayoutResourceListener(OnInjectLayoutResourceListener listener) {
        selectionConfig.isInjectLayoutResource = listener != null;
        PictureSelectionConfig.onLayoutResourceListener = listener;
        return this;
    }

    /**
     * Intercept media edit click events, and users can implement their own edit media framework
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setEditMediaInterceptListener(OnMediaEditInterceptListener listener) {
        PictureSelectionConfig.onEditMediaEventListener = listener;
        return this;
    }

    /**
     * Custom interception permission processing
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setPermissionsInterceptListener(OnPermissionsInterceptListener listener) {
        PictureSelectionConfig.onPermissionsEventListener = listener;
        return this;
    }

    /**
     * Custom limit tips
     *
     * @param listener
     * @return
     */
    public PictureSelectionModel setSelectLimitTipsListener(OnSelectLimitTipsListener listener) {
        PictureSelectionConfig.onSelectLimitTipsListener = listener;
        return this;
    }

    /**
     * Do you want to open a foreground service to prevent the system from reclaiming the memory
     * of some models due to the use of cameras
     *
     * @param isForeground
     * @return
     */
    public PictureSelectionModel isCameraForegroundService(boolean isForeground) {
        selectionConfig.isCameraForegroundService = isForeground;
        return this;
    }

    /**
     * Change the desired orientation of this activity.  If the activity
     * is currently in the foreground or otherwise impacting the screen
     * orientation, the screen will immediately be changed (possibly causing
     * the activity to be restarted). Otherwise, this will be used the next
     * time the activity is visible.
     *
     * @param requestedOrientation An orientation constant as used in
     *                             {@link android.content.pm.ActivityInfo.screenOrientation ActivityInfo.screenOrientation}.
     */
    public PictureSelectionModel setRequestedOrientation(int requestedOrientation) {
        selectionConfig.requestedOrientation = requestedOrientation;
        return this;
    }


    /**
     * @param selectionMode PictureSelector Selection model
     *                      and {@link SelectModeConfig.MULTIPLE} or {@link SelectModeConfig.SINGLE}
     *                      <p>
     *                      Use {@link SelectModeConfig}
     *                      </p>
     * @return
     */
    public PictureSelectionModel setSelectionMode(int selectionMode) {
        selectionConfig.selectionMode = selectionMode;
        selectionConfig.maxSelectNum = selectionConfig.selectionMode ==
                SelectModeConfig.SINGLE ? 1 : selectionConfig.maxSelectNum;
        return this;
    }


    /**
     * You can select pictures and videos at the same time
     *
     * @param isWithVideoImage Whether the pictures and videos can be selected together
     * @return
     */
    public PictureSelectionModel isWithSelectVideoImage(boolean isWithVideoImage) {
        selectionConfig.isWithVideoImage =
                selectionConfig.selectionMode != SelectModeConfig.SINGLE
                        && selectionConfig.chooseMode == SelectMimeType.ofAll() && isWithVideoImage;
        return this;
    }

    /**
     * Choose between photographing and shooting in ofAll mode
     *
     * @param ofAllCameraType {@link PictureMimeType.ofImage or PictureMimeType.ofVideo}
     *                        The default is ofAll() mode
     * @return
     */
    public PictureSelectionModel setOfAllCameraType(int ofAllCameraType) {
        selectionConfig.ofAllCameraType = ofAllCameraType;
        return this;
    }

    /**
     * When the maximum number of choices is reached, does the list enable the mask effect
     *
     * @param isMaxSelectEnabledMask
     * @return
     */
    public PictureSelectionModel isMaxSelectEnabledMask(boolean isMaxSelectEnabledMask) {
        selectionConfig.isMaxSelectEnabledMask = isMaxSelectEnabledMask;
        return this;
    }

    /**
     * Do you need to display the original controller
     * <p>
     * It needs to be used with setSandboxFileEngine
     * {@link LocalMedia .setSandboxPath()}
     * </p>
     *
     * @param isOriginalControl
     * @return
     */
    public PictureSelectionModel isOriginalControl(boolean isOriginalControl) {
        selectionConfig.isOriginalControl = isOriginalControl;
        if (selectionConfig.isOnlyCamera && isOriginalControl) {
            selectionConfig.isCheckOriginalImage = true;
        }
        return this;
    }

    /**
     * If SyncCover
     *
     * @param isSyncCover
     * @return
     */
    public PictureSelectionModel isSyncCover(boolean isSyncCover) {
        selectionConfig.isSyncCover = isSyncCover;
        return this;
    }

    /**
     * Select the maximum number of files
     *
     * @param maxSelectNum PictureSelector max selection
     * @return
     */
    public PictureSelectionModel setMaxSelectNum(int maxSelectNum) {
        selectionConfig.maxSelectNum = selectionConfig.selectionMode == SelectModeConfig.SINGLE ? 1 : maxSelectNum;
        return this;
    }

    /**
     * Select the minimum number of files
     *
     * @param minSelectNum PictureSelector min selection
     * @return
     */
    public PictureSelectionModel setMinSelectNum(int minSelectNum) {
        selectionConfig.minSelectNum = minSelectNum;
        return this;
    }


    /**
     * By clicking the title bar consecutively, RecyclerView automatically rolls back to the top
     *
     * @param isAutomaticTitleRecyclerTop
     * @return
     */
    public PictureSelectionModel isAutomaticTitleRecyclerTop(boolean isAutomaticTitleRecyclerTop) {
        selectionConfig.isAutomaticTitleRecyclerTop = isAutomaticTitleRecyclerTop;
        return this;
    }


    /**
     * @param Select whether to return directly
     * @return
     */
    public PictureSelectionModel isDirectReturnSingle(boolean isDirectReturn) {
        selectionConfig.isDirectReturnSingle = selectionConfig.selectionMode == SelectModeConfig.SINGLE && isDirectReturn;
        return this;
    }


    /**
     * Whether to turn on paging mode
     *
     * @param isPageStrategy
     * @return
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy) {
        selectionConfig.isPageStrategy = isPageStrategy;
        return this;
    }

    /**
     * Whether to turn on paging mode
     *
     * @param isPageStrategy
     * @param pageSize       Maximum number of pages {@link PageSize is preferably no less than 20}
     * @return
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, int pageSize) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.pageSize = pageSize < PictureConfig.MIN_PAGE_SIZE ? PictureConfig.MAX_PAGE_SIZE : pageSize;
        return this;
    }


    /**
     * Whether to turn on paging mode
     *
     * @param isPageStrategy
     * @param isFilterInvalidFile Whether to filter invalid files {@link Some of the query performance is consumed,Especially on the Q version}
     * @return
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, boolean isFilterInvalidFile) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.isFilterInvalidFile = isFilterInvalidFile;
        return this;
    }

    /**
     * Whether to turn on paging mode
     *
     * @param isPageStrategy
     * @param pageSize            Maximum number of pages {@link  PageSize is preferably no less than 20}
     * @param isFilterInvalidFile Whether to filter invalid files {@link Some of the query performance is consumed,Especially on the Q version}
     * @return
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, int pageSize, boolean isFilterInvalidFile) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.pageSize = pageSize < PictureConfig.MIN_PAGE_SIZE ? PictureConfig.MAX_PAGE_SIZE : pageSize;
        selectionConfig.isFilterInvalidFile = isFilterInvalidFile;
        return this;
    }


    /**
     * The video quality output mode is only for system recording, and there are only two modes: poor quality or high quality
     *
     * @param videoQuality video quality and 0 or 1
     *                     Use {@link VideoQuality}
     *                     <p>
     *                     There are limitations, only high or low
     *                     </p>
     * @return
     */
    @Deprecated
    public PictureSelectionModel setVideoQuality(int videoQuality) {
        selectionConfig.videoQuality = videoQuality;
        return this;
    }


    /**
     * camera output image format
     *
     * @param imageFormat PictureSelector media format
     * @return
     */
    public PictureSelectionModel setCameraImageFormat(String imageFormat) {
        selectionConfig.cameraImageFormat = imageFormat;
        return this;
    }

    /**
     * camera output image format
     *
     * @param imageFormat PictureSelector media format
     * @return
     */
    public PictureSelectionModel setCameraImageFormatForQ(String imageFormat) {
        selectionConfig.cameraImageFormatForQ = imageFormat;
        return this;
    }

    /**
     * camera output video format
     *
     * @param videoFormat PictureSelector media format
     * @return
     */
    public PictureSelectionModel setCameraVideoFormat(String videoFormat) {
        selectionConfig.cameraVideoFormat = videoFormat;
        return this;
    }

    /**
     * camera output video format
     *
     * @param videoFormat PictureSelector media format
     * @return
     */
    public PictureSelectionModel setCameraVideoFormatForQ(String videoFormat) {
        selectionConfig.cameraVideoFormatForQ = videoFormat;
        return this;
    }


    /**
     * filter max seconds video
     *
     * @param videoMaxSecond filter video max second
     * @return
     */
    public PictureSelectionModel setFilterVideoMaxSecond(int videoMaxSecond) {
        selectionConfig.filterVideoMaxSecond = videoMaxSecond * 1000;
        return this;
    }

    /**
     * filter min seconds video
     *
     * @param videoMinSecond filter video min second
     * @return
     */
    public PictureSelectionModel setFilterVideoMinSecond(int videoMinSecond) {
        selectionConfig.filterVideoMinSecond = videoMinSecond * 1000;
        return this;
    }

    /**
     * Select the max number of seconds for video or audio support
     *
     * @param maxDurationSecond select video max second
     * @return
     */
    public PictureSelectionModel setSelectMaxDurationSecond(int maxDurationSecond) {
        selectionConfig.selectMaxDurationSecond = maxDurationSecond * 1000;
        return this;
    }

    /**
     * Select the min number of seconds for video or audio support
     *
     * @param minDurationSecond select video min second
     * @return
     */
    public PictureSelectionModel setSelectMinDurationSecond(int minDurationSecond) {
        selectionConfig.selectMinDurationSecond = minDurationSecond * 1000;
        return this;
    }

    /**
     * The max duration of video recording. If it is system recording, there may be compatibility problems
     *
     * @param maxSecond video record second
     * @return
     */
    public PictureSelectionModel setRecordVideoMaxSecond(int maxSecond) {
        selectionConfig.recordVideoMaxSecond = maxSecond;
        return this;
    }


    /**
     * Select the maximum video number of files
     *
     * @param maxVideoSelectNum PictureSelector video max selection
     * @return
     */
    public PictureSelectionModel setMaxVideoSelectNum(int maxVideoSelectNum) {
        selectionConfig.maxVideoSelectNum = selectionConfig.chooseMode == SelectMimeType.ofVideo() ? 0 : maxVideoSelectNum;
        return this;
    }

    /**
     * Select the minimum video number of files
     *
     * @param minVideoSelectNum PictureSelector video min selection
     * @return
     */
    public PictureSelectionModel setMinVideoSelectNum(int minVideoSelectNum) {
        selectionConfig.minVideoSelectNum = minVideoSelectNum;
        return this;
    }

    /**
     * Select the minimum audio number of files
     *
     * @param minAudioSelectNum PictureSelector audio min selection
     * @return
     */
    public PictureSelectionModel setMinAudioSelectNum(int minAudioSelectNum) {
        selectionConfig.minAudioSelectNum = minAudioSelectNum;
        return this;
    }

    /**
     * @param minSecond video record second
     * @return
     */
    public PictureSelectionModel setRecordVideoMinSecond(int minSecond) {
        selectionConfig.recordVideoMinSecond = minSecond;
        return this;
    }

    /**
     * @param imageSpanCount PictureSelector image span count
     * @return
     */
    public PictureSelectionModel setImageSpanCount(int imageSpanCount) {
        selectionConfig.imageSpanCount = imageSpanCount;
        return this;
    }

    /**
     * @param isEmptyReturn No data can be returned
     * @return
     */
    public PictureSelectionModel isEmptyResultReturn(boolean isEmptyReturn) {
        selectionConfig.isEmptyResultReturn = isEmptyReturn;
        return this;
    }


    /**
     * After recording with the system camera, does it support playing the video immediately using the system player
     *
     * @param isQuickCapture
     * @return
     */
    public PictureSelectionModel isQuickCapture(boolean isQuickCapture) {
        selectionConfig.isQuickCapture = isQuickCapture;
        return this;
    }

    /**
     * @param isDisplayCamera Whether to open camera button
     * @return
     */
    public PictureSelectionModel isDisplayCamera(boolean isDisplayCamera) {
        selectionConfig.isDisplayCamera = isDisplayCamera;
        return this;
    }

    /**
     * @param outPutCameraDir Camera output path
     *                        <p>Audio mode setting is not supported</p>
     * @return
     */
    public PictureSelectionModel setOutputCameraDir(String outPutCameraDir) {
        selectionConfig.outPutCameraDir = outPutCameraDir;
        return this;
    }

    /**
     * @param outPutAudioDir Audio output path
     * @return
     */
    public PictureSelectionModel setOutputAudioDir(String outPutAudioDir) {
        selectionConfig.outPutAudioDir = outPutAudioDir;
        return this;
    }

    /**
     * Camera IMAGE custom local file name
     * # Such as xxx.png
     *
     * @param fileName
     * @return
     */
    public PictureSelectionModel setOutputCameraImageFileName(String fileName) {
        selectionConfig.outPutCameraImageFileName = fileName;
        return this;
    }

    /**
     * Camera VIDEO custom local file name
     * # Such as xxx.png
     *
     * @param fileName
     * @return
     */
    public PictureSelectionModel setOutputCameraVideoFileName(String fileName) {
        selectionConfig.outPutCameraVideoFileName = fileName;
        return this;
    }

    /**
     * Camera VIDEO custom local file name
     * # Such as xxx.amr
     *
     * @param fileName
     * @return
     */
    public PictureSelectionModel setOutputAudioFileName(String fileName) {
        selectionConfig.outPutAudioFileName = fileName;
        return this;
    }

    /**
     * Query the pictures or videos in the specified directory
     *
     * @param dir Camera out path
     *            <p>
     *            Normally, it should be consistent with {@link PictureSelectionConfig.setOutputCameraDir()};
     *            </p>
     *
     *            <p>
     *            If build.version.sdk_INT < 29,{@link PictureSelectionConfig.setQuerySandboxDir();}
     *            Do not set the external storage path,
     *            which may cause the problem of picture duplication
     *            </p>
     * @return
     */
    public PictureSelectionModel setQuerySandboxDir(String dir) {
        selectionConfig.sandboxDir = dir;
        return this;
    }

    /**
     * Only the resources in the specified directory are displayed
     * <p>
     * Only Display setQuerySandboxDir();  Source
     * <p/>
     *
     * @param isOnlySandboxDir true or Only Display {@link PictureSelectionConfig.setQuerySandboxDir();}
     * @return
     */
    public PictureSelectionModel isOnlyObtainSandboxDir(boolean isOnlySandboxDir) {
        selectionConfig.isOnlySandboxDir = isOnlySandboxDir;
        return this;
    }

    /**
     * Displays the creation timeline of the resource
     *
     * @param isDisplayTimeAxis
     * @return
     */
    public PictureSelectionModel isDisplayTimeAxis(boolean isDisplayTimeAxis) {
        if (selectionConfig.isOnlyCamera) {
            selectionConfig.isDisplayTimeAxis = false;
        } else {
            selectionConfig.isDisplayTimeAxis = isDisplayTimeAxis;
        }
        return this;
    }

    /**
     * # file size The unit is KB
     *
     * @param fileKbSize Filter max file size
     * @return
     */
    public PictureSelectionModel setFilterMaxFileSize(long fileKbSize) {
        if (fileKbSize >= PictureConfig.MB) {
            selectionConfig.filterMaxFileSize = fileKbSize;
        } else {
            selectionConfig.filterMaxFileSize = fileKbSize * 1024;
        }
        return this;
    }

    /**
     * # file size The unit is KB
     *
     * @param fileKbSize Filter min file size
     * @return
     */
    public PictureSelectionModel setFilterMinFileSize(long fileKbSize) {
        if (fileKbSize >= PictureConfig.MB) {
            selectionConfig.filterMinFileSize = fileKbSize;
        } else {
            selectionConfig.filterMinFileSize = fileKbSize * 1024;
        }
        return this;
    }


    /**
     * # file size The unit is KB
     *
     * @param fileKbSize Filter max file size
     * @return
     */
    public PictureSelectionModel setSelectMaxFileSize(long fileKbSize) {
        if (fileKbSize >= PictureConfig.MB) {
            selectionConfig.selectMaxFileSize = fileKbSize;
        } else {
            selectionConfig.selectMaxFileSize = fileKbSize * 1024;
        }
        return this;
    }

    /**
     * # file size The unit is KB
     *
     * @param fileKbSize Filter min file size
     * @return
     */
    public PictureSelectionModel setSelectMinFileSize(long fileKbSize) {
        if (fileKbSize >= PictureConfig.MB) {
            selectionConfig.selectMinFileSize = fileKbSize;
        } else {
            selectionConfig.selectMinFileSize = fileKbSize * 1024;
        }
        return this;
    }

    /**
     * query only mimeType
     *
     * @param mimeTypes Use example {@link { image/jpeg or image/png ... }}
     * @return
     */
    public PictureSelectionModel setQueryOnlyMimeType(String... mimeTypes) {
        if (mimeTypes != null && mimeTypes.length > 0) {
            selectionConfig.queryOnlyList.addAll(Arrays.asList(mimeTypes));
        }
        return this;
    }

    /**
     * @param isGif Whether to open gif
     * @return
     */
    public PictureSelectionModel isGif(boolean isGif) {
        selectionConfig.isGif = isGif;
        return this;
    }

    /**
     * @param isWebp Whether to open .webp
     * @return
     */
    public PictureSelectionModel isWebp(boolean isWebp) {
        selectionConfig.isWebp = isWebp;
        return this;
    }

    /**
     * @param isBmp Whether to open .isBmp
     * @return
     */
    public PictureSelectionModel isBmp(boolean isBmp) {
        selectionConfig.isBmp = isBmp;
        return this;
    }

    /**
     * Preview Full Screen Mode
     *
     * @param isFullScreenModel
     * @return
     */
    public PictureSelectionModel isPreviewFullScreenMode(boolean isFullScreenModel) {
        if (selectionConfig.isOnlyCamera) {
            selectionConfig.isPreviewFullScreenMode = false;
        } else {
            selectionConfig.isPreviewFullScreenMode = isFullScreenModel;
        }
        return this;
    }

    /**
     * Preview Zoom Effect Mode
     *
     * @return
     */
    public PictureSelectionModel isPreviewZoomEffect(boolean isPreviewZoomEffect) {
        if (selectionConfig.isOnlyCamera) {
            selectionConfig.isPreviewZoomEffect = false;
        } else {
            selectionConfig.isPreviewZoomEffect = isPreviewZoomEffect;
        }
        return this;
    }

    /**
     * Do you want to preview play the audio file?
     *
     * @param isPreviewAudio
     * @return
     */
    public PictureSelectionModel isPreviewAudio(boolean isPreviewAudio) {
        selectionConfig.isEnablePreviewAudio = isPreviewAudio;
        return this;
    }

    /**
     * @param isPreviewImage Do you want to preview the picture?
     * @return
     */
    public PictureSelectionModel isPreviewImage(boolean isPreviewImage) {
        selectionConfig.isEnablePreviewImage = isPreviewImage;
        return this;
    }


    /**
     * @param isPreviewVideo Do you want to preview the video?
     * @return
     */
    public PictureSelectionModel isPreviewVideo(boolean isPreviewVideo) {
        selectionConfig.isEnablePreviewVideo = isPreviewVideo;
        return this;
    }

    /**
     * @param isHidePreviewDownload Previews do not show downloads
     * @return
     */
    public PictureSelectionModel isHidePreviewDownload(boolean isHidePreviewDownload) {
        selectionConfig.isHidePreviewDownload = isHidePreviewDownload;
        return this;
    }


    /**
     * @param isClickSound Whether to open click voice
     * @return
     */
    public PictureSelectionModel isOpenClickSound(boolean isClickSound) {
        selectionConfig.isOpenClickSound = !selectionConfig.isOnlyCamera && isClickSound;
        return this;
    }

    /**
     * Set camera direction (after default image)
     */
    public PictureSelectionModel isCameraAroundState(boolean isCameraAroundState) {
        selectionConfig.isCameraAroundState = isCameraAroundState;
        return this;
    }

    /**
     * Camera image rotation, automatic correction
     */
    public PictureSelectionModel isCameraRotateImage(boolean isCameraRotateImage) {
        selectionConfig.isCameraRotateImage = isCameraRotateImage;
        return this;
    }

    /**
     * @param selectedList Select the selected picture set
     * @return
     */
    public PictureSelectionModel setSelectedData(List<LocalMedia> selectedList) {
        if (selectedList == null) {
            return this;
        }
        if (selectionConfig.selectionMode == SelectModeConfig.SINGLE && selectionConfig.isDirectReturnSingle) {
            SelectedManager.clear();
        } else {
            SelectedManager.getSelectedResult().addAll(new ArrayList<>(selectedList));
        }
        return this;
    }

    /**
     * Photo album list animation {}
     * Use {@link AnimationType#ALPHA_IN_ANIMATION or SLIDE_IN_BOTTOM_ANIMATION} directly.
     *
     * @param animationMode
     * @return
     */
    public PictureSelectionModel setRecyclerAnimationMode(int animationMode) {
        selectionConfig.animationMode = animationMode;
        return this;
    }

    /**
     * preview LocalMedia
     *
     * @param currentPosition current position
     * @param isDisplayDelete if visible delete
     * @param list            preview data
     */
    public void startFragmentPreview(int currentPosition, boolean isDisplayDelete, List<LocalMedia> list) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            if (PictureSelectionConfig.imageEngine == null) {
                throw new NullPointerException("imageEngine is null,Please implement ImageEngine");
            }
            if (list == null || list.size() == 0) {
                throw new NullPointerException("preview data is null");
            }
            FragmentManager fragmentManager = null;
            if (activity instanceof AppCompatActivity) {
                fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            } else if (activity instanceof FragmentActivity) {
                fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            }
            if (fragmentManager == null) {
                throw new NullPointerException("FragmentManager cannot be null");
            }
            if (ActivityCompatHelper.checkFragmentNonExits((FragmentActivity) activity, PictureSelectorPreviewFragment.TAG)) {
                PictureSelectorPreviewFragment fragment = PictureSelectorPreviewFragment.newInstance();
                ArrayList<LocalMedia> previewData = new ArrayList<>(list);
                fragment.setExternalPreviewData(currentPosition, previewData.size(), previewData, isDisplayDelete);
                FragmentInjectManager.injectSystemRoomFragment(fragmentManager, PictureSelectorPreviewFragment.TAG, fragment);
            }
        }
    }

    /**
     * preview LocalMedia
     *
     * @param currentPosition current position
     * @param isDisplayDelete if visible delete
     * @param list            preview data
     */
    public void startActivityPreview(int currentPosition, boolean isDisplayDelete, ArrayList<LocalMedia> list) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            if (PictureSelectionConfig.imageEngine == null) {
                throw new NullPointerException("imageEngine is null,Please implement ImageEngine");
            }
            if (list == null || list.size() == 0) {
                throw new NullPointerException("preview data is null");
            }
            Intent intent = new Intent(activity, PictureSelectorSupporterActivity.class);
            SelectedManager.addSelectedPreviewResult(list);
            intent.putExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW, true);
            intent.putExtra(PictureConfig.EXTRA_PREVIEW_CURRENT_POSITION, currentPosition);
            intent.putExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW_DISPLAY_DELETE, isDisplayDelete);
            activity.startActivity(intent);
            PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
            activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.ps_anim_fade_in);
        }
    }

    /**
     * Start PictureSelector
     *
     * @param call
     */
    public void forResult(OnResultCallbackListener<LocalMedia> call) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            if (call == null) {
                throw new NullPointerException("OnResultCallbackListener cannot be null");
            }
            // 绑定回调监听
            selectionConfig.isResultListenerBack = true;
            selectionConfig.isActivityResultBack = false;
            PictureSelectionConfig.onResultCallListener = call;
            if (selectionConfig.isOnlyCamera) {
                FragmentManager fragmentManager = null;
                if (activity instanceof AppCompatActivity) {
                    fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                } else if (activity instanceof FragmentActivity) {
                    fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                }
                if (fragmentManager == null) {
                    throw new NullPointerException("FragmentManager cannot be null");
                }
                Fragment fragment = fragmentManager.findFragmentByTag(PictureOnlyCameraFragment.TAG);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
                FragmentInjectManager.injectSystemRoomFragment(fragmentManager,
                        PictureOnlyCameraFragment.TAG, PictureOnlyCameraFragment.newInstance());
            } else {
                if (PictureSelectionConfig.imageEngine == null) {
                    throw new NullPointerException("imageEngine is null,Please implement ImageEngine");
                }
                Intent intent = new Intent(activity, PictureSelectorSupporterActivity.class);
                activity.startActivity(intent);
                PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
                activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.ps_anim_fade_in);
            }
        }
    }


    /**
     * Start PictureSelector
     *
     * @param requestCode
     */
    public void forResult(int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            selectionConfig.isResultListenerBack = false;
            selectionConfig.isActivityResultBack = true;
            if (selectionConfig.isOnlyCamera) {
                FragmentManager fragmentManager = null;
                if (activity instanceof AppCompatActivity) {
                    fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                } else if (activity instanceof FragmentActivity) {
                    fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                }
                if (fragmentManager == null) {
                    throw new NullPointerException("FragmentManager cannot be null");
                }
                Fragment fragment = fragmentManager.findFragmentByTag(PictureOnlyCameraFragment.TAG);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
                FragmentInjectManager.injectSystemRoomFragment(fragmentManager,
                        PictureOnlyCameraFragment.TAG, PictureOnlyCameraFragment.newInstance());
            } else {
                if (PictureSelectionConfig.imageEngine == null) {
                    throw new NullPointerException("imageEngine is null,Please implement ImageEngine");
                }
                Intent intent = new Intent(activity, PictureSelectorSupporterActivity.class);
                activity.startActivityForResult(intent, requestCode);
                PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
                activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.ps_anim_fade_in);
            }
        }
    }


    /**
     * ActivityResultLauncher PictureSelector
     *
     * @param launcher use {@link Activity.registerForActivityResult(ActivityResultContract, ActivityResultCallback)}
     */
    public void forResult(ActivityResultLauncher<Intent> launcher) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            if (launcher == null) {
                throw new NullPointerException("ActivityResultLauncher cannot be null");
            }
            selectionConfig.isResultListenerBack = false;
            selectionConfig.isActivityResultBack = true;
            if (selectionConfig.isOnlyCamera) {
                FragmentManager fragmentManager = null;
                if (activity instanceof AppCompatActivity) {
                    fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                } else if (activity instanceof FragmentActivity) {
                    fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                }
                if (fragmentManager == null) {
                    throw new NullPointerException("FragmentManager cannot be null");
                }
                Fragment fragment = fragmentManager.findFragmentByTag(PictureOnlyCameraFragment.TAG);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
                FragmentInjectManager.injectSystemRoomFragment(fragmentManager,
                        PictureOnlyCameraFragment.TAG, PictureOnlyCameraFragment.newInstance());
            } else {
                if (PictureSelectionConfig.imageEngine == null) {
                    throw new NullPointerException("imageEngine is null,Please implement ImageEngine");
                }
                Intent intent = new Intent(activity, PictureSelectorSupporterActivity.class);
                launcher.launch(intent);
                PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
                activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.ps_anim_fade_in);
            }
        }
    }
}

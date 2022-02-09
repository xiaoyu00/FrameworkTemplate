package com.luck.picture.camerax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.camera.view.video.OnVideoSavedCallback;
import androidx.camera.view.video.OutputFileOptions;
import androidx.camera.view.video.OutputFileResults;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.luck.picture.camerax.listener.CaptureListener;
import com.luck.picture.camerax.listener.ClickListener;
import com.luck.picture.camerax.listener.FlowCameraListener;
import com.luck.picture.camerax.listener.OnVideoPlayPrepareListener;
import com.luck.picture.camerax.listener.TypeListener;
import com.luck.picture.camerax.utils.CameraUtils;
import com.luck.picture.camerax.utils.FileUtil;
import com.luck.picture.camerax.utils.ScreenUtils;
import com.luck.picture.camerax.views.CaptureLayout;
import com.luck.picture.config.PictureConfig;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.lib.R;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FrameworkCameraView extends FrameLayout implements LifecycleOwner {

    final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private int lensFacing = -1;
    //闪关灯状态
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;
    private int type_flash = TYPE_FLASH_OFF;

    // 选择拍照 拍视频 或者都有
    public static final int BUTTON_STATE_ONLY_CAPTURE = 1;      //只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 2;     //只能录像
    public static final int BUTTON_STATE_BOTH = 0;
    public static final int EXTRA_RECORD_VIDEO_SECOND = 10;//拍摄视频时长
    private Context mContext;

    private FlowCameraListener flowCameraListener;
    private ClickListener leftClickListener;
    private PreviewView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private ImageView mFlashLamp;
    private CaptureLayout mCaptureLayout;
    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    private LifecycleCameraController lifecycleCameraController;
    private int useCameraCases = -1;
    private String filePath;
    //切换摄像头按钮的参数
    private int iconSrc;        //图标资源
    private int iconLeft;       //左图标
    private int iconRight;      //右图标
    private int duration;      //录制时间
    private long recordTime = 0;

    public FrameworkCameraView(@NonNull Context context) {
        this(context, null);
    }

    public FrameworkCameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameworkCameraView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FrameworkCameraView, defStyleAttr, 0);
        iconSrc = a.getResourceId(R.styleable.FrameworkCameraView_iconSrc, R.drawable.ic_camera);
        iconLeft = a.getResourceId(R.styleable.FrameworkCameraView_iconLeft, 0);
        iconRight = a.getResourceId(R.styleable.FrameworkCameraView_iconRight, 0);
        duration = a.getInteger(R.styleable.FrameworkCameraView_duration_max, EXTRA_RECORD_VIDEO_SECOND * 1000);       //没设置默认为10s
        a.recycle();
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.flow_camera_view, this);
        mVideoView = view.findViewById(R.id.video_preview);
        mTextureView = view.findViewById(R.id.mVideo);
        mPhoto = view.findViewById(R.id.image_photo);
        mSwitchCamera = view.findViewById(R.id.image_switch);
        mSwitchCamera.setImageResource(iconSrc);
        mFlashLamp = view.findViewById(R.id.image_flash);
        initController();
        initCapture(view);
        initOthers(view);
    }

    private void initController() {
        lifecycleCameraController = new LifecycleCameraController(mContext);
        lifecycleCameraController.bindToLifecycle(FrameworkCameraView.this);
        lifecycleCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_AUTO);
        mVideoView.setController(lifecycleCameraController);
    }

    private void initCapture(View view) {
        mCaptureLayout = view.findViewById(R.id.capture_layout);
//        mCaptureLayout.setDuration(duration);
        mCaptureLayout.setIconSrc(iconLeft, iconRight);
        mCaptureLayout.setButtonFeatures(PictureSelectionConfig.getInstance().chooseMode);
        if (PictureSelectionConfig.getInstance().recordVideoMaxSecond > 0) {
            mCaptureLayout.setDuration(PictureSelectionConfig.getInstance().recordVideoMaxSecond * 1000);
        }
        if (PictureSelectionConfig.getInstance().recordVideoMinSecond > 0) {
            mCaptureLayout.setMinDuration(PictureSelectionConfig.getInstance().recordVideoMinSecond * 1000);
        }
        //拍照 录像
        mCaptureLayout.setCaptureLisenter(new CaptureListener() {
            @Override
            public void takePictures() {
                takePicture();
            }

            @SuppressLint("UnsafeExperimentalUsageError")
            @Override
            public void recordStart() {
                startRecord();
            }

            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void recordShort(final long time) {
                recordTime = time;
                mSwitchCamera.setVisibility(VISIBLE);
                mFlashLamp.setVisibility(VISIBLE);
                mCaptureLayout.resetCaptureLayout();
                mCaptureLayout.setTextWithAnimation("录制时间过短");
                lifecycleCameraController.stopRecording();
            }

            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void recordEnd(long time) {
                recordTime = time;
                lifecycleCameraController.stopRecording();
            }

            @Override
            public void recordZoom(float zoom) {

            }

            @Override
            public void recordError() {
                if (flowCameraListener != null) {
                    flowCameraListener.onError(0, "未知原因!", null);
                }
            }
        });
        //确认 取消
        mCaptureLayout.setTypeLisenter(new TypeListener() {
            @Override
            public void cancel() {
                stopVideoPlay();
                resetState();
            }

            @Override
            public void confirm() {

                if (isImageCaptureEnabled()) {
                    mPhoto.setVisibility(INVISIBLE);
                    if (flowCameraListener != null) {
                        flowCameraListener.captureSuccess(filePath);
                    }
                } else {
                    stopVideoPlay();
                    if (flowCameraListener != null) {
                        flowCameraListener.recordSuccess(filePath);
                    }
                }
//                scanPhotoAlbum(filePath);
            }
        });
        mCaptureLayout.setLeftClickListener(() -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });
    }

    private void initOthers(View view) {
        setFlashRes();
        mFlashLamp.setOnClickListener(v -> {
            type_flash++;
            if (type_flash > 0x023)
                type_flash = TYPE_FLASH_AUTO;
            setFlashRes();
        });
        //切换摄像头
        mSwitchCamera.setOnClickListener(v -> {
            switchCamera();
        });
    }

    /**
     * 当确认保存此文件时才去扫描相册更新并显示视频和图片
     */
    private void scanPhotoAlbum(String path) {
        if (path == null) {
            return;
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(path.substring(path.lastIndexOf(".") + 1));
        MediaScannerConnection.scanFile(
                mContext, new String[]{path}, new String[]{mimeType}, null);
    }

    private boolean isImageCaptureEnabled() {
        return useCameraCases == LifecycleCameraController.IMAGE_CAPTURE;
    }

    private void takePicture() {
        mSwitchCamera.setVisibility(INVISIBLE);
        mFlashLamp.setVisibility(INVISIBLE);
        if (lensFacing == -1) {
            lensFacing = CameraSelector.LENS_FACING_BACK;
        }

        boolean isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT;
        ImageCapture.Metadata metadata = new ImageCapture.Metadata();
        metadata.setReversedHorizontal(isReversedHorizontal);
        ImageCapture.OutputFileOptions fileOptions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && TextUtils.isEmpty(PictureSelectionConfig.getInstance().outPutCameraDir)) {
            ContentValues contentValues = FileUtil.buildImageContentValues(PictureSelectionConfig.getInstance().outPutCameraImageFileName, PictureSelectionConfig.getInstance().cameraImageFormatForQ);
            fileOptions = new ImageCapture.OutputFileOptions.Builder(getContext().getContentResolver()
                    , MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    .setMetadata(metadata).build();
        } else {
            fileOptions = new ImageCapture.OutputFileOptions.Builder(FileUtil.createCameraFile(mContext, FileUtil.TYPE_IMAGE,
                    PictureSelectionConfig.getInstance().outPutCameraImageFileName, PictureSelectionConfig.getInstance().cameraImageFormat, PictureSelectionConfig.getInstance().outPutCameraDir))
                    .setMetadata(metadata).build();
        }
        useCameraCases = CameraController.IMAGE_CAPTURE;
        lifecycleCameraController.setImageCaptureMode(PictureSelectionConfig.getInstance().imageQuality == 0 ? ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY : ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY);
        lifecycleCameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE | CameraController.IMAGE_ANALYSIS);
        lifecycleCameraController.takePicture(fileOptions, ContextCompat.getMainExecutor(mContext), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                if (outputFileResults.getSavedUri() == null) {
                    Toast.makeText(mContext, "图片保存出错!", Toast.LENGTH_LONG).show();
                    return;
                }
                Uri savedUri = outputFileResults.getSavedUri();
                ((Activity) mContext).getIntent().putExtra(MediaStore.EXTRA_OUTPUT, savedUri);
                filePath = FileUtil.isContent(savedUri.toString()) ? savedUri.toString() : savedUri.getPath();
                PictureSelectionConfig.imageEngine.loadImage(mContext, filePath, mPhoto);
                mPhoto.setVisibility(View.VISIBLE);
                mCaptureLayout.startTypeBtnAnimator();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                if (flowCameraListener != null) {
                    flowCameraListener.onError(exception.getImageCaptureError(), Objects.requireNonNull(exception.getMessage()), exception.getCause());
                }
            }
        });
    }

    private void switchCamera() {
        if (lifecycleCameraController.getCameraSelector() == CameraSelector.DEFAULT_FRONT_CAMERA && lifecycleCameraController.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
            lifecycleCameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
        } else if (lifecycleCameraController.getCameraSelector() == CameraSelector.DEFAULT_BACK_CAMERA && lifecycleCameraController.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
            lifecycleCameraController.setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void startRecord() {
        mSwitchCamera.setVisibility(INVISIBLE);
        mFlashLamp.setVisibility(INVISIBLE);

        OutputFileOptions fileOptions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && TextUtils.isEmpty(PictureSelectionConfig.getInstance().outPutCameraDir)) {
            ContentValues contentValues = CameraUtils.buildVideoContentValues(PictureSelectionConfig.getInstance().outPutCameraVideoFileName, PictureSelectionConfig.getInstance().cameraVideoFormatForQ);
            fileOptions = OutputFileOptions.builder(
                    getContext().getContentResolver(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    contentValues).build();
        } else {
            File cameraFile = FileUtil.createCameraFile(getContext(), FileUtil.TYPE_VIDEO,
                    PictureSelectionConfig.getInstance().outPutCameraVideoFileName, PictureSelectionConfig.getInstance().cameraVideoFormat, PictureSelectionConfig.getInstance().outPutCameraDir);
            fileOptions = OutputFileOptions.builder(cameraFile).build();
        }
        useCameraCases = CameraController.VIDEO_CAPTURE;
        lifecycleCameraController.setEnabledUseCases(CameraController.VIDEO_CAPTURE);
        lifecycleCameraController.startRecording(fileOptions, ContextCompat.getMainExecutor(mContext), new OnVideoSavedCallback() {
            @Override
            public void onVideoSaved(@NonNull OutputFileResults outputFileResults) {

                long minSecond = PictureSelectionConfig.getInstance().recordVideoMinSecond <= 0 ? PictureConfig.DEFAULT_MIN_RECORD_VIDEO : PictureSelectionConfig.getInstance().recordVideoMinSecond;
                if (recordTime < minSecond || outputFileResults.getSavedUri() == null) {
                    return;
                }
                Uri savedUri = outputFileResults.getSavedUri();
                ((Activity) mContext).getIntent().putExtra(MediaStore.EXTRA_OUTPUT, savedUri);
                filePath = FileUtil.isContent(savedUri.toString()) ? savedUri.toString() : savedUri.getPath();
                mTextureView.setVisibility(View.VISIBLE);
                mTextureView.setVisibility(View.VISIBLE);
                mCaptureLayout.startTypeBtnAnimator();
                transformsTextureView(mTextureView);
                if (mTextureView.isAvailable()) {
                    startVideoPlay(filePath, () ->
                            mVideoView.setVisibility(View.GONE)
                    );
                } else {
                    mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                        @Override
                        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                            startVideoPlay(filePath, () ->
                                    mVideoView.setVisibility(View.GONE)
                            );
                        }

                        @Override
                        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                            return false;
                        }

                        @Override
                        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                        }
                    });
                }
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                if (flowCameraListener != null) {
                    flowCameraListener.onError(videoCaptureError, message, cause);
                }
            }
        });
    }

    /**
     * 预览自拍视频时 旋转TextureView 解决左右镜像的问题
     *
     * @param textureView
     */
    private void transformsTextureView(TextureView textureView) {
        Matrix matrix = new Matrix();
        int screenHeight = ScreenUtils.getScreenHeight(mContext);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            matrix.postScale(-1, 1, 1f * screenWidth / 2, 1f * screenHeight / 2);
        } else {
            matrix.postScale(1, 1, 1f * screenWidth / 2, 1f * screenHeight / 2);
        }
        textureView.setTransform(matrix);
    }

    /**
     * 开始循环播放视频
     */
    private void startVideoPlay(String path, OnVideoPlayPrepareListener onVideoPlayPrepareListener) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            if (FileUtil.isContent(path)) {
                mMediaPlayer.setDataSource(getContext(), Uri.parse(path));
            } else {
                mMediaPlayer.setDataSource(path);
            }
            mMediaPlayer.setSurface(new Surface(mTextureView.getSurfaceTexture()));
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnPreparedListener(mp -> {
                mp.start();

                float ratio = mp.getVideoWidth() * 1f / mp.getVideoHeight();
                int width1 = mTextureView.getWidth();
                ViewGroup.LayoutParams layoutParams = mTextureView.getLayoutParams();
                layoutParams.height = (int) (width1 / ratio);
                mTextureView.setLayoutParams(layoutParams);

                if (onVideoPlayPrepareListener != null) {
                    onVideoPlayPrepareListener.onPrepared();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止视频播放
     */
    private void stopVideoPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mTextureView.setVisibility(View.GONE);
    }

    /**
     * 重置状态
     */
    @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
    private void resetState() {
        if (lifecycleCameraController.isRecording()) {
            lifecycleCameraController.stopRecording();
        }
        FileUtil.deleteFile(getContext(), filePath);
        mPhoto.setVisibility(INVISIBLE);
        mSwitchCamera.setVisibility(VISIBLE);
        mFlashLamp.setVisibility(VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mCaptureLayout.resetCaptureLayout();
    }

    private void setFlashRes() {
        switch (type_flash) {
            case TYPE_FLASH_AUTO:
                mFlashLamp.setImageResource(R.drawable.ic_flash_auto);
                lifecycleCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_AUTO);
                break;
            case TYPE_FLASH_ON:
                mFlashLamp.setImageResource(R.drawable.ic_flash_on);
                lifecycleCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_ON);
                break;
            case TYPE_FLASH_OFF:
                mFlashLamp.setImageResource(R.drawable.ic_flash_off);
                lifecycleCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_OFF);
                break;
        }
    }

    /**
     * 设置录像时loading色值
     *
     * @param color
     */
    public void setProgressColor(int color) {
        mCaptureLayout.setProgressColor(color);
    }

    /**
     * 关闭相机界面按钮
     *
     * @param clickListener
     */
    public void setLeftClickListener(ClickListener clickListener) {
        this.leftClickListener = clickListener;
    }

    public void setFlowCameraListener(FlowCameraListener flowCameraListener) {
        this.flowCameraListener = flowCameraListener;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            Log.d("zdu__", "view 显示");
        } else if (visibility == GONE || visibility == INVISIBLE) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            Log.d("zdu__", "view 隐藏");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }
}

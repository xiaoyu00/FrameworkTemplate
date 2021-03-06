package com.luck.picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.luck.picture.adapter.PictureImageGridAdapter;
import com.luck.picture.animators.AlphaInAnimationAdapter;
import com.luck.picture.animators.AnimationType;
import com.luck.picture.animators.SlideInBottomAnimationAdapter;
import com.luck.picture.basic.FragmentInjectManager;
import com.luck.picture.basic.IPictureSelectorEvent;
import com.luck.picture.basic.PictureCommonFragment;
import com.luck.picture.basic.PictureSelectorSupporterActivity;
import com.luck.picture.config.InjectResourceSource;
import com.luck.picture.config.PictureConfig;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.config.SelectMimeType;
import com.luck.picture.config.SelectModeConfig;
import com.luck.picture.decoration.GridSpacingItemDecoration;
import com.luck.picture.dialog.AlbumListPopWindow;
import com.luck.picture.dialog.AudioPlayDialog;
import com.luck.picture.entity.LocalMedia;
import com.luck.picture.entity.LocalMediaFolder;
import com.luck.picture.interfaces.OnAlbumItemClickListener;
import com.luck.picture.interfaces.OnCallbackListener;
import com.luck.picture.interfaces.OnQueryAlbumListener;
import com.luck.picture.interfaces.OnQueryAllAlbumListener;
import com.luck.picture.interfaces.OnQueryDataResultListener;
import com.luck.picture.interfaces.OnRecyclerViewPreloadMoreListener;
import com.luck.picture.interfaces.OnRecyclerViewScrollListener;
import com.luck.picture.interfaces.OnRecyclerViewScrollStateListener;
import com.luck.picture.lib.R;
import com.luck.picture.loader.LocalMediaLoader;
import com.luck.picture.loader.LocalMediaPageLoader;
import com.luck.picture.magical.BuildRecycleItemViewParams;
import com.luck.picture.manager.SelectedManager;
import com.luck.picture.permissions.PermissionChecker;
import com.luck.picture.permissions.PermissionConfig;
import com.luck.picture.permissions.PermissionResultCallback;
import com.luck.picture.style.PictureSelectorStyle;
import com.luck.picture.style.SelectMainStyle;
import com.luck.picture.utils.ActivityCompatHelper;
import com.luck.picture.utils.AnimUtils;
import com.luck.picture.utils.DateUtils;
import com.luck.picture.utils.DensityUtil;
import com.luck.picture.utils.DoubleUtils;
import com.luck.picture.utils.StyleUtils;
import com.luck.picture.utils.ToastUtils;
import com.luck.picture.utils.ValueOf;
import com.luck.picture.widget.BottomNavBar;
import com.luck.picture.widget.CompleteSelectView;
import com.luck.picture.widget.RecyclerPreloadView;
import com.luck.picture.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author???luck
 * @date???2021/11/17 10:24 ??????
 * @describe???PictureSelectorFragment
 */
public class PictureSelectorFragment extends PictureCommonFragment
        implements OnRecyclerViewPreloadMoreListener, IPictureSelectorEvent {
    public static final String TAG = PictureSelectorFragment.class.getSimpleName();
    /**
     * ????????????????????????R.anim.ps_anim_modal_in?????????
     */
    private static final int SELECT_ANIM_DURATION = 135;
    private RecyclerPreloadView mRecycler;

    private TextView tvDataEmpty;

    private TitleBar titleBar;

    private BottomNavBar bottomNarBar;

    private CompleteSelectView completeSelectView;

    private TextView tvCurrentDataTime;

    private long intervalClickTime = 0;

    /**
     * open camera number
     */
    private int openCameraNumber;

    private int allFolderSize;

    private int currentPosition = -1;

    private boolean isDisplayCamera;

    private PictureImageGridAdapter mAdapter;

    private AlbumListPopWindow albumListPopWindow;

    private boolean isCameraMemoryRecycling;

    public static PictureSelectorFragment newInstance() {
        PictureSelectorFragment fragment = new PictureSelectorFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public int getResourceId() {
        int layoutResourceId = InjectResourceSource.getLayoutResource(getContext(), InjectResourceSource.MAIN_SELECTOR_LAYOUT_RESOURCE);
        if (layoutResourceId != 0) {
            return layoutResourceId;
        }
        return R.layout.ps_fragment_selector;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSelectedChange(boolean isAddRemove, LocalMedia currentMedia) {
        bottomNarBar.setSelectedChange();
        completeSelectView.setSelectedChange(false);
        // ??????????????????
        if (checkNotifyStrategy(isAddRemove)) {
            mAdapter.notifyItemPositionChanged(currentMedia.position);
            mRecycler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            }, SELECT_ANIM_DURATION);
        } else {
            mAdapter.notifyItemPositionChanged(currentMedia.position);
        }
        if (!isAddRemove) {
            sendChangeSubSelectPositionEvent(true);
        }
    }

    @Override
    public void onFixedSelectedChange(LocalMedia oldLocalMedia) {
        mAdapter.notifyItemPositionChanged(oldLocalMedia.position);
    }

    @Override
    public void sendChangeSubSelectPositionEvent(boolean adapterChange) {
        if (PictureSelectionConfig.selectorStyle.getSelectMainStyle().isSelectNumberStyle()) {
            for (int index = 0; index < SelectedManager.getCount(); index++) {
                LocalMedia media = SelectedManager.getSelectedResult().get(index);
                media.setNum(index + 1);
                if (adapterChange) {
                    mAdapter.notifyItemPositionChanged(media.position);
                }
            }
        }
    }

    @Override
    public void onCheckOriginalChange() {
        bottomNarBar.setOriginalCheck();
    }

    /**
     * ??????????????????
     *
     * @param isAddRemove
     * @return
     */
    private boolean checkNotifyStrategy(boolean isAddRemove) {
        boolean isNotifyAll = false;
        if (config.isMaxSelectEnabledMask && config.selectionMode == SelectModeConfig.MULTIPLE) {
            if (config.isWithVideoImage) {
                isNotifyAll = SelectedManager.getCount() == config.maxSelectNum
                        || (!isAddRemove && SelectedManager.getCount() == config.maxSelectNum - 1);
            } else {
                if (SelectedManager.getCount() == 0 || (isAddRemove && SelectedManager.getCount() == 1)) {
                    // ????????????????????????????????????0?????????notifyDataSetChanged
                    isNotifyAll = true;
                } else {
                    if (PictureMimeType.isHasVideo(SelectedManager.getTopResultMimeType())) {
                        int maxSelectNum = config.maxVideoSelectNum > 0
                                ? config.maxVideoSelectNum : config.maxSelectNum;
                        isNotifyAll = SelectedManager.getCount() == maxSelectNum
                                || (!isAddRemove && SelectedManager.getCount() == maxSelectNum - 1);
                    } else {
                        isNotifyAll = SelectedManager.getCount() == config.maxSelectNum
                                || (!isAddRemove && SelectedManager.getCount() == config.maxSelectNum - 1);
                    }
                }
            }
        }
        return isNotifyAll;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PictureConfig.EXTRA_ALL_FOLDER_SIZE, allFolderSize);
        outState.putInt(PictureConfig.EXTRA_CURRENT_PAGE, mPage);
        outState.putInt(PictureConfig.EXTRA_PREVIEW_CURRENT_POSITION, mRecycler.getLastVisiblePosition());
        outState.putBoolean(PictureConfig.EXTRA_DISPLAY_CAMERA, mAdapter.isDisplayCamera());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reStartSavedInstance(savedInstanceState);
        isCameraMemoryRecycling = savedInstanceState != null;
        tvDataEmpty = view.findViewById(R.id.tv_data_empty);
        completeSelectView = view.findViewById(R.id.ps_complete_select);
        titleBar = view.findViewById(R.id.title_bar);
        bottomNarBar = view.findViewById(R.id.bottom_nar_bar);
        tvCurrentDataTime = view.findViewById(R.id.tv_current_data_time);
        initLoader();
        initAlbumListPopWindow();
        initTitleBar();
        initComplete();
        initRecycler(view);
        initBottomNavBar();
        if (isNormalDefaultEnter()) {
            requestLoadData();
        }
    }

    @Override
    public void onEnterFragmentAnimComplete() {
        if (isOtherEnter()) {
            requestLoadData();
        }
    }

    /**
     * ??????PictureSelector ??????????????????
     *
     * @return
     */
    private boolean isNormalDefaultEnter() {
        return getActivity() instanceof PictureSelectorSupporterActivity;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    private boolean isOtherEnter() {
        return !(getActivity() instanceof PictureSelectorSupporterActivity);
    }

    @Override
    public void reStartSavedInstance(Bundle savedInstanceState) {
        super.reStartSavedInstance(savedInstanceState);
        if (savedInstanceState != null) {
            allFolderSize = savedInstanceState.getInt(PictureConfig.EXTRA_ALL_FOLDER_SIZE);
            mPage = savedInstanceState.getInt(PictureConfig.EXTRA_CURRENT_PAGE, mPage);
            currentPosition = savedInstanceState.getInt(PictureConfig.EXTRA_PREVIEW_CURRENT_POSITION, currentPosition);
            isDisplayCamera = savedInstanceState.getBoolean(PictureConfig.EXTRA_DISPLAY_CAMERA, config.isDisplayCamera);
        } else {
            isDisplayCamera = config.isDisplayCamera;
        }
    }

    /**
     * ????????????
     */
    private void initComplete() {
        if (config.selectionMode == SelectModeConfig.SINGLE && config.isDirectReturnSingle) {
            PictureSelectionConfig.selectorStyle.getTitleBarStyle().setHideCancelButton(false);
            titleBar.getTitleCancelView().setVisibility(View.VISIBLE);
            completeSelectView.setVisibility(View.GONE);
        } else {
            completeSelectView.setCompleteSelectViewStyle();
            completeSelectView.setSelectedChange(false);
            SelectMainStyle selectMainStyle = PictureSelectionConfig.selectorStyle.getSelectMainStyle();
            if (selectMainStyle.isCompleteSelectRelativeTop()) {
                if (completeSelectView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                    ((ConstraintLayout.LayoutParams)
                            completeSelectView.getLayoutParams()).topToTop = R.id.title_bar;
                    ((ConstraintLayout.LayoutParams)
                            completeSelectView.getLayoutParams()).bottomToBottom = R.id.title_bar;
                    if (config.isPreviewFullScreenMode) {
                        ((ConstraintLayout.LayoutParams) completeSelectView
                                .getLayoutParams()).topMargin = DensityUtil.getStatusBarHeight(getContext());
                    }
                } else if (completeSelectView.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                    if (config.isPreviewFullScreenMode) {
                        ((RelativeLayout.LayoutParams) completeSelectView
                                .getLayoutParams()).topMargin = DensityUtil.getStatusBarHeight(getContext());
                    }
                }
            }
            completeSelectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTransformResult();
                }
            });
        }
    }

    /**
     * init LocalMedia Loader
     */
    protected void initLoader() {
        if (config.isPageStrategy) {
            mLoader = new LocalMediaPageLoader(getContext(), config);
        } else {
            mLoader = new LocalMediaLoader(getContext(), config);
        }
    }

    private void initTitleBar() {
        if (PictureSelectionConfig.selectorStyle.getTitleBarStyle().isHideTitleBar()) {
            titleBar.setVisibility(View.GONE);
        }
        titleBar.setTitleBarStyle();
        titleBar.setOnTitleBarListener(new TitleBar.OnTitleBarListener() {
            @Override
            public void onTitleDoubleClick() {
                if (config.isAutomaticTitleRecyclerTop) {
                    int intervalTime = 500;
                    if (SystemClock.uptimeMillis() - intervalClickTime < intervalTime && mAdapter.getItemCount() > 0) {
                        mRecycler.scrollToPosition(0);
                    } else {
                        intervalClickTime = SystemClock.uptimeMillis();
                    }
                }
            }

            @Override
            public void onBackPressed() {
                if (albumListPopWindow.isShowing()) {
                    albumListPopWindow.dismiss();
                } else {
                    if (ActivityCompatHelper.checkRootFragment(getActivity())) {
                        if (PictureSelectionConfig.onResultCallListener != null) {
                            PictureSelectionConfig.onResultCallListener.onCancel();
                        }
                    }
                    SelectorResult result = getResult(Activity.RESULT_CANCELED, new ArrayList<>());
                    if (!ActivityCompatHelper.isDestroy(getActivity())) {
                        getActivity().setResult(result.mResultCode, result.mResultData);
                    }
                    iBridgePictureBehavior.onSelectFinish(false, result);
                }
            }

            @Override
            public void onShowAlbumPopWindow(View anchor) {
                albumListPopWindow.showAsDropDown(anchor);
            }
        });
    }

    /**
     * initAlbumListPopWindow
     */
    private void initAlbumListPopWindow() {
        albumListPopWindow = AlbumListPopWindow.buildPopWindow(getContext());
        albumListPopWindow.setOnPopupWindowStatusListener(new AlbumListPopWindow.OnPopupWindowStatusListener() {
            @Override
            public void onShowPopupWindow() {
                if (!config.isOnlySandboxDir) {
                    AnimUtils.rotateArrow(titleBar.getImageArrow(), true);
                }
            }

            @Override
            public void onDismissPopupWindow() {
                if (!config.isOnlySandboxDir) {
                    AnimUtils.rotateArrow(titleBar.getImageArrow(), false);
                }
            }
        });
        addAlbumPopWindowAction();
    }

    private void requestLoadData() {
        mAdapter.setDisplayCamera(isDisplayCamera);
        if (PermissionChecker.isCheckReadStorage(getContext())) {
            beginLoadData();
        } else {
            if (PictureSelectionConfig.onPermissionsEventListener != null) {
                PictureSelectionConfig.onPermissionsEventListener.requestPermission(this,
                        PermissionConfig.READ_WRITE_EXTERNAL_STORAGE, new OnCallbackListener<Boolean>() {
                            @Override
                            public void onCall(Boolean isResult) {
                                if (isResult) {
                                    beginLoadData();
                                } else {
                                    handlePermissionDenied(PermissionConfig.READ_WRITE_EXTERNAL_STORAGE);
                                }
                            }
                        });
            } else {
                PermissionChecker.getInstance().requestPermissions(this,
                        PermissionConfig.READ_WRITE_EXTERNAL_STORAGE, new PermissionResultCallback() {
                            @Override
                            public void onGranted() {
                                beginLoadData();
                            }

                            @Override
                            public void onDenied() {
                                handlePermissionDenied(PermissionConfig.READ_WRITE_EXTERNAL_STORAGE);
                            }
                        });
            }
        }
    }

    /**
     * ??????????????????
     */
    private void beginLoadData() {
        showLoading();
        if (config.isOnlySandboxDir) {
            loadOnlyInAppDirectoryAllMediaData();
        } else {
            loadAllAlbumData();
        }
    }

    @Override
    public void handlePermissionSettingResult() {
        boolean isHasPermissions;
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            isHasPermissions = PictureSelectionConfig.onPermissionsEventListener.hasPermissions(this);
        } else {
            isHasPermissions = PermissionChecker.isCheckReadStorage(getContext());
        }
        if (isHasPermissions) {
            beginLoadData();
        } else {
            ToastUtils.showToast(getContext(), getString(R.string.ps_jurisdiction));
            iBridgePictureBehavior.onSelectFinish(false, null);
        }
    }

    /**
     * ???AlbumListPopWindow????????????
     */
    private void addAlbumPopWindowAction() {
        albumListPopWindow.setOnIBridgeAlbumWidget(new OnAlbumItemClickListener() {

            @Override
            public void onItemClick(int position, LocalMediaFolder curFolder) {
                isDisplayCamera = config.isDisplayCamera && curFolder.getBucketId() == PictureConfig.ALL;
                mAdapter.setDisplayCamera(isDisplayCamera);
                titleBar.setTitle(curFolder.getFolderName());
                LocalMediaFolder lastFolder = SelectedManager.getCurrentLocalMediaFolder();
                long lastBucketId = lastFolder.getBucketId();
                if (config.isPageStrategy) {
                    if (curFolder.getBucketId() != lastBucketId) {
                        // 1??????????????????????????????????????????????????????????????????????????????????????????
                        lastFolder.setData(mAdapter.getData());
                        lastFolder.setCurrentDataPage(mPage);
                        lastFolder.setHasMore(mRecycler.isEnabledLoadMore());

                        // 2???????????????????????????????????????????????????????????????MediaStore????????????
                        if (curFolder.getData().size() > 0) {
                            setAdapterData(curFolder.getData());
                            mPage = curFolder.getCurrentDataPage();
                            mRecycler.setEnabledLoadMore(curFolder.isHasMore());
                            mRecycler.smoothScrollToPosition(0);
                        } else {
                            // 3??????MediaStore????????????
                            mPage = 1;
                            showLoading();
                            if (PictureSelectionConfig.loaderDataEngine != null) {
                                PictureSelectionConfig.loaderDataEngine.loadFirstPageMediaData(getContext(),
                                        curFolder.getBucketId(), mPage, config.pageSize,
                                        new OnQueryDataResultListener<LocalMedia>() {
                                            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                                                handleSwitchAlbum(result, isHasMore);
                                            }
                                        });
                            } else {
                                mLoader.loadPageMediaData(curFolder.getBucketId(), mPage, config.pageSize,
                                        new OnQueryDataResultListener<LocalMedia>() {
                                            @Override
                                            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                                                handleSwitchAlbum(result, isHasMore);
                                            }
                                        });
                            }
                        }
                    }
                } else {
                    // ??????????????????????????????????????????????????????
                    if (curFolder.getBucketId() != lastBucketId) {
                        setAdapterData(curFolder.getData());
                        mRecycler.smoothScrollToPosition(0);
                    }
                }
                SelectedManager.setCurrentLocalMediaFolder(curFolder);
                albumListPopWindow.dismiss();
            }
        });
    }

    private void handleSwitchAlbum(ArrayList<LocalMedia> result, boolean isHasMore) {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return;
        }
        dismissLoading();
        mRecycler.setEnabledLoadMore(isHasMore);
        if (result.size() == 0) {
            // ?????????MediaStore???????????????????????????adapter????????????????????????????????????
            mAdapter.getData().clear();
        }
        setAdapterData(result);
        mRecycler.onScrolled(0, 0);
        mRecycler.smoothScrollToPosition(0);
    }


    private void initBottomNavBar() {
        bottomNarBar.setBottomNavBarStyle();
        bottomNarBar.setOnBottomNavBarListener(new BottomNavBar.OnBottomNavBarListener() {
            @Override
            public void onPreview() {
                onStartPreview(0, true);
            }

            @Override
            public void onCheckOriginalChange() {
                sendSelectedOriginalChangeEvent();
            }
        });
        bottomNarBar.setSelectedChange();
    }


    @Override
    public void loadAllAlbumData() {
        if (PictureSelectionConfig.loaderDataEngine != null) {
            PictureSelectionConfig.loaderDataEngine.loadAllAlbumData(getContext(),
                    new OnQueryAllAlbumListener<LocalMediaFolder>() {
                        @Override
                        public void onComplete(List<LocalMediaFolder> result) {
                            handleAllAlbumData(result);
                        }
                    });
        } else {
            mLoader.loadAllMedia(new OnQueryAllAlbumListener<LocalMediaFolder>() {

                @Override
                public void onComplete(List<LocalMediaFolder> result) {
                    handleAllAlbumData(result);
                }
            });
        }
    }

    private void handleAllAlbumData(List<LocalMediaFolder> result) {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return;
        }
        if (result.size() > 0) {
            LocalMediaFolder firstFolder;
            if (SelectedManager.getCurrentLocalMediaFolder() != null) {
                firstFolder = SelectedManager.getCurrentLocalMediaFolder();
            } else {
                firstFolder = result.get(0);
                SelectedManager.setCurrentLocalMediaFolder(firstFolder);
            }
            titleBar.setTitle(firstFolder.getFolderName());
            albumListPopWindow.bindAlbumData(result);
            if (config.isPageStrategy) {
                loadFirstPageMediaData(firstFolder.getBucketId());
            } else {
                dismissLoading();
                setAdapterData(firstFolder.getData());
            }
        } else {
            showDataNull();
        }
    }

    @Override
    public void loadFirstPageMediaData(long firstBucketId) {
        mRecycler.setEnabledLoadMore(true);
        if (PictureSelectionConfig.loaderDataEngine != null) {
            PictureSelectionConfig.loaderDataEngine.loadFirstPageMediaData(getContext(), firstBucketId,
                    mPage, mPage * config.pageSize, new OnQueryDataResultListener<LocalMedia>() {

                        @Override
                        public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                            handleFirstPageMedia(result, isHasMore);
                        }
                    });
        } else {
            mLoader.loadFirstPageMedia(firstBucketId, mPage * config.pageSize,
                    new OnQueryDataResultListener<LocalMedia>() {
                        @Override
                        public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                            handleFirstPageMedia(result, isHasMore);
                        }
                    });
        }
    }

    private void handleFirstPageMedia(ArrayList<LocalMedia> result, boolean isHasMore) {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return;
        }
        dismissLoading();
        mRecycler.setEnabledLoadMore(isHasMore);
        if (mRecycler.isEnabledLoadMore() && result.size() == 0) {
            // ??????isHasMore???true???result.size() = 0;
            // ????????????????????????????????????????????????????????????????????????????????????????????????
            onRecyclerViewPreloadMore();
        } else {
            setAdapterData(result);
        }
        recoveryRecyclerPosition();
    }

    @Override
    public void loadOnlyInAppDirectoryAllMediaData() {
        if (PictureSelectionConfig.loaderDataEngine != null) {
            PictureSelectionConfig.loaderDataEngine.loadOnlyInAppDirAllMediaData(getContext(),
                    new OnQueryAlbumListener<LocalMediaFolder>() {
                        @Override
                        public void onComplete(LocalMediaFolder folder) {
                            dismissLoading();
                            handleInAppDirAllMedia(folder);
                        }
                    });
        } else {
            mLoader.loadOnlyInAppDirAllMedia(new OnQueryAlbumListener<LocalMediaFolder>() {
                @Override
                public void onComplete(LocalMediaFolder folder) {
                    dismissLoading();
                    handleInAppDirAllMedia(folder);
                }
            });
        }
    }

    private void handleInAppDirAllMedia(LocalMediaFolder folder) {
        if (!ActivityCompatHelper.isDestroy(getActivity())) {
            String sandboxDir = config.sandboxDir;
            boolean isNonNull = folder != null;
            String folderName = isNonNull ? folder.getFolderName() : new File(sandboxDir).getName();
            titleBar.setTitle(folderName);
            if (isNonNull) {
                SelectedManager.setCurrentLocalMediaFolder(folder);
                setAdapterData(folder.getData());
                recoveryRecyclerPosition();
            } else {
                showDataNull();
            }
        }
    }

    /**
     * ????????????????????????RecyclerView????????????
     */
    private void recoveryRecyclerPosition() {
        if (currentPosition > 0) {
            mRecycler.post(new Runnable() {
                @Override
                public void run() {
                    mRecycler.scrollToPosition(currentPosition);
                    mRecycler.setLastVisiblePosition(currentPosition);
                }
            });
        }
    }


    private void initRecycler(View view) {
        mRecycler = view.findViewById(R.id.recycler);
        PictureSelectorStyle selectorStyle = PictureSelectionConfig.selectorStyle;
        SelectMainStyle selectMainStyle = selectorStyle.getSelectMainStyle();
        int listBackgroundColor = selectMainStyle.getMainListBackgroundColor();
        if (StyleUtils.checkStyleValidity(listBackgroundColor)) {
            mRecycler.setBackgroundColor(listBackgroundColor);
        } else {
            mRecycler.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ps_color_black));
        }
        int imageSpanCount = config.imageSpanCount <= 0 ? PictureConfig.DEFAULT_SPAN_COUNT : config.imageSpanCount;
        if (StyleUtils.checkSizeValidity(selectMainStyle.getAdapterItemSpacingSize())) {
            mRecycler.addItemDecoration(new GridSpacingItemDecoration(imageSpanCount,
                    selectMainStyle.getAdapterItemSpacingSize(), selectMainStyle.isAdapterItemIncludeEdge()));
        } else {
            mRecycler.addItemDecoration(new GridSpacingItemDecoration(imageSpanCount,
                    DensityUtil.dip2px(view.getContext(), 1), selectMainStyle.isAdapterItemIncludeEdge()));
        }
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), imageSpanCount));
        RecyclerView.ItemAnimator itemAnimator = mRecycler.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            mRecycler.setItemAnimator(null);
        }
        if (config.isPageStrategy) {
            mRecycler.setReachBottomRow(RecyclerPreloadView.BOTTOM_PRELOAD);
            mRecycler.setOnRecyclerViewPreloadListener(this);
        } else {
            mRecycler.setHasFixedSize(true);
        }
        mAdapter = new PictureImageGridAdapter(getContext(), config);
        mAdapter.setDisplayCamera(isDisplayCamera);
        switch (config.animationMode) {
            case AnimationType.ALPHA_IN_ANIMATION:
                mRecycler.setAdapter(new AlphaInAnimationAdapter(mAdapter));
                break;
            case AnimationType.SLIDE_IN_BOTTOM_ANIMATION:
                mRecycler.setAdapter(new SlideInBottomAnimationAdapter(mAdapter));
                break;
            default:
                mRecycler.setAdapter(mAdapter);
                break;
        }
        addRecyclerAction();
    }

    private void addRecyclerAction() {
        mAdapter.setOnItemClickListener(new PictureImageGridAdapter.OnItemClickListener() {

            @Override
            public void openCameraClick() {
                openSelectedCamera();
            }

            @Override
            public int onSelected(View selectedView, int position, LocalMedia media) {
                int selectResultCode = confirmSelect(media, selectedView.isSelected());
                if (selectResultCode == SelectedManager.ADD_SUCCESS) {
                    selectedView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ps_anim_modal_in));
                }
                return selectResultCode;
            }

            @Override
            public void onItemClick(View selectedView, int position, LocalMedia media) {
                if (config.selectionMode == SelectModeConfig.SINGLE && config.isDirectReturnSingle) {
                    SelectedManager.getSelectedResult().clear();
                    SelectedManager.getSelectedResult().add(media);
                    dispatchTransformResult();
                } else {
                    if (DoubleUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (PictureMimeType.isHasAudio(media.getMimeType())) {
                        if (PictureSelectionConfig.onPreviewInterceptListener != null) {
                            PictureSelectionConfig.onPreviewInterceptListener.onPreviewAudio(getContext(), media);
                        } else {
                            AudioPlayDialog.showPlayAudioDialog(getActivity(), media.getPath());
                        }
                    } else {
                        onStartPreview(position, false);
                    }
                }
            }
        });

        mRecycler.setOnRecyclerViewScrollStateListener(new OnRecyclerViewScrollStateListener() {
            @Override
            public void onScrollFast() {
                if (PictureSelectionConfig.imageEngine != null) {
                    PictureSelectionConfig.imageEngine.pauseRequests(getContext());
                }
            }

            @Override
            public void onScrollSlow() {
                if (PictureSelectionConfig.imageEngine != null) {
                    PictureSelectionConfig.imageEngine.resumeRequests(getContext());
                }
            }
        });
        mRecycler.setOnRecyclerViewScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrolled(int dx, int dy) {
                setCurrentMediaCreateTimeText();
            }

            @Override
            public void onScrollStateChanged(int state) {
                if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
                    showCurrentMediaCreateTimeUI();
                } else if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    hideCurrentMediaCreateTimeUI();
                }
            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void setCurrentMediaCreateTimeText() {
        if (config.isDisplayTimeAxis) {
            int position = mRecycler.getFirstVisiblePosition();
            if (position != RecyclerView.NO_POSITION) {
                ArrayList<LocalMedia> data = mAdapter.getData();
                if (data.size() > position && data.get(position).getDateAddedTime() > 0) {
                    tvCurrentDataTime.setText(DateUtils.getDataFormat(getContext(),
                            data.get(position).getDateAddedTime()));
                }
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private void showCurrentMediaCreateTimeUI() {
        if (config.isDisplayTimeAxis && mAdapter.getData().size() > 0) {
            if (tvCurrentDataTime.getAlpha() == 0F) {
                tvCurrentDataTime.animate().setDuration(150).alphaBy(1.0F).start();
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private void hideCurrentMediaCreateTimeUI() {
        if (config.isDisplayTimeAxis && mAdapter.getData().size() > 0) {
            tvCurrentDataTime.animate().setDuration(250).alpha(0.0F).start();
        }
    }

    /**
     * ????????????
     *
     * @param position        ??????????????????
     * @param isBottomPreview true ?????????????????? false??????????????????
     */
    private void onStartPreview(int position, boolean isBottomPreview) {
        if (ActivityCompatHelper.checkFragmentNonExits(getActivity(), PictureSelectorPreviewFragment.TAG)) {
            ArrayList<LocalMedia> data;
            int totalNum;
            long currentBucketId = 0;
            if (isBottomPreview) {
                data = new ArrayList<>(SelectedManager.getSelectedResult());
                totalNum = data.size();
            } else {
                data = mAdapter.getData();
                totalNum = SelectedManager.getCurrentLocalMediaFolder().getFolderTotalNum();
                currentBucketId = SelectedManager.getCurrentLocalMediaFolder().getBucketId();
            }
            if (!isBottomPreview && config.isPreviewZoomEffect) {
                BuildRecycleItemViewParams.generateViewParams(mRecycler,
                        config.isPreviewFullScreenMode ? 0 : DensityUtil.getStatusBarHeight(getContext()));
            }
            if (PictureSelectionConfig.onPreviewInterceptListener != null) {
                PictureSelectionConfig.onPreviewInterceptListener
                        .onPreview(getContext(), position, totalNum, mPage, currentBucketId, titleBar.getTitleText(),
                                mAdapter.isDisplayCamera(), data, isBottomPreview);
            } else {
                if (ActivityCompatHelper.checkFragmentNonExits(getActivity(), PictureSelectorPreviewFragment.TAG)) {
                    PictureSelectorPreviewFragment previewFragment = PictureSelectorPreviewFragment.newInstance();
                    previewFragment.setInternalPreviewData(isBottomPreview, titleBar.getTitleText(), mAdapter.isDisplayCamera(),
                            position, totalNum, mPage, currentBucketId, data);
                    FragmentInjectManager.injectFragment(getActivity(), PictureSelectorPreviewFragment.TAG, previewFragment);
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapterData(ArrayList<LocalMedia> result) {
        sendChangeSubSelectPositionEvent(false);
        mAdapter.setDataAndDataSetChanged(result);
        if (mAdapter.isDataEmpty()) {
            showDataNull();
        } else {
            hideDataNull();
        }
    }

    @Override
    public void onRecyclerViewPreloadMore() {
        loadMoreMediaData();
    }

    /**
     * ????????????
     */
    @Override
    public void loadMoreMediaData() {
        if (mRecycler.isEnabledLoadMore()) {
            mPage++;
            LocalMediaFolder localMediaFolder = SelectedManager.getCurrentLocalMediaFolder();
            long bucketId = localMediaFolder != null ? localMediaFolder.getBucketId() : 0;
            if (PictureSelectionConfig.loaderDataEngine != null) {
                PictureSelectionConfig.loaderDataEngine.loadMoreMediaData(getContext(), bucketId, mPage,
                        getPageLimit(bucketId), config.pageSize, new OnQueryDataResultListener<LocalMedia>() {
                            @Override
                            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                                handleMoreMediaData(result, isHasMore);
                            }
                        });
            } else {
                mLoader.loadPageMediaData(bucketId, mPage, getPageLimit(bucketId), config.pageSize,
                        new OnQueryDataResultListener<LocalMedia>() {
                            @Override
                            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                                handleMoreMediaData(result, isHasMore);
                            }
                        });
            }
        }
    }

    private void handleMoreMediaData(List<LocalMedia> result, boolean isHasMore) {
        if (ActivityCompatHelper.isDestroy(getActivity())) {
            return;
        }
        mRecycler.setEnabledLoadMore(isHasMore);
        if (mRecycler.isEnabledLoadMore()) {
            if (result.size() > 0) {
                int positionStart = mAdapter.getData().size();
                mAdapter.getData().addAll(result);
                mAdapter.notifyItemRangeChanged(positionStart, mAdapter.getItemCount());
            } else {
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????0?????????
                onRecyclerViewPreloadMore();
            }
            if (result.size() < PictureConfig.MIN_PAGE_SIZE) {
                // ????????????????????????????????????????????????????????????????????????????????????????????????
                mRecycler.onScrolled(mRecycler.getScrollX(), mRecycler.getScrollY());
            }
        }
    }


    @Override
    public void dispatchCameraMediaResult(LocalMedia media) {
        if (isCameraMemoryRecycling) {
            isCameraMemoryRecycling = false;
            // ?????????????????????????????????????????????????????????Fragment???????????????????????????loadAllData???????????????????????????????????????
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
            SelectedManager.getSelectedResult().add(media);
            mAdapter.notifyItemPositionChanged(config.isDisplayCamera ? 1 : 0);
            if (config.isDirectReturnSingle) {
                dispatchTransformResult();
            }
            return;
        }
        int exitsTotalNum = albumListPopWindow.getFirstAlbumImageCount();
        if (!isAddSameImp(exitsTotalNum)) {
            mAdapter.getData().add(0, media);
            openCameraNumber++;
        }
        if (config.selectionMode == SelectModeConfig.SINGLE && config.isDirectReturnSingle) {
            SelectedManager.getSelectedResult().clear();
            SelectedManager.getSelectedResult().add(media);
            dispatchTransformResult();
        } else {
            confirmSelect(media, false);
        }
        mAdapter.notifyItemInserted(config.isDisplayCamera ? 1 : 0);
        mAdapter.notifyItemRangeChanged(config.isDisplayCamera ? 1 : 0, mAdapter.getData().size());
        if (config.isOnlySandboxDir) {
            LocalMediaFolder currentLocalMediaFolder = SelectedManager.getCurrentLocalMediaFolder();
            if (currentLocalMediaFolder == null) {
                currentLocalMediaFolder = new LocalMediaFolder();
                long bucketId = ValueOf.toLong(media.getParentFolderName().hashCode());
                currentLocalMediaFolder.setBucketId(bucketId);
                currentLocalMediaFolder.setFolderName(media.getParentFolderName());
                currentLocalMediaFolder.setFirstMimeType(media.getMimeType());
                currentLocalMediaFolder.setFirstImagePath(media.getPath());
                currentLocalMediaFolder.setFolderTotalNum(mAdapter.getData().size());
                currentLocalMediaFolder.setCurrentDataPage(mPage);
                currentLocalMediaFolder.setHasMore(false);
                mRecycler.setEnabledLoadMore(false);
                SelectedManager.setCurrentLocalMediaFolder(currentLocalMediaFolder);
            }
        } else {
            mergeFolder(media);
        }
        allFolderSize = 0;
        if (mAdapter.getData().size() > 0 || config.isDirectReturnSingle) {
            hideDataNull();
        } else {
            showDataNull();
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param media
     */
    private void mergeFolder(LocalMedia media) {
        LocalMediaFolder allFolder;
        if (albumListPopWindow.getFolderCount() == 0) {
            // 1????????????????????????????????????????????????
            allFolder = new LocalMediaFolder();
            String folderName = config.chooseMode == SelectMimeType.ofAudio()
                    ? getString(R.string.ps_all_audio) : getString(R.string.ps_camera_roll);
            allFolder.setFolderName(folderName);
            allFolder.setFirstImagePath("");
            allFolder.setBucketId(PictureConfig.ALL);
            albumListPopWindow.getAlbumList().add(0, allFolder);
        } else {
            // 2??????????????????????????????????????????????????????
            allFolder = albumListPopWindow.getFolder(0);
        }
        allFolder.setFirstImagePath(media.getPath());
        allFolder.setFirstMimeType(media.getMimeType());
        allFolder.setData(mAdapter.getData());
        allFolder.setBucketId(PictureConfig.ALL);
        allFolder.setFolderTotalNum(isAddSameImp(allFolder.getFolderTotalNum()) ? allFolder.getFolderTotalNum() : allFolder.getFolderTotalNum() + 1);
        if (SelectedManager.getCurrentLocalMediaFolder() == null) {
            SelectedManager.setCurrentLocalMediaFolder(allFolder);
        }
        // ?????????Camera????????????????????????????????????Camera??????
        LocalMediaFolder cameraFolder = null;
        List<LocalMediaFolder> albumList = albumListPopWindow.getAlbumList();
        for (int i = 0; i < albumList.size(); i++) {
            LocalMediaFolder exitsFolder = albumList.get(i);
            if (TextUtils.equals(exitsFolder.getFolderName(), media.getParentFolderName())) {
                cameraFolder = exitsFolder;
                break;
            }
        }
        if (cameraFolder == null) {
            // ????????????????????????????????????
            cameraFolder = new LocalMediaFolder();
            cameraFolder.setFolderName(media.getParentFolderName());
            cameraFolder.setBucketId(media.getBucketId());
            // ????????????????????????????????????
            if (!TextUtils.isEmpty(config.outPutCameraDir) || !TextUtils.isEmpty(config.outPutAudioDir)) {
                cameraFolder.getData().add(0, media);
            }
            albumList.add(cameraFolder);
        } else {
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (!config.isPageStrategy && !isAddSameImp(allFolder.getFolderTotalNum()) ||
                    !TextUtils.isEmpty(config.outPutCameraDir) || !TextUtils.isEmpty(config.outPutAudioDir)) {
                cameraFolder.getData().add(0, media);
            }
            if (cameraFolder.getBucketId() == -1 || cameraFolder.getBucketId() == 0) {
                cameraFolder.setBucketId(media.getBucketId());
            }
        }
        cameraFolder.setFolderTotalNum(isAddSameImp(allFolder.getFolderTotalNum())
                ? cameraFolder.getFolderTotalNum() : cameraFolder.getFolderTotalNum() + 1);
        cameraFolder.setFirstImagePath(config.cameraPath);
        cameraFolder.setFirstMimeType(media.getMimeType());
        albumListPopWindow.bindAlbumData(albumList);
    }

    /**
     * ??????????????????
     */
    private boolean isAddSameImp(int totalNum) {
        if (totalNum == 0) {
            return false;
        }
        return allFolderSize > 0 && allFolderSize < totalNum;
    }

    /**
     * ??????Limit
     * ????????????????????????????????????????????????????????????
     *
     * @return
     */
    private int getPageLimit(long bucketId) {
        if (bucketId == -1) {
            int limit = openCameraNumber > 0 ? config.pageSize - openCameraNumber : config.pageSize;
            openCameraNumber = 0;
            return limit;
        }
        return config.pageSize;
    }

    /**
     * ????????????????????????
     */
    private void showDataNull() {
        dismissLoading();
        if (tvDataEmpty.getVisibility() == View.GONE) {
            tvDataEmpty.setVisibility(View.VISIBLE);
        }
        tvDataEmpty.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ps_ic_no_data, 0, 0);
        int chooseMode = config.chooseMode;
        String tips = chooseMode == SelectMimeType.ofAudio()
                ? getString(R.string.ps_audio_empty) : getString(R.string.ps_empty);
        tvDataEmpty.setText(tips);
    }

    /**
     * ????????????????????????
     */
    private void hideDataNull() {
        if (tvDataEmpty.getVisibility() == View.VISIBLE) {
            tvDataEmpty.setVisibility(View.GONE);
        }
    }
}

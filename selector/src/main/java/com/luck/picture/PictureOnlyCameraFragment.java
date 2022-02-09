package com.luck.picture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luck.picture.basic.PictureCommonFragment;
import com.luck.picture.config.PictureConfig;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.entity.LocalMedia;
import com.luck.picture.lib.R;
import com.luck.picture.manager.SelectedManager;
import com.luck.picture.permissions.PermissionChecker;
import com.luck.picture.permissions.PermissionConfig;
import com.luck.picture.permissions.PermissionResultCallback;
import com.luck.picture.utils.ActivityCompatHelper;
import com.luck.picture.utils.SdkVersionUtils;
import com.luck.picture.utils.ToastUtils;

/**
 * @author：luck
 * @date：2021/11/22 2:26 下午
 * @describe：PictureOnlyCameraFragment
 */
public class PictureOnlyCameraFragment extends PictureCommonFragment {
    public static final String TAG = PictureOnlyCameraFragment.class.getSimpleName();

    public static PictureOnlyCameraFragment newInstance() {
        return new PictureOnlyCameraFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.ps_empty;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (SdkVersionUtils.isQ()) {
            openSelectedCamera();
        } else {
            PermissionChecker.getInstance().requestPermissions(this,
                    PermissionConfig.WRITE_EXTERNAL_STORAGE, new PermissionResultCallback() {
                        @Override
                        public void onGranted() {
                            openSelectedCamera();
                        }

                        @Override
                        public void onDenied() {
                            handlePermissionDenied(PermissionConfig.WRITE_EXTERNAL_STORAGE);
                        }
                    });
        }
    }

    @Override
    public void dispatchCameraMediaResult(LocalMedia media) {
        SelectedManager.getSelectedResult().add(media);
        dispatchTransformResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == PictureConfig.REQUEST_CAMERA) {
                if (!ActivityCompatHelper.isDestroy(getActivity())) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }
    }

    @Override
    public void handlePermissionSettingResult() {
        boolean isHasPermissions;
        if (PictureSelectionConfig.onPermissionsEventListener != null) {
            isHasPermissions = PictureSelectionConfig.onPermissionsEventListener.hasPermissions(this);
        } else {
            isHasPermissions = PermissionChecker.isCheckCamera(getContext());
            if (SdkVersionUtils.isQ()) {
            } else {
                isHasPermissions = PermissionChecker.isCheckWriteStorage(getContext());
            }
        }
        if (isHasPermissions) {
            openSelectedCamera();
        } else {
            if (!PermissionChecker.isCheckCamera(getContext())) {
                ToastUtils.showToast(getContext(),getString(R.string.ps_camera));
            } else if (!PermissionChecker.isCheckWriteStorage(getContext())) {
                ToastUtils.showToast(getContext(),getString(R.string.ps_jurisdiction));
            }
            if (!ActivityCompatHelper.isDestroy(getActivity())) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}

package com.luck.picture.basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luck.picture.config.PictureConfig;
import com.luck.picture.entity.LocalMedia;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author：luck
 * @date：2017-5-24 22:30
 * @describe：PictureSelector
 */

public final class PictureSelector {

    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;

    private PictureSelector(Activity activity) {
        this(activity, null);
    }

    private PictureSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private PictureSelector(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    /**
     * Start PictureSelector for context.
     *
     * @param context
     * @return PictureSelector instance.
     */
    public static PictureSelector create(Context context) {
        return new PictureSelector((Activity) context);
    }

    /**
     * Start PictureSelector for Activity.
     *
     * @param activity
     * @return PictureSelector instance.
     */
    public static PictureSelector create(Activity activity) {
        return new PictureSelector(activity);
    }

    /**
     * Start PictureSelector for Fragment.
     *
     * @param fragment
     * @return PictureSelector instance.
     */
    public static PictureSelector create(Fragment fragment) {
        return new PictureSelector(fragment);
    }

    /**
     * @param chooseMode Select the type of picture you want，all or Picture or Video .
     * @return LocalMedia PictureSelectionModel
     * Use {@link com.luck.picture.config.SelectMimeType}
     */
    public PictureSelectionModel openGallery(int chooseMode) {
        return new PictureSelectionModel(this, chooseMode);
    }

    /**
     * @param chooseMode Select the type of picture you want，Picture or Video.
     * @return LocalMedia PictureSelectionModel
     * Use {@link com.luck.picture.config.SelectMimeType}
     */
    public PictureSelectionModel openCamera(int chooseMode) {
        return new PictureSelectionModel(this, chooseMode, true);
    }

    /**
     * preview data
     *
     * @return
     */
    public PictureSelectionModel openPreview() {
        return new PictureSelectionModel(this);
    }

    /**
     * set result
     *
     * @param data result
     * @return
     */
    public static Intent putIntentResult(ArrayList<LocalMedia> data) {
        return new Intent().putParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION, data);
    }

    /**
     * @param intent
     * @return get Selector  LocalMedia
     */
    public static ArrayList<LocalMedia> obtainSelectorList(Intent intent) {
        if (intent == null) {
            return new ArrayList<>();
        }
        ArrayList<LocalMedia> result = intent.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
        return result != null ? result : new ArrayList<>();
    }

    /**
     * @return Activity.
     */
    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    /**
     * @return Fragment.
     */
    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }

}

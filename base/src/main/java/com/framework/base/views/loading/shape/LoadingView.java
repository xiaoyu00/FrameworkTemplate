package com.framework.base.views.loading.shape;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.base.R;

/**
 * @Description 自定义loadingView
 * @Author 一花一世界
 */
public class LoadingView extends FrameLayout {

    private static final int ANIMATION_DURATION = 500;
    private static float mDistance = 200;
    public float factor = 1.2f;

    private ShapeLoadingView mShapeLoadingView;
    private ImageView mIndicationIm;
    private TextView mLoadTextView;
    private int mTextAppearance;

    private String mLoadText;
    private AnimatorSet mUpAnimatorSet;
    private AnimatorSet mDownAnimatorSet;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mLoadText = typedArray.getString(R.styleable.LoadingView_loadingText);
        mTextAppearance = typedArray.getResourceId(R.styleable.LoadingView_loadingTextAppearance, -1);

        typedArray.recycle();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.load_view, null);
        mDistance = dip2px(54f);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mShapeLoadingView = (ShapeLoadingView) view.findViewById(R.id.shapeLoadingView);
        mIndicationIm = (ImageView) view.findViewById(R.id.indication);
        mLoadTextView = (TextView) view.findViewById(R.id.promptTV);

        if (mTextAppearance != -1) {
            mLoadTextView.setTextAppearance(getContext(), mTextAppearance);
        }
        setLoadingText(mLoadText);
        addView(view, layoutParams);
        startLoading(900);
    }

    private AnimatorSet mAnimatorSet = null;

    private Runnable mFreeFallRunnable = new Runnable() {
        @Override
        public void run() {
            freeFall();
        }
    };

    private void startLoading(long delay) {
        if (mDownAnimatorSet != null && mDownAnimatorSet.isRunning()) {
            return;
        }
        this.removeCallbacks(mFreeFallRunnable);
        if (delay > 0) {
            this.postDelayed(mFreeFallRunnable, delay);
        } else {
            this.post(mFreeFallRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    private void stopLoading() {
        if (mAnimatorSet != null) {
            if (mAnimatorSet.isRunning()) {
                mAnimatorSet.cancel();
            }
            mAnimatorSet = null;
        }
        if (mUpAnimatorSet != null) {
            if (mUpAnimatorSet.isRunning()) {
                mUpAnimatorSet.cancel();
            }
            mUpAnimatorSet.removeAllListeners();
            for (Animator animator : mUpAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
        }
        if (mDownAnimatorSet != null) {
            if (mDownAnimatorSet.isRunning()) {
                mDownAnimatorSet.cancel();
            }
            mDownAnimatorSet.removeAllListeners();
            for (Animator animator : mDownAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
        }
        this.removeCallbacks(mFreeFallRunnable);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startLoading(200);
        } else {
            stopLoading();
        }
    }

    public void setLoadingText(CharSequence loadingText) {
        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView.setVisibility(GONE);
        } else {
            mLoadTextView.setVisibility(VISIBLE);
        }
        mLoadTextView.setText(loadingText);
    }

    /**
     * 上抛
     */
    public void upThrow() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", mDistance, 0);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1);

        ObjectAnimator objectAnimator1 = null;
        switch (mShapeLoadingView.getShape()) {
            case SHAPE_RECT:
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);
                break;
            case SHAPE_CIRCLE:
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
            case SHAPE_TRIANGLE:
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
        }

        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator1.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator(factor));
        objectAnimator1.setInterpolator(new DecelerateInterpolator(factor));
        mUpAnimatorSet = new AnimatorSet();
        mUpAnimatorSet.setDuration(ANIMATION_DURATION);
        mUpAnimatorSet.playTogether(objectAnimator, objectAnimator1, scaleIndication);

        mUpAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mUpAnimatorSet.start();
    }

    /**
     * 下落
     */
    public void freeFall() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", 0, mDistance);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1, 0.2f);

        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator(factor));
        mDownAnimatorSet = new AnimatorSet();
        mDownAnimatorSet.setDuration(ANIMATION_DURATION);
        mDownAnimatorSet.playTogether(objectAnimator, scaleIndication);
        mDownAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeLoadingView.changeShape();
                upThrow();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDownAnimatorSet.start();
    }
}

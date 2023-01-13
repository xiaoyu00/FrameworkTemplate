package com.framework.base.component.face;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.framework.base.R;
import com.framework.base.adapter.ViewPagerAdapter;
import com.framework.base.component.face.core.Emoji;
import com.framework.base.databinding.FragmentChatFaceBinding;
import com.framework.base.parent.basics.BaseBindingFragment;
import com.framework.base.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 适用与im聊天表情
 */
public class FaceChatFragment extends BaseBindingFragment<FragmentChatFaceBinding> {
    private final List<Fragment> fragments = new ArrayList<>();
    private OnEmojiClickListener listener;

    @Override
    public int contextViewId() {
        return R.layout.fragment_chat_face;
    }

    @Override
    public void initialize() {
        initFaceFragments();
    }

    @Override
    public void onGlobalLayoutCompleted() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity(), fragments);
        dataBinding.pageFace.setAdapter(viewPagerAdapter);
        dataBinding.pageFace.setOffscreenPageLimit(2);
    }

    public void setEmojiClickListener(OnEmojiClickListener listener) {
        this.listener = listener;
    }

    private void initFaceFragments() {
        FaceFragment faceFragment = new FaceFragment();
        if (listener != null) {
            faceFragment.setEmojiClickListener(listener);
        }
        fragments.add(faceFragment);
    }
}

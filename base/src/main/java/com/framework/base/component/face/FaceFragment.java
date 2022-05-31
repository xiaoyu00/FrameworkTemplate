package com.framework.base.component.face;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.framework.base.R;
import com.framework.base.component.face.core.Emoji;
import com.framework.base.component.face.core.FaceGroup;
import com.framework.base.component.face.core.FaceManager;
import com.framework.base.component.face.core.RecentEmojiManager;
import com.framework.base.databinding.FragmentFaceBinding;
import com.framework.base.parent.basics.BaseBindingFragment;
import com.framework.base.utils.DensityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 适用与im聊天表情
 */
public class FaceFragment extends BaseBindingFragment<FragmentFaceBinding> {
    ArrayList<Emoji> emojiList;
    ArrayList<Emoji> recentlyEmojiList;
    private int columns = 7;
    private OnEmojiClickListener listener;
    private RecentEmojiManager recentManager;

    @Override
    public int contextViewId() {
        return R.layout.fragment_face;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentManager = RecentEmojiManager.make(getContext());
        initData();
    }

    public void setEmojiClickListener(OnEmojiClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize() {
        initViews();
    }

    private void initData() {
        try {
            emojiList = FaceManager.getEmojiList();
            if (recentManager.getCollection(RecentEmojiManager.PREFERENCE_NAME) != null) {
                recentlyEmojiList = (ArrayList<Emoji>) recentManager.getCollection(RecentEmojiManager.PREFERENCE_NAME);
            } else {
                recentlyEmojiList = new ArrayList<>();
                if (!emojiList.isEmpty()) {
                    if (emojiList.size() > columns) {
                        recentlyEmojiList.addAll(emojiList.subList(0, columns));
                    } else {
                        recentlyEmojiList.addAll(emojiList);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        FaceGVAdapter fGvAdapter = new FaceGVAdapter(recentlyEmojiList, getActivity());
        dataBinding.gvFaceRecent.setAdapter(fGvAdapter);
        dataBinding.gvFaceRecent.setNumColumns(columns);
        if (!recentlyEmojiList.isEmpty()) {
            double height = recentlyEmojiList.get(0).getHeight()+DensityUtils.INSTANCE.dpToPx(requireContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height);
            dataBinding.gvFaceRecent.setLayoutParams(layoutParams);
        }
        dataBinding.gvFaceRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onEmojiClick(recentlyEmojiList.get(position));
                }
            }
        });

        FaceGVAdapter mGvAdapter = new FaceGVAdapter(emojiList, getActivity());
        dataBinding.gvFaceAll.setAdapter(mGvAdapter);
        dataBinding.gvFaceAll.setNumColumns(columns);
        if (!emojiList.isEmpty()) {
            int cNum = (int) Math.ceil(emojiList.size() / (double) columns);
            double height = cNum * emojiList.get(0).getHeight() + (cNum - 1) * DensityUtils.INSTANCE.dpToPx(requireContext(), 16);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height);
            dataBinding.gvFaceAll.setLayoutParams(layoutParams);
        }
        dataBinding.gvFaceAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onEmojiClick(emojiList.get(position));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        try {
            recentManager.putCollection(RecentEmojiManager.PREFERENCE_NAME, recentlyEmojiList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }
}

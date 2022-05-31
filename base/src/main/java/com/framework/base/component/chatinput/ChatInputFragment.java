package com.framework.base.component.chatinput;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.*;
import androidx.fragment.app.FragmentTransaction;

import com.framework.base.R;
import com.framework.base.component.face.FaceChatFragment;
import com.framework.base.component.face.OnEmojiClickListener;
import com.framework.base.component.face.core.Emoji;
import com.framework.base.component.face.core.FaceManager;
import com.framework.base.component.face.core.InputMentionEditText;
import com.framework.base.databinding.FragmentChatInputBinding;
import com.framework.base.parent.basics.BaseBindingFragment;
import com.framework.base.utils.ScreenUtils;
import com.framework.base.utils.SoftKeyBoardUtil;

import java.util.List;

public class ChatInputFragment extends BaseBindingFragment<FragmentChatInputBinding> {
    private FaceChatFragment faceChatFragment = new FaceChatFragment();
    private MoreInputFragment moreInputFragment = new MoreInputFragment();
    /**
     * 语音/文字切换输入控件
     */
    protected boolean mAudioInputDisable;
    /**
     * 表情按钮
     */
    protected boolean mEmojiInputDisable = false;
    /**
     * 更多按钮
     */
    protected boolean mMoreInputDisable = false;

    private boolean isPanelShow = false;
    private ValueAnimator valueAnimator;
    private String mInputContent;
    @Override
    public int contextViewId() {
        return R.layout.fragment_chat_input;
    }

    @Override
    public void initialize() {
        initFragments();
        initViews();

    }
    @SuppressLint("ClickableViewAccessibility")
    private void initViews(){
        dataBinding.voiceInputSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelHide();
                dataBinding.faceBtn.setImageResource(R.drawable.action_face_selector);
                if (mAudioInputDisable) {
                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    showSoftInput();
                } else {
                    dataBinding.voiceInputSwitch.setImageResource(R.mipmap.chat_input_keyboard);
                    dataBinding.chatVoiceInput.setVisibility(VISIBLE);
                    dataBinding.chatMessageInput.setVisibility(GONE);
                    SoftKeyBoardUtil.hideSoftInput(requireContext(), dataBinding.chatMessageInput.getWindowToken());
                }
                mAudioInputDisable = !mAudioInputDisable;
                mEmojiInputDisable = false;
                mMoreInputDisable = false;
            }
        });
        dataBinding.faceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmojiInputDisable) {
                    showSoftInput();
                } else {
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    dataBinding.faceBtn.setImageResource(R.mipmap.chat_input_keyboard);

                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    getChildFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_panel, faceChatFragment, "faceChatFragment").commit();
                    panelShow();
                    SoftKeyBoardUtil.hideSoftInput(requireContext(), dataBinding.chatMessageInput.getWindowToken());
                }
                mEmojiInputDisable = !mEmojiInputDisable;
                mAudioInputDisable = false;
                mMoreInputDisable = false;
            }
        });
        dataBinding.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreInputDisable) {
                    showSoftInput();
                } else {
                    SoftKeyBoardUtil.hideSoftInput(requireContext(), dataBinding.chatMessageInput.getWindowToken());
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    dataBinding.faceBtn.setImageResource(R.drawable.action_face_selector);
                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    panelShow();
                    getChildFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_panel, moreInputFragment, "moreInputFragment").commit();
                }
                mEmojiInputDisable = false;
                mAudioInputDisable = false;
                mMoreInputDisable = !mMoreInputDisable;
            }
        });
        dataBinding.chatMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mInputContent = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    dataBinding.sendBtn.setVisibility(View.GONE);
                    dataBinding.moreBtn.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.sendBtn.setVisibility(View.VISIBLE);
                    dataBinding.moreBtn.setVisibility(View.GONE);
                    if (!TextUtils.equals(mInputContent, dataBinding.chatMessageInput.getText().toString())) {
                        FaceManager.handlerEmojiText(dataBinding.chatMessageInput, dataBinding.chatMessageInput.getText().toString(), true);
                    }
                }
            }
        });
        dataBinding.chatMessageInput.setOnMentionInputListener(new InputMentionEditText.OnMentionInputListener() {
            @Override
            public void onMentionCharacterInput(String tag) {
//                if ((tag.equals(TIMMentionEditText.TIM_MENTION_TAG) || tag.equals(TIMMentionEditText.TIM_MENTION_TAG_FULL))
//                        && TUIChatUtils.isGroupChat(mChatLayout.getChatInfo().getType())) {
//                    if (mStartActivityListener != null) {
//                        mStartActivityListener.onStartGroupMemberSelectActivity();
//                    }
//                }
            }
        });
        dataBinding.chatMessageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mEmojiInputDisable = false;
                    mAudioInputDisable = false;
                    mMoreInputDisable = false;
                    showSoftInput();
                }
                return false;
            }
        });
    }
    private void initFragments(){
        faceChatFragment.setEmojiClickListener(new OnEmojiClickListener() {
            @Override
            public void onEmojiClick(Emoji emoji) {
                int index = dataBinding.chatMessageInput.getSelectionStart();
                Editable editable = dataBinding.chatMessageInput.getText();
                editable.insert(index, emoji.getFilter());
                FaceManager.handlerEmojiText(dataBinding.chatMessageInput, editable.toString(), true);
            }

            @Override
            public void onCustomFaceClick(int groupIndex, Emoji emoji) {

            }
        });
    }
    private void initPanel() {
        int panelHeight = ScreenUtils.INSTANCE.getTotalScreenHeight(requireActivity()) / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, panelHeight);
        dataBinding.fragmentPanel.setLayoutParams(layoutParams);
        ((LinearLayout.LayoutParams) dataBinding.getRoot().getLayoutParams()).bottomMargin = -panelHeight;
        valueAnimator = ValueAnimator.ofInt(0, -panelHeight);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((LinearLayout.LayoutParams) dataBinding.getRoot().getLayoutParams()).bottomMargin = (int) animation.getAnimatedValue();
                dataBinding.getRoot().requestLayout();

            }
        });
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_panel, faceChatFragment, "faceChatFragment").commit();
    }

    public void showSoftInput() {
        panelHide();
        dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
        dataBinding.faceBtn.setImageResource(R.mipmap.chat_input_face);
        dataBinding.chatVoiceInput.setVisibility(GONE);
        dataBinding.chatMessageInput.setVisibility(VISIBLE);
        dataBinding.chatMessageInput.requestFocus();
        SoftKeyBoardUtil.showSoftInput(requireContext());

    }

    private void panelHide() {
        if (isPanelShow) {
            valueAnimator.start();
            isPanelShow = false;
        }
    }

    private void panelShow() {
        if (!isPanelShow) {
            valueAnimator.reverse();
            isPanelShow = true;
        }

    }

    @Override
    public void onGlobalLayoutCompleted() {
        initPanel();
    }
}

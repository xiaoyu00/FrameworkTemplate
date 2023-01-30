package com.framework.base.component.chatinput;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.framework.base.R;
import com.framework.base.component.face.FaceChatFragment;
import com.framework.base.component.face.OnEmojiClickListener;
import com.framework.base.component.face.core.Emoji;
import com.framework.base.component.face.core.FaceManager;
import com.framework.base.component.face.core.InputMentionEditText;
import com.framework.base.databinding.FragmentChatInputBinding;
import com.framework.base.parent.basics.BaseBindingFragment;
import com.framework.base.utils.SoftKeyBoardUtil;

public class ChatInputFragment extends BaseBindingFragment<FragmentChatInputBinding> {

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

    private FaceChatFragment faceChatFragment;

    /**
     * 表情面板控制回调
     */
    private PanelControlListener panelControlListener;

    private String mInputContent;

    private ChatInputViewModel chatInputViewModel;




    @Override
    public int contextViewId() {
        return R.layout.fragment_chat_input;
    }

    @Override
    public void initialize() {
        chatInputViewModel = new ViewModelProvider(requireActivity())
                .get(ChatInputViewModel.class);
        initFragments();
        initViews();
    }

    public void setFaceChatFragment(FaceChatFragment fragment) {
        faceChatFragment = fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        dataBinding.voiceInputSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatInputViewModel.setHandleType(HandleType.ONLY_PANEL_DOWN);
                panelControlListener.onPanelHide();
                chatInputViewModel.setPanelShow(false);
                dataBinding.faceBtn.setImageResource(R.drawable.action_face_selector);
                if (mAudioInputDisable) {
                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    dataBinding.chatMessageInput.requestFocus();
                    chatInputViewModel.setHandleType(HandleType.ONLY_KEYBOARD_UP);
                    showSoftInput();
                } else {
                    dataBinding.voiceInputSwitch.setImageResource(R.mipmap.chat_input_keyboard);
                    dataBinding.chatVoiceInput.setVisibility(VISIBLE);
                    dataBinding.chatMessageInput.setVisibility(GONE);
                    chatInputViewModel.setHandleType(HandleType.ONLY_KEYBOARD_DOWN);
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
                    chatInputViewModel.setHandleType(HandleType.KU_PD);
                    showSoftInput();
                } else {
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    dataBinding.faceBtn.setImageResource(R.mipmap.chat_input_keyboard);

                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    panelControlListener.onSwitchPanelFragment(1);

                    if (SoftKeyBoardUtil.isSoftInputShown(requireContext())) {
                        SoftKeyBoardUtil.hideSoftInput(requireContext(), dataBinding.chatMessageInput.getWindowToken());
                        chatInputViewModel.setHandleType(HandleType.KD_PU);
                    } else {
                        chatInputViewModel.setHandleType(HandleType.ONLY_PANEL_UP);
                        panelControlListener.onPanelShow();
                        chatInputViewModel.setPanelShow(true);
                    }
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
                    chatInputViewModel.setHandleType(HandleType.KU_PD);
                    showSoftInput();
                } else {
                    dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
                    dataBinding.faceBtn.setImageResource(R.drawable.action_face_selector);
                    dataBinding.chatVoiceInput.setVisibility(GONE);
                    dataBinding.chatMessageInput.setVisibility(VISIBLE);
                    if (SoftKeyBoardUtil.isSoftInputShown(requireContext())) {
                        SoftKeyBoardUtil.hideSoftInput(requireContext(), dataBinding.chatMessageInput.getWindowToken());
                        chatInputViewModel.setHandleType(HandleType.KD_PU);
                    } else {
                        chatInputViewModel.setHandleType(HandleType.ONLY_PANEL_UP);
                        panelControlListener.onPanelShow();
                        chatInputViewModel.setPanelShow(true);
                    }
                    panelControlListener.onSwitchPanelFragment(2);
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
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEmojiInputDisable = false;
                    mAudioInputDisable = false;
                    mMoreInputDisable = false;
                    if (chatInputViewModel.isPanelShow()) {
                        chatInputViewModel.setHandleType(HandleType.KD_PU);
                    } else {
                        chatInputViewModel.setHandleType(HandleType.ONLY_KEYBOARD_UP);
                    }
                    SoftKeyBoardUtil.showSoftInput(requireContext());
                }
                return false;
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (chatInputViewModel.isPanelShow()) {
                    chatInputViewModel.setHandleType(HandleType.ONLY_PANEL_DOWN);
                    panelControlListener.onPanelHide();
                    chatInputViewModel.setPanelShow(false);
                    dataBinding.faceBtn.setImageResource(R.mipmap.chat_input_face);
                    return;
                }
                requireActivity().finish();
            }
        };

        //获取Activity的返回键分发器添加回调
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initFragments() {
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

    public void showSoftInput() {
        dataBinding.voiceInputSwitch.setImageResource(R.drawable.action_audio_selector);
        dataBinding.faceBtn.setImageResource(R.mipmap.chat_input_face);
        dataBinding.chatVoiceInput.setVisibility(GONE);
        dataBinding.chatMessageInput.setVisibility(VISIBLE);
        SoftKeyBoardUtil.showSoftInput(requireContext());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PanelControlListener) {
            panelControlListener = (PanelControlListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelControlListener = null;
    }

}

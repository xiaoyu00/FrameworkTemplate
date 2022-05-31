package com.framework.base.views.bottominput;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.framework.base.R;
import com.framework.base.databinding.DialogInputBinding;

public class InputDialog extends Dialog {
    DialogInputBinding binding;

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_input, null, false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setWindowAnimations(R.style.bottom_enter_in);
        setContentView(binding.getRoot());
    }
}

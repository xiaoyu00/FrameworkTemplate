package com.framework.base.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.framework.base.R;
import com.framework.base.utils.FormatterUtil;
import com.framework.base.utils.UnitConversionUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 提醒升级-对话框
 */
public class UpdateDownloadDialog implements View.OnClickListener {

    private Context context;
    private Display display;
    private Dialog dialog;
    private ConstraintLayout lLayout_bg;
    private SeekBar seekBar;
    private int maxTotal;
    private TextView tv_sum, tv_progress, tv_seekbar_topbubble;

    public UpdateDownloadDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public UpdateDownloadDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_update_download, null);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (ConstraintLayout) view.findViewById(R.id.lLayout_bg);
        seekBar = view.findViewById(R.id.pb_data);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar.setOnTouchListener((v, event) -> true);

        tv_seekbar_topbubble = view.findViewById(R.id.tv_seekbar_topbubble);
        tv_sum = view.findViewById(R.id.tv_sum);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        seekBar.setThumb(context.getResources().getDrawable(R.mipmap.update_yuan));
        setMyProgress(1);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.75), LinearLayout.LayoutParams.WRAP_CONTENT));
        view.setBackgroundResource(R.drawable.shape_bg_white_6dp);
        return this;
    }

    protected int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        seekBar.setMax(maxTotal);
    }

    public void setMyProgress(int value) {
        if (value > 1) {
            seekBar.setProgress(value);
        }
        // android.util.Log.e("111", "getFileFromServer: 1111111111111111111"+value );
    }

    public UpdateDownloadDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void setLayout() {

    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            translationText(seekBar, progress);
            seekBar.setThumb(context.getResources().getDrawable(R.mipmap.update_yuan));
            tv_progress.setText((progress / 1000) + "M");
            tv_sum.setText("/" + (maxTotal / 1000) + "M");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();

        }
    };

    private void translationText(SeekBar seekBar, int progress) {
        //设置文本显示
        tv_seekbar_topbubble.setText(getPercentNoSignFormatter().format(((double) progress / (double) maxTotal)));
        //获取文本宽度
        float textWidth = tv_seekbar_topbubble.getWidth();
        //获取seekbar最左端的x位置
        float left = seekBar.getLeft();
        //进度条的刻度值
        float max = Math.abs(seekBar.getMax());
        //这不叫thumb的宽度,叫seekbar距左边宽度,实验了一下，seekbar 不是顶格的，两头都存在一定空间，所以xml 需要用paddingStart 和 paddingEnd 来确定具体空了多少值,我这里设置15dp;
        float thumb = UnitConversionUtils.INSTANCE.dpToPx(context, 15);
        //每移动1个单位，text应该变化的距离 = (seekBar的宽度 - 两头空的空间) / 总的progress长度
        float average = (((float) seekBar.getWidth()) - 2 * thumb) / max;
        //int to float
        float currentProgress = progress;
        //textview 应该所处的位置 = seekbar最左端 + seekbar左端空的空间 + 当前progress应该加的长度 - textview宽度的一半(保持居中作用)
        float pox = left - textWidth / 2 + thumb + average * currentProgress;
        tv_seekbar_topbubble.setX(pox);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private NumberFormat percentNoSignFormatter;

    private NumberFormat getPercentNoSignFormatter() {
        if (percentNoSignFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentNoSignFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                percentNoSignFormatter.setGroupingUsed(false);
                percentNoSignFormatter.setMaximumFractionDigits(0);
                DecimalFormat formatter = (DecimalFormat) percentNoSignFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentNoSignFormatter;
    }


}

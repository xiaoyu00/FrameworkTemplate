package com.framework.base.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 自定义ViewSpan
 * String messageString = "@张三：这是一条消息消息消息哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
 * SpannableString spannableString = new SpannableString(messageString);
 * TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(context, R.style.text_chat_name);
 * spannableString.setSpan(textAppearanceSpan, 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 * View view = LayoutInflater.from(context).inflate(R.layout.layout_live_chat_mark, null);
 * ViewSpan viewSpan = new ViewSpan(view);
 * spannableString.setSpan(viewSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 * tvMessage.setText(spannableString);
 */
public class ViewSpan extends ReplacementSpan {
    private View view;

    public ViewSpan(View view) {
        this.view = view;
        this.view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i1, @Nullable Paint.FontMetricsInt fontMetricsInt) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(widthSpec, heightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        if (fontMetricsInt != null) {

            int height = view.getMeasuredHeight();

            fontMetricsInt.ascent = fontMetricsInt.top = -height / 2;

            fontMetricsInt.descent = fontMetricsInt.bottom = height / 2;

        }

        return view.getRight();
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int transY = (y + fm.descent + y + fm.ascent) / 2 - view.getMeasuredHeight() / 2;//计算y方向的位移

        canvas.save();

        canvas.translate(x, transY);

        view.draw(canvas);

        canvas.restore();
    }
}

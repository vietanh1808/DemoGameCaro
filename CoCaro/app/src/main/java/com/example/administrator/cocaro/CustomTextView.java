package com.example.administrator.cocaro;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 3/12/2018.
 */

public class CustomTextView extends TextView{

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure (int width, int height) {
        super.onMeasure(width, height);
        int dai = getMeasuredWidth();
        setMeasuredDimension(dai, dai);
    }
}

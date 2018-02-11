package com.example.customcomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

/**
 * @author wanlijun
 * @description 自定义PopupWindow
 * @time 2018/2/8 16:04
 */

public class MyPopupWindow extends PopupWindow {
    private NumberPicker yearPick;
    private int maxValue;
    private int minValue;
    public MyPopupWindow(Context context){
        this(context,null);
    }
    public MyPopupWindow(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyPopupWindow(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyPopupWindow);
        maxValue = typedArray.getInteger(R.styleable.MyPopupWindow_maxValue,2050);
        minValue = typedArray.getInteger(R.styleable.MyPopupWindow_minValue,1957);
        typedArray.recycle();
        initView(context);
    }
    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout,null);
        yearPick = (NumberPicker)view.findViewById(R.id.yearPick);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //获取焦点后点击PopupWindow以外的地方或者按返回键，PopupWindow才会消失
        setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(0xffffffff);
        setBackgroundDrawable(colorDrawable);
    }

    public void setMaxValue(int maxValue){
        yearPick.setMaxValue(maxValue);
    }
    public void setMinValue(int minValue){
        yearPick.setMinValue(minValue);
    }
    public void setValue(int value){
        yearPick.setValue(value);
    }
}

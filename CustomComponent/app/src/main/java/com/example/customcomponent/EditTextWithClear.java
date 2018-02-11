package com.example.customcomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author wanlijun
 * @description  自定义带清空功能的编辑框
 * @time 2018/2/5 17:56
 */

public class EditTextWithClear extends LinearLayout implements View.OnClickListener{
    private ImageView iconIv;   //编辑框前面的图标
    private EditText customEt;   //编辑框
    private ImageButton clearBtn;   //编辑框带的清空输入按钮
    private View bottomLine;   //编辑框底部的线
    private boolean isClearBtnInvisibility;
    private int myStyle;
    private int iconIvBackground;
    public EditTextWithClear(Context context){
        this(context,null);
    }
    public EditTextWithClear(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public EditTextWithClear(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.EditTextWithClearAttr);
        isClearBtnInvisibility = typedArray.getBoolean(R.styleable.EditTextWithClearAttr_isClearBtnInvisibility,true);
        myStyle = typedArray.getInt(R.styleable.EditTextWithClearAttr_myStyle,2);
        iconIvBackground = typedArray.getResourceId(R.styleable.EditTextWithClearAttr_iconIvBackground,android.R.drawable.ic_media_pause);
        typedArray.recycle();
        intitView(context);
        initValue();
    }
    private void intitView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_edittext_layout,null);
        iconIv = (ImageView)view.findViewById(R.id.iconIv);
        customEt = (EditText)view.findViewById(R.id.customEt);
        clearBtn = (ImageButton)view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        bottomLine = (View)view.findViewById(R.id.bottomLine);
        addView(view);
    }
    public void setIconIvVisibility(int visibility){
        iconIv.setVisibility(visibility);
    }
    public void setClearBtnVisibility(int visibility){
        clearBtn.setVisibility(visibility);
    }
    public void setIconIvBackground(int resid){
        iconIv.setBackgroundResource(resid);
    }
    public void setClearBtnBackground(int resid){
        clearBtn.setBackgroundResource(resid);
    }
    private void initValue(){
        if(isClearBtnInvisibility){
            clearBtn.setVisibility(GONE);
        }
        customEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("wanlijun","beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("wanlijun","onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("wanlijun","afterTextChanged");
                if(isClearBtnInvisibility){
                    if(editable.toString().length()<=0){
                        clearBtn.setVisibility(GONE);
                    }else{
                        clearBtn.setVisibility(VISIBLE);
                    }
                }
            }
        });
        if(myStyle == 1){
            //经典
            //EditTextWithClear继承LinearLayout,属于容器ViewGroup，不会主动执行onDraw方法，用setWillNotDraw(false)可以让其执行onDraw方法
            setWillNotDraw(false);
            invalidate();
        }
        setIconIvBackground(iconIvBackground);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clearBtn:
                customEt.setText("");
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(myStyle == 1){
            //经典
            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.parseColor("#a3a3a5"));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5.0f);
            canvas.drawRect(10,10,getWidth()-10,getHeight()-10,mPaint);
        }
    }
}

package com.android.banana.commlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;

import com.android.banana.commlib.R;

/**
 * Created by qiaomu on 2017/11/2.
 */

public class ToggleEditTextView extends android.support.v7.widget.AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {
    private boolean visible;
    private boolean isPassWrod;
    private Drawable visibleDrawable;
    private Drawable invisibleDrawable;

    public ToggleEditTextView(Context context) {
        this(context, null);
    }

    public ToggleEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ToggleEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.toggleEditTextView);
        isPassWrod = getInputType() == (EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        visible = typedArray.getInt(R.styleable.toggleEditTextView_visibility, 0) == 1;
        int invisibleId = typedArray.getResourceId(R.styleable.toggleEditTextView_toggle_invisible, 0);
        int visibleId = typedArray.getResourceId(R.styleable.toggleEditTextView_toggle_visible, 0);
        typedArray.recycle();

        if (visibleId != 0)
            visibleDrawable = ContextCompat.getDrawable(context, visibleId);
        if (invisibleId != 0)
            invisibleDrawable = ContextCompat.getDrawable(context, invisibleId);


        if (visibleDrawable != null)
            visibleDrawable.setBounds(0, 0, visibleDrawable.getIntrinsicWidth(), visibleDrawable.getIntrinsicHeight());
        if (invisibleDrawable != null)
            invisibleDrawable.setBounds(0, 0, invisibleDrawable.getIntrinsicWidth(), invisibleDrawable.getIntrinsicHeight());

        setToggleIconVisible(visible);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - visibleDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    if (isPassWrod)
                        togglePassword();
                    else
                        getText().clear();
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private void togglePassword() {
        if (visible) {
            //显示密码明文
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            postInvalidate();
            CharSequence charSequence = getText();
            //为了保证体验效果，需要保持输入焦点在文本最后一位
            if (charSequence != null) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            postInvalidate();
            setSelection(getText().length());
        }
        visible = !visible;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setToggleIconVisible(getText().length() > 0);
        } else {
            setToggleIconVisible(false);
        }
    }


    public void setToggleIconVisible(boolean visible) {
        if (isPassWrod) {
            Drawable right = visible ? invisibleDrawable : visibleDrawable;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], visible ? visibleDrawable : null, getCompoundDrawables()[3]);
        }
    }




    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        setToggleIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
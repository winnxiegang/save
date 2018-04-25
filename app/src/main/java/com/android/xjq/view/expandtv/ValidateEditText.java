package com.android.xjq.view.expandtv;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xjq.XjqApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lingjiu on 2017/11/23.
 */

public class ValidateEditText extends EditText {
    public static String PASSWORD_PATTERN = "[0-9a-zA-Z!@#$%^&*()]";
    public static String NICKNAME_PATTERN = "[\u4e00-\u9fa5_a-zA-Z0-9]";

    public ValidateEditText(Context context) {
        super(context);
        init();
    }

    public ValidateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValidateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }


    public void setInputFilter(final String rules,int maxLength) {
        InputFilter inputFilter = new InputFilter() {
            Pattern pattern = Pattern.compile(rules);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                Matcher matcher = pattern.matcher(source);
                //LogUtils.e("filter", "dest=" + dest + "    " + source);
                if (matcher.find()) {
                    return source;
                } else {
                    //删除字符,以及切换可见/不可见的时候,不提示
                    if (!((!TextUtils.isEmpty(dest) && TextUtils.isEmpty(source)) ||
                            (!TextUtils.isEmpty(source) && source.length() > 1) ||
                            TextUtils.isEmpty(source) && TextUtils.isEmpty(dest)))
                        Toast.makeText(XjqApplication.getContext(), "输入字符无效", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
        };

        setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(maxLength)});
    }

}

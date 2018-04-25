package com.android.xjq.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouyi on 2017/3/30.
 */

public class ValidateUtils {

    public enum NormalPattern implements ValidatePattern {

        PHONE("请输入有效的手机号码", "^[1]{1}[0-9]{10}$"),

        SMS_ID("", ""),

        NICKNAME("", ""),

        PASSWORD("密码位数为8-16位", "^{8,16}$");

        String name;

        String pattern;

        NormalPattern(String name, String pattern) {
            this.name = name;
            this.pattern = pattern;
        }

        @Override
        public String getMessage() {
            return name;
        }

        @Override
        public String getPattern() {
            return pattern;
        }
    }


    public boolean validate(List<ValidateBean> list, ValidateListener listener) {
        for (ValidateBean bean : list) {
            Pattern pattern = Pattern.compile(bean.getPattern().getPattern());
            Matcher matcher = pattern.matcher(bean.validateMessage);
            if (!matcher.find()) {
                listener.showMessage(bean.pattern);
                return false;
            }
        }
        return true;
    }

    public static interface ValidatePattern {
        public String getMessage();

        public String getPattern();
    }


    public static class ValidateBean {

        String validateMessage;

        ValidatePattern pattern;

        public String getValidateMessage() {
            return validateMessage;
        }

        public void setValidateMessage(String validateMessage) {
            this.validateMessage = validateMessage;
        }

        public ValidatePattern getPattern() {
            return pattern;
        }

        public void setPattern(ValidatePattern pattern) {
            this.pattern = pattern;
        }
    }


    static interface ValidateListener {
        public void showMessage(ValidatePattern pattern);
    }
}

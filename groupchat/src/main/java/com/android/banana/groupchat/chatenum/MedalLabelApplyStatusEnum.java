package com.android.banana.groupchat.chatenum;

/**
 * Created by lingjiu on 2017/12/8.
 */

public enum MedalLabelApplyStatusEnum {
    APPLYING("审核中"),

    AUDIT_REFUSE("审核失败"),

    AUDIT_PASS("审核通过"),

    NOT_APPLYING("未申请"),

    DEFAULT("");

    String message;

    public String getMessage() {
        return message;
    }

    MedalLabelApplyStatusEnum(String message) {
        this.message = message;
    }

    public static MedalLabelApplyStatusEnum safeValueOf(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
        }
        return DEFAULT;
    }
}

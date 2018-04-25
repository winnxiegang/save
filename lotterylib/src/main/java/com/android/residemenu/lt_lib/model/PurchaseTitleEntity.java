package com.android.residemenu.lt_lib.model;

/**
 * Created by Duzaoqiu on 2016/7/12 14:22.
 */
public class PurchaseTitleEntity {

    private String modifyTitleReasonString;

    private String modifyTitleTime;

    private boolean modifyTitle;

    public PurchaseTitleEntity(String modifyTitleReasonString, String modifyTitleTime, boolean modifyTitle) {
        this.modifyTitleReasonString = modifyTitleReasonString;
        this.modifyTitleTime = modifyTitleTime;
        this.modifyTitle = modifyTitle;
    }

    public String getModifyTitleReasonString() {
        return modifyTitleReasonString;
    }

    public void setModifyTitleReasonString(String modifyTitleReasonString) {
        this.modifyTitleReasonString = modifyTitleReasonString;
    }

    public String getModifyTitleTime() {
        return modifyTitleTime;
    }

    public void setModifyTitleTime(String modifyTitleTime) {
        this.modifyTitleTime = modifyTitleTime;
    }

    public boolean isModifyTitle() {
        return modifyTitle;
    }

    public void setModifyTitle(boolean modifyTitle) {
        this.modifyTitle = modifyTitle;
    }
}

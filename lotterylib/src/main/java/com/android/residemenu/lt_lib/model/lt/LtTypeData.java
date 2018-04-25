package com.android.residemenu.lt_lib.model.lt;

public class LtTypeData {

    private String code;

    private String name;

    private boolean selected;

    private int[] selectedDrawable;

    public LtTypeData() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int[] getSelectedDrawable() {
        return selectedDrawable;
    }

    public void setSelectedDrawable(int[] selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
    }
}

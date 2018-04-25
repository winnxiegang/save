package com.etiennelawlor.imagegallery.library.event;

/**
 * Created by Coffee on 2016/2/18 18:45.
 */
public class EventChangeSelectPicStatus {

    public EventChangeSelectPicStatus() {
    }

    public EventChangeSelectPicStatus(int position, boolean isSelect) {
        this.position = position;
        this.isSelect = isSelect;
        this.isClickPreviewTv = false;
    }

    public EventChangeSelectPicStatus(int position, boolean isSelect, boolean isClickPreviewTv) {
        this.position = position;
        this.isSelect = isSelect;
        this.isClickPreviewTv = isClickPreviewTv;
    }

    public int position;

    private boolean isSelect;

    private boolean isClickPreviewTv;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isClickPreviewTv() {
        return isClickPreviewTv;
    }

    public void setIsClickPreviewTv(boolean isClickPreviewTv) {
        this.isClickPreviewTv = isClickPreviewTv;
    }
}

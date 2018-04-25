package com.android.xjq.bean.matchLive;

/**
 * Created by 周毅 on 2015/7/1 13:52.
 */
public class LeagueInfo {

    private boolean isSelected;

    private String cnShortName;

    private String id;

    public String getCnShortName() {
        return cnShortName;
    }

    public void setCnShortName(String cnShortName) {
        this.cnShortName = cnShortName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public boolean equals(Object o) {
        if ("0".equals(id)) {
            LeagueInfo dst = (LeagueInfo) o;
            if (cnShortName.equals(dst.cnShortName)) {
                return true;
            }
            return false;
        } else {
            LeagueInfo dst = (LeagueInfo) o;
            if (id.equals(dst.id)) {
                return true;
            }
            return false;
        }
    }
}

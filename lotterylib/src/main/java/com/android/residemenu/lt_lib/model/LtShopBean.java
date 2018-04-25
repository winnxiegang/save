package com.android.residemenu.lt_lib.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouyi on 2015/9/24 15:44.
 */
public class LtShopBean {

    private String name;

    private String url;

    private String id;

    private String subDomain;

    private String code;

    public LtShopBean(JSONObject jo) throws JSONException {
        String id = jo.getString("id");
        String branchName = jo.getString("branchName");
        String subDomain = jo.getString("subDomain");
        String code = jo.getString("code");

        setSubDomain(subDomain);

        setId(id);

        setName(branchName);

        setCode(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

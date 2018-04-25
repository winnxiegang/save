package com.android.residemenu.lt_lib.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouyi on 2015/9/24 17:23.
 */
public class PageIndicatorBean {

    private int page;

    private int items;

    private int itemsPerPage;

    private int pages;

    public PageIndicatorBean(JSONObject jo) throws JSONException {
        page = jo.getInt("page");
        items = jo.getInt("items");
        itemsPerPage = jo.getInt("itemsPerPage");
        pages = jo.getInt("pages");

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

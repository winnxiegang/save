package com.android.banana.commlib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouyi on 2015/12/4 16:04.
 */
public class PaginatorBean implements Parcelable {

    private int page;

    private int items;

    private int itemsPerPage;

    private int pages;

    public PaginatorBean(JSONObject jo)throws JSONException {

        page = jo.getInt("page");

        items = jo.getInt("items");

        itemsPerPage = jo.getInt("itemsPerPage");

        pages = jo.getInt("pages");

    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPage() {
        return page;
    }

    public int getItems() {
        return items;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getPages() {
        return pages;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.items);
        dest.writeInt(this.itemsPerPage);
        dest.writeInt(this.pages);
    }

    protected PaginatorBean(Parcel in) {
        this.page = in.readInt();
        this.items = in.readInt();
        this.itemsPerPage = in.readInt();
        this.pages = in.readInt();
    }

    public static final Parcelable.Creator<PaginatorBean> CREATOR = new Parcelable.Creator<PaginatorBean>() {
        @Override
        public PaginatorBean createFromParcel(Parcel source) {
            return new PaginatorBean(source);
        }

        @Override
        public PaginatorBean[] newArray(int size) {
            return new PaginatorBean[size];
        }
    };
}

package com.android.residemenu.lt_lib.model;

/**
 * Created by Administrator on 2016/8/23.
 */
public class PurchaseType {

    private String name;
    private String message;


    public PurchaseType(String name, String message) {
        super();
        this.name = name;
        this.message = message;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}

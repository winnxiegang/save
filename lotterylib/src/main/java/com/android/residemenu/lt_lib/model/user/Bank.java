package com.android.residemenu.lt_lib.model.user;

/**
 * Created by zhouyi on 2015/4/24.
 */
public class Bank {


    /**
     * 工商银行
     */
    private String name;

    private int orderNumber;

    private boolean third;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


    public boolean isThird() {
        return third;
    }

    public void setThird(boolean third) {
        this.third = third;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * ICBC
     */
    private String code;

}

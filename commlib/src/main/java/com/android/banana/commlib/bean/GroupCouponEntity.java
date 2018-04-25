package com.android.banana.commlib.bean;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.bean.PaginatorBean;

import java.util.List;

/**
 * Created by lingjiu on 2017/7/25.
 */

public class GroupCouponEntity implements BaseOperator {

    private int num;
    private PaginatorBean paginator;
    private int totalNum;
    private List<String> allocatedCouponNos;
    private List<GroupCouponInfoBean> groupCouponList;

    @Override
    public void operatorData() {
        if (groupCouponList != null && groupCouponList.size() != 0) {
            if (allocatedCouponNos != null && allocatedCouponNos.size() != 0) {
                for (GroupCouponInfoBean groupCouponInfo : groupCouponList) {
                    if (allocatedCouponNos.contains(groupCouponInfo.getCouponNo())) {
                        groupCouponInfo.setIsOwnAllocated(true);
                    }
                }
            }
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<String> getAllocatedCouponNos() {
        return allocatedCouponNos;
    }

    public void setAllocatedCouponNos(List<String> allocatedCouponNos) {
        this.allocatedCouponNos = allocatedCouponNos;
    }

    public List<GroupCouponInfoBean> getGroupCouponList() {
        return groupCouponList;
    }

    public void setGroupCouponList(List<GroupCouponInfoBean> groupCouponList) {
        this.groupCouponList = groupCouponList;
    }

}

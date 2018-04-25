package com.android.banana.commlib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2016/11/9 15:33.
 */
public class FetCouponInfoBean implements Parcelable {
    /**
     * 抢过的红包id
     */
    private List<String> allocatedCouponNos;
    /**
     * 可抢红包
     */
    private int num;
    /**
     * 分页器
     */
    private PaginatorBean paginator;
    /**
     * 方案号
     */
    private HashMap<String, String> purchaseNoAndAppPurchaseNo;
    /**
     * 群红包列表
     */
    private List<GroupCouponInfo> groupCouponList;
    /**
     * 是否显示红包个数以及总金额
     */
    private boolean supportShowTotalCountAndAmount;


    public FetCouponInfoBean(JSONObject jo) {

        FetCouponInfoBean fetCouponInfoBean = new Gson().fromJson(jo.toString(), FetCouponInfoBean.class);

        this.allocatedCouponNos = fetCouponInfoBean.allocatedCouponNos;

        this.num = fetCouponInfoBean.num;

        this.paginator = fetCouponInfoBean.paginator;

        this.purchaseNoAndAppPurchaseNo = fetCouponInfoBean.purchaseNoAndAppPurchaseNo;

        this.supportShowTotalCountAndAmount = fetCouponInfoBean.supportShowTotalCountAndAmount;

        List<GroupCouponInfo> groupCouponList = fetCouponInfoBean.groupCouponList;

        if (allocatedCouponNos != null && allocatedCouponNos.size() != 0) {

            for (GroupCouponInfo groupCouponInfo : groupCouponList) {

                for (String allocatedCouponNo : allocatedCouponNos) {

                    if (groupCouponInfo.getThirdGroupCouponId().equals(allocatedCouponNo)) {

                        groupCouponInfo.setIsOwnAllocated(true);
                    }
                }
            }
        }

        this.groupCouponList = groupCouponList;

    }

    public static class GroupCouponInfo implements Parcelable {
        public GroupCouponInfo(){}

        /**
         * 红包状态
         */
        private String allocateStatus;
        /**
         * 红包类型
         */
        private String amountAllocateType;
        private String loginName;
        private String userLogoUrl;
        private boolean vip;
        private String createUserId;
        private String gmtCreate;
        private String gmtExpired;
        private String gmtModified;
        private String gmtPay;
        private int id;
        private String platformCode;
        private String platformObjectId;
        private String platformObjectType;
        private String platformType;
        private int remainNum;
        private String thirdGroupCouponId;
        private String thirdGroupCouponUniqueId;
        private String title;
        private double totalAmount;
        private int totalCount;
        /**
         * 是否领取过该红包
         */
        @Expose
        private boolean isOwnAllocated;

        public boolean isOwnAllocated() {
            return isOwnAllocated;
        }

        public void setIsOwnAllocated(boolean isOwnAllocated) {
            this.isOwnAllocated = isOwnAllocated;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public String getAllocateStatus() {
            return allocateStatus;
        }

        public void setAllocateStatus(String allocateStatus) {
            this.allocateStatus = allocateStatus;
        }

        public String getAmountAllocateType() {
            return amountAllocateType;
        }

        public void setAmountAllocateType(String amountAllocateType) {
            this.amountAllocateType = amountAllocateType;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtExpired() {
            return gmtExpired;
        }

        public void setGmtExpired(String gmtExpired) {
            this.gmtExpired = gmtExpired;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public String getGmtPay() {
            return gmtPay;
        }

        public void setGmtPay(String gmtPay) {
            this.gmtPay = gmtPay;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlatformCode() {
            return platformCode;
        }

        public void setPlatformCode(String platformCode) {
            this.platformCode = platformCode;
        }

        public String getPlatformObjectId() {
            return platformObjectId;
        }

        public void setPlatformObjectId(String platformObjectId) {
            this.platformObjectId = platformObjectId;
        }

        public String getPlatformObjectType() {
            return platformObjectType;
        }

        public void setPlatformObjectType(String platformObjectType) {
            this.platformObjectType = platformObjectType;
        }

        public String getPlatformType() {
            return platformType;
        }

        public void setPlatformType(String platformType) {
            this.platformType = platformType;
        }

        public int getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(int remainNum) {
            this.remainNum = remainNum;
        }

        public String getThirdGroupCouponId() {
            return thirdGroupCouponId;
        }

        public void setThirdGroupCouponId(String thirdGroupCouponId) {
            this.thirdGroupCouponId = thirdGroupCouponId;
        }

        public String getThirdGroupCouponUniqueId() {
            return thirdGroupCouponUniqueId;
        }

        public void setThirdGroupCouponUniqueId(String thirdGroupCouponUniqueId) {
            this.thirdGroupCouponUniqueId = thirdGroupCouponUniqueId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.allocateStatus);
            dest.writeString(this.amountAllocateType);
            dest.writeString(this.loginName);
            dest.writeString(this.userLogoUrl);
            dest.writeByte(this.vip ? (byte) 1 : (byte) 0);
            dest.writeString(this.createUserId);
            dest.writeString(this.gmtCreate);
            dest.writeString(this.gmtExpired);
            dest.writeString(this.gmtModified);
            dest.writeString(this.gmtPay);
            dest.writeInt(this.id);
            dest.writeString(this.platformCode);
            dest.writeString(this.platformObjectId);
            dest.writeString(this.platformObjectType);
            dest.writeString(this.platformType);
            dest.writeInt(this.remainNum);
            dest.writeString(this.thirdGroupCouponId);
            dest.writeString(this.thirdGroupCouponUniqueId);
            dest.writeString(this.title);
            dest.writeDouble(this.totalAmount);
            dest.writeInt(this.totalCount);
            dest.writeByte(this.isOwnAllocated ? (byte) 1 : (byte) 0);
        }

        public GroupCouponInfo(Parcel in) {
            this.allocateStatus = in.readString();
            this.amountAllocateType = in.readString();
            this.loginName = in.readString();
            this.userLogoUrl = in.readString();
            this.vip = in.readByte() != 0;
            this.createUserId = in.readString();
            this.gmtCreate = in.readString();
            this.gmtExpired = in.readString();
            this.gmtModified = in.readString();
            this.gmtPay = in.readString();
            this.id = in.readInt();
            this.platformCode = in.readString();
            this.platformObjectId = in.readString();
            this.platformObjectType = in.readString();
            this.platformType = in.readString();
            this.remainNum = in.readInt();
            this.thirdGroupCouponId = in.readString();
            this.thirdGroupCouponUniqueId = in.readString();
            this.title = in.readString();
            this.totalAmount = in.readDouble();
            this.totalCount = in.readInt();
            this.isOwnAllocated = in.readByte() != 0;
        }

        public static final Creator<GroupCouponInfo> CREATOR = new Creator<GroupCouponInfo>() {
            @Override
            public GroupCouponInfo createFromParcel(Parcel source) {
                return new GroupCouponInfo(source);
            }

            @Override
            public GroupCouponInfo[] newArray(int size) {
                return new GroupCouponInfo[size];
            }
        };
    }


    public List<String> getAllocatedCouponNos() {
        return allocatedCouponNos;
    }

    public void setAllocatedCouponNos(List<String> allocatedCouponNos) {
        this.allocatedCouponNos = allocatedCouponNos;
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

    public HashMap<String, String> getPurchaseNoAndAppPurchaseNo() {
        return purchaseNoAndAppPurchaseNo;
    }

    public void setPurchaseNoAndAppPurchaseNo(HashMap<String, String> purchaseNoAndAppPurchaseNo) {
        this.purchaseNoAndAppPurchaseNo = purchaseNoAndAppPurchaseNo;
    }

    public List<GroupCouponInfo> getGroupCouponList() {
        return groupCouponList;
    }

    public void setGroupCouponList(List<GroupCouponInfo> groupCouponList) {
        this.groupCouponList = groupCouponList;
    }

    public boolean isSupportShowTotalCountAndAmount() {
        return supportShowTotalCountAndAmount;
    }

    public void setSupportShowTotalCountAndAmount(boolean supportShowTotalCountAndAmount) {
        this.supportShowTotalCountAndAmount = supportShowTotalCountAndAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.allocatedCouponNos);
        dest.writeInt(this.num);
        dest.writeParcelable(this.paginator, flags);
        dest.writeSerializable(this.purchaseNoAndAppPurchaseNo);
        dest.writeTypedList(this.groupCouponList);
        dest.writeByte(this.supportShowTotalCountAndAmount ? (byte) 1 : (byte) 0);
    }

    protected FetCouponInfoBean(Parcel in) {
        this.allocatedCouponNos = in.createStringArrayList();
        this.num = in.readInt();
        this.paginator = in.readParcelable(PaginatorBean.class.getClassLoader());
        this.purchaseNoAndAppPurchaseNo = (HashMap<String, String>) in.readSerializable();
        this.groupCouponList = in.createTypedArrayList(GroupCouponInfo.CREATOR);
        this.supportShowTotalCountAndAmount = in.readByte() != 0;
    }

    public static final Creator<FetCouponInfoBean> CREATOR = new Creator<FetCouponInfoBean>() {
        @Override
        public FetCouponInfoBean createFromParcel(Parcel source) {
            return new FetCouponInfoBean(source);
        }

        @Override
        public FetCouponInfoBean[] newArray(int size) {
            return new FetCouponInfoBean[size];
        }
    };
}

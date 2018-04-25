package com.android.banana.commlib.bean;



import java.util.List;

/**
 * Created by lingjiu on 2017/7/26.
 */

public class CouponDetailInfoBean implements BaseOperator {

    private GroupCouponRemainInfoBean groupCouponRemainInfo;
    private boolean jumpLogin;
    private CouponDetailItemBean maxGroupCouponItem;
    private PaginatorBean paginator;
    private List<CouponDetailItemBean> groupCouponItemList;

    private String createUserId;
    private String createUserLoginName;
    private String logoUrl;
    private String title;
    private double fetchedAmount;
    private String couponType;

    @Override
    public void operatorData() {
        if (maxGroupCouponItem != null && groupCouponItemList != null && groupCouponItemList.size() > 0) {
            for (CouponDetailItemBean couponDetailItemBean : groupCouponItemList) {
                if (couponDetailItemBean.getId().equals(maxGroupCouponItem.getId())) {
                    couponDetailItemBean.setLuckyKing(true);
                    break;
                }
            }
        }

    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public double getAmount() {
        return fetchedAmount;
    }

    public void setAmount(double amount) {
        this.fetchedAmount = amount;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserLoginName() {
        return createUserLoginName;
    }

    public void setCreateUserLoginName(String createUserLoginName) {
        this.createUserLoginName = createUserLoginName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getFetchedAmount() {
        return fetchedAmount;
    }

    public void setFetchedAmount(double fetchedAmount) {
        this.fetchedAmount = fetchedAmount;
    }

    public GroupCouponRemainInfoBean getGroupCouponRemainInfo() {
        return groupCouponRemainInfo;
    }

    public void setGroupCouponRemainInfo(GroupCouponRemainInfoBean groupCouponRemainInfo) {
        this.groupCouponRemainInfo = groupCouponRemainInfo;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public CouponDetailItemBean getMaxGroupCouponItem() {
        return maxGroupCouponItem;
    }

    public void setMaxGroupCouponItem(CouponDetailItemBean maxGroupCouponItem) {
        this.maxGroupCouponItem = maxGroupCouponItem;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public List<CouponDetailItemBean> getGroupCouponItemList() {
        return groupCouponItemList;
    }

    public void setGroupCouponItemList(List<CouponDetailItemBean> groupCouponItemList) {
        this.groupCouponItemList = groupCouponItemList;
    }

    public static class GroupCouponRemainInfoBean {
        /**
         * allocateStatus : {"message":"已分配","name":"ALL_ALLOCATED"}
         * receivedAmount : 2000.0
         * receivedNum : 2
         * remainNum : 0
         * totalAmount : 2000.0
         * totalCount : 2
         */

        private AllocateStatusBean allocateStatus;
        private double receivedAmount;
        private int receivedNum;
        private int remainNum;
        private double totalAmount;
        private int totalCount;

        public AllocateStatusBean getAllocateStatus() {
            return allocateStatus;
        }

        public void setAllocateStatus(AllocateStatusBean allocateStatus) {
            this.allocateStatus = allocateStatus;
        }

        public double getReceivedAmount() {
            return receivedAmount;
        }

        public void setReceivedAmount(double receivedAmount) {
            this.receivedAmount = receivedAmount;
        }

        public int getReceivedNum() {
            return receivedNum;
        }

        public void setReceivedNum(int receivedNum) {
            this.receivedNum = receivedNum;
        }

        public int getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(int remainNum) {
            this.remainNum = remainNum;
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

        public static class AllocateStatusBean {
            /**
             * message : 已分配
             * name : ALL_ALLOCATED
             */

            private String message;
            private String name;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

}

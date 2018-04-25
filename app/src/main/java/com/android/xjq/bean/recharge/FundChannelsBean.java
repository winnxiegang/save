package com.android.xjq.bean.recharge;

import java.util.List;

/**
 * Created by lingjiu on 2017/6/9.
 */

public class FundChannelsBean {


    /**
     * id : 460059
     * code : WEIXINPAY_APP_QRCODE
     * name : 微信扫码支付
     * providerType : {"name":"WEIXINPAY","message":"微信支付","value":0}
     * orderNumber : 0
     * bankName : 微信
     * bankCode : WEIXINPAY
     * chargeRate : 0.0
     * chargeFee : 0.0
     * maxChargeFee : 0.0
     * logoUrl : http://livemapi.huohongshe.net/fundChannelLogo.resource?code=WEIXINPAY_APP_QRCODE&mt=1495705541000
     * userInstruction : 保存二维码支付,无手续费
     * payType : {"name":"MIXED","message":"混合的","value":0}
     * maxAllowAmount : 0.0
     * minAllowAmount : 0.0
     * depositCreateType : {"name":"MCLIENT","message":"SDK方式","value":0}
     * chargeRangeList : [{"minAmount":1,"maxAmount":1,"includeMin":true,"includeMax":true,"chargeRate":1,"chargeFee":1,"maxChargeFee":1}]
     */

    private int id;
    private String code;
    private String name;
    private ProviderTypeBean providerType;
    private int orderNumber;
    private String bankName;
    private String bankCode;
    private double chargeRate;
    private double chargeFee;
    private double maxChargeFee;
    private String logoUrl;
    private String userInstruction;
    private PayTypeBean payType;
    private double maxAllowAmount;
    private double minAllowAmount;
    private DepositCreateTypeBean depositCreateType;
    private List<ChargeRangeListBean> chargeRangeList;
    private double goldcoinExchangeRate;

    public double getGoldcoinExchangeRate() {
        return goldcoinExchangeRate;
    }

    public void setGoldcoinExchangeRate(double goldcoinExchangeRate) {
        this.goldcoinExchangeRate = goldcoinExchangeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProviderTypeBean getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderTypeBean providerType) {
        this.providerType = providerType;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public double getMaxChargeFee() {
        return maxChargeFee;
    }

    public void setMaxChargeFee(double maxChargeFee) {
        this.maxChargeFee = maxChargeFee;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getUserInstruction() {
        return userInstruction;
    }

    public void setUserInstruction(String userInstruction) {
        this.userInstruction = userInstruction;
    }

    public PayTypeBean getPayType() {
        return payType;
    }

    public void setPayType(PayTypeBean payType) {
        this.payType = payType;
    }

    public double getMaxAllowAmount() {
        return maxAllowAmount;
    }

    public void setMaxAllowAmount(double maxAllowAmount) {
        this.maxAllowAmount = maxAllowAmount;
    }

    public double getMinAllowAmount() {
        return minAllowAmount;
    }

    public void setMinAllowAmount(double minAllowAmount) {
        this.minAllowAmount = minAllowAmount;
    }

    public DepositCreateTypeBean getDepositCreateType() {
        return depositCreateType;
    }

    public void setDepositCreateType(DepositCreateTypeBean depositCreateType) {
        this.depositCreateType = depositCreateType;
    }

    public List<ChargeRangeListBean> getChargeRangeList() {
        return chargeRangeList;
    }

    public void setChargeRangeList(List<ChargeRangeListBean> chargeRangeList) {
        this.chargeRangeList = chargeRangeList;
    }

    public static class ProviderTypeBean {
        /**
         * name : WEIXINPAY
         * message : 微信支付
         * value : 0
         */

        private String name;
        private String message;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class PayTypeBean {
        /**
         * name : MIXED
         * message : 混合的
         * value : 0
         */

        private String name;
        private String message;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class DepositCreateTypeBean {
        /**
         * name : MCLIENT
         * message : SDK方式
         * value : 0
         */

        private String name;
        private String message;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class ChargeRangeListBean {
        /**
         * minAmount : 1.0
         * maxAmount : 1.0
         * includeMin : true
         * includeMax : true
         * chargeRate : 1.0
         * chargeFee : 1.0
         * maxChargeFee : 1.0
         */

        private double minAmount;
        private double maxAmount;
        private boolean includeMin;
        private boolean includeMax;
        private double chargeRate;
        private double chargeFee;
        private double maxChargeFee;

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public boolean isIncludeMin() {
            return includeMin;
        }

        public void setIncludeMin(boolean includeMin) {
            this.includeMin = includeMin;
        }

        public boolean isIncludeMax() {
            return includeMax;
        }

        public void setIncludeMax(boolean includeMax) {
            this.includeMax = includeMax;
        }

        public double getChargeRate() {
            return chargeRate;
        }

        public void setChargeRate(double chargeRate) {
            this.chargeRate = chargeRate;
        }

        public double getChargeFee() {
            return chargeFee;
        }

        public void setChargeFee(double chargeFee) {
            this.chargeFee = chargeFee;
        }

        public double getMaxChargeFee() {
            return maxChargeFee;
        }

        public void setMaxChargeFee(double maxChargeFee) {
            this.maxChargeFee = maxChargeFee;
        }
    }
}

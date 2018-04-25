package com.android.xjq.bean.accountdetails;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.PaginatorBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zaozao on 2017/11/3.
 */

public class AccountBean implements BaseOperator{

    /**
     * accountLogList : [{"availableAfter":10000000,"gmtTrans":"2017-11-03 18:30:10","id":1320557,"memo":"运营","subTransCode":"TRANSFER_NORMAL","subTransName":"普通转账","transAmount":10000000,"transCode":"TRANSFER","transDirection":{"message":"收入","name":"I","value":0}}]
     * jumpLogin : false
     * nowDate : 2017-11-04 14:49:11
     * paginator : {"items":1,"itemsPerPage":20,"page":1,"pages":1}
     * success : true
     */

    private String nowDate;
    private PaginatorBean paginator;
    private List<AccountLogListBean> accountLogList;


    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }


    public List<AccountLogListBean> getAccountLogList() {
        return accountLogList;
    }

    public void setAccountLogList(List<AccountLogListBean> accountLogList) {
        this.accountLogList = accountLogList;
    }

    @Override
    public void operatorData() {
        if(accountLogList!=null&&accountLogList.size()>0){
            for (int i = 0; i <accountLogList.size() ; i++) {
                DecimalFormat format = new DecimalFormat("#0.0#");
//                String s =format.format()
            }
        }
    }


    public static class AccountLogListBean {
        /**
         * availableAfter : 10000000
         * gmtTrans : 2017-11-03 18:30:10
         * id : 1320557
         * memo : 运营
         * subTransCode : TRANSFER_NORMAL
         * subTransName : 普通转账
         * transAmount : 10000000
         * transCode : TRANSFER
         * transDirection : {"message":"收入","name":"I","value":0}
         */

        private BigDecimal availableAfter;
        private String gmtTrans;
        private int id;
        private String memo;
        private String subTransCode;
        private String subTransName;
        private BigDecimal transAmount;
        private String transCode;
        private TransDirectionBean transDirection;



        public String getGmtTrans() {
            return gmtTrans;
        }

        public void setGmtTrans(String gmtTrans) {
            this.gmtTrans = gmtTrans;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getSubTransCode() {
            return subTransCode;
        }

        public void setSubTransCode(String subTransCode) {
            this.subTransCode = subTransCode;
        }

        public String getSubTransName() {
            return subTransName;
        }

        public void setSubTransName(String subTransName) {
            this.subTransName = subTransName;
        }

        public BigDecimal getAvailableAfter() {
            return availableAfter;
        }

        public void setAvailableAfter(BigDecimal availableAfter) {
            this.availableAfter = availableAfter;
        }

        public BigDecimal getTransAmount() {
            return transAmount;
        }

        public void setTransAmount(BigDecimal transAmount) {
            this.transAmount = transAmount;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public TransDirectionBean getTransDirection() {
            return transDirection;
        }

        public void setTransDirection(TransDirectionBean transDirection) {
            this.transDirection = transDirection;
        }

        public static class TransDirectionBean {
            /**
             * message : 收入
             * name : I
             * value : 0
             */

            private String message;
            private String name;
            private int value;

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

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }


}

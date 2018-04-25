package com.android.xjq.bean.myzhuwei;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.List;
import java.util.Map;

/**
 * Created by kokuma on 2017/11/5.
 */

public class ZhuweiListBean {

        private boolean success;
        private String nowDate;
        private boolean jumpLogin;
        private PaginatorBean paginator;
        private Map<String,ZhuweiDetailBean.GameBoardBean> gameBoardMap;
        private List<ZhuweiDetailBean.PurchaseOrderBean> purchaseOrderSummaryClientSimpleList;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getNowDate() {
            return nowDate;
        }

        public void setNowDate(String nowDate) {
            this.nowDate = nowDate;
        }

        public boolean isJumpLogin() {
            return jumpLogin;
        }

        public void setJumpLogin(boolean jumpLogin) {
            this.jumpLogin = jumpLogin;
        }

        public PaginatorBean getPaginator() {
            return paginator;
        }

        public void setPaginator(PaginatorBean paginator) {
            this.paginator = paginator;
        }

        public Map<String,ZhuweiDetailBean.GameBoardBean> getGameBoardMap() {
            return gameBoardMap;
        }

        public void setGameBoardMap(Map<String,ZhuweiDetailBean.GameBoardBean> gameBoardMap) {
            this.gameBoardMap = gameBoardMap;
        }

        public List<ZhuweiDetailBean.PurchaseOrderBean> getPurchaseOrderSummaryClientSimpleList() {
            return purchaseOrderSummaryClientSimpleList;
        }

        public void setPurchaseOrderSummaryClientSimpleList(List<ZhuweiDetailBean.PurchaseOrderBean> purchaseOrderSummaryClientSimpleList) {
            this.purchaseOrderSummaryClientSimpleList = purchaseOrderSummaryClientSimpleList;
        }


}

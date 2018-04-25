package com.android.xjq.bean.dynamic;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.List;

/**
 * Created by zaozao on 2018/1/29.
 * 用处
 */

public class ImpressionDataBean {

    /**
     * jumpLogin : false
     * list : [{"createType":"USER","creatorUserId":"8201711248722668","id":"4000292253468300440000007889","likeCount":3,"objectId":"15622865","objectType":"SUBJECT","tagId":"1","tagName":"6666"},{"createType":"USER","creatorUserId":"8201711248722668","id":"4000305987155200440000009740","likeCount":3,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000305986507800440000009700","tagName":"666"},{"creatorUserId":"8201711288725846","id":"4000548890305500440970018688","likeCount":1,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000548890305300440970016400","tagName":"4     新  加"},{"creatorUserId":"8201712158726308","id":"4000375804993800440970015305","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000375804989100440970013857","tagName":"标签创建api"},{"creatorUserId":"8201711288725846","id":"4000548446190800440970012435","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000548446186300440970017916","tagName":"3"},{"creatorUserId":"8201711288725846","id":"4000548513828500440970011480","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000548513828200440970016000","tagName":"12赞"},{"createType":"USER","creatorUserId":"8201711248722668","id":"4000306002451000440000008060","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000305986507800440000009700","tagName":"666"},{"creatorUserId":"8201711288725846","id":"4000548774102700440970015269","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000548774102500440970014104","tagName":"3     新  加"},{"creatorUserId":"8201711288725846","id":"4000549956997200440970015997","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"4000549956996800440970019121","tagName":"新增的标签1"},{"createType":"USER","creatorUserId":"8201711248722668","id":"1","likeCount":0,"objectId":"15622865","objectType":"SUBJECT","tagId":"1","tagName":"6666"}]
     * nowDate : 2018-02-26 16:25:30
     * paginator : {"items":10,"itemsPerPage":20,"page":1,"pages":1}
     * success : true
     */

    private PaginatorBean paginator;
    private List<ImpressionTagSimple> list;

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }


    public List<ImpressionTagSimple> getList() {
        return list;
    }

    public void setList(List<ImpressionTagSimple> list) {
        this.list = list;
    }


    public static class ImpressionTagSimple {
        /**
         * createType : USER
         * creatorUserId : 8201711248722668
         * id : 4000292253468300440000007889
         * likeCount : 3
         * objectId : 15622865
         * objectType : SUBJECT
         * tagId : 1
         * tagName : 6666
         */

        public String createType;
        public String creatorUserId;
        public String id;
        public int likeCount;
        public String objectId;
        public String objectType;
        public String tagId;
        public String tagName;
        public boolean liked;

    }
}

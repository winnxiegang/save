package com.android.xjq.bean.subject;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.xjq.bean.SubjectBean2;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.bean.UserMedalLevelBean;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouyi on 2016/1/7 17:45.
 */
public class SubjectDetailHeadBean implements BaseOperator {

    private String shareUrl;

    private boolean success;
    private String nowDate;

//    private SubjectSimpleBean subjectSimple;
    private SubjectBean2 subjectSimple;
    public List<UserMedalLevelBean> userMedalLevelList;
    /**
     * 勋章信息
     */
    private HashMap<String, List<JczjMedalBean>> userMedalClientSimpleMap;
    /**
     * 标签信息
     */
    private HashMap<String, List<SubjectTag>> subjectTagMap;

    private JczqDataBean basketballRace;

    private JczqDataBean footballRace;


    @Expose
    private String jcdxType;

    @Expose
    private NormalObject PredictResultStatus;

    public JczqDataBean getBasketballRace() {
        return basketballRace;
    }

    public void setBasketballRace(JczqDataBean basketballRace) {
        this.basketballRace = basketballRace;
    }

    public JczqDataBean getFootballRace() {
        return footballRace;
    }

    public void setFootballRace(JczqDataBean footballRace) {
        this.footballRace = footballRace;
    }

    public String getJcdxType() {
        return jcdxType;
    }

    public void setJcdxType(String jcdxType) {
        this.jcdxType = jcdxType;
    }

    public NormalObject getPredictResultStatus() {
        return PredictResultStatus;
    }

    public void setPredictResultStatus(NormalObject predictResultStatus) {
        PredictResultStatus = predictResultStatus;
    }

    public HashMap<String, List<JczjMedalBean>> getUserMedalClientSimpleMap() {
        return userMedalClientSimpleMap;
    }

    public void setUserMedalClientSimpleMap(HashMap<String, List<JczjMedalBean>> userMedalClientSimpleMap) {
        this.userMedalClientSimpleMap = userMedalClientSimpleMap;
    }


    public HashMap<String, List<SubjectTag>> getSubjectTagMap() {
        return subjectTagMap;
    }

    public void setSubjectTagMap(HashMap<String, List<SubjectTag>> subjectTagMap) {
        this.subjectTagMap = subjectTagMap;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }


    public String getShareUrl() {
        return shareUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public SubjectBean2 getSubjectSimple() {
        return subjectSimple;
    }

    public void setSubjectSimple(SubjectBean2 subjectSimple) {
        this.subjectSimple = subjectSimple;
    }

    @Override
    public void operatorData() {
//        //添加勋章信息
//        if (userMedalClientSimpleMap != null && userMedalClientSimpleMap.size() != 0) {
//            if (userMedalClientSimpleMap.containsKey(subjectSimple.getUserId())) {
//                subjectSimple.setUserMedalBeanList(userMedalClientSimpleMap.get(subjectSimple.getUserId()));
//            }
//        }
//        //添加标签信息
//        if (subjectTagMap != null && subjectTagMap.size() > 0) {
//            if (subjectTagMap.containsKey(subjectSimple.getSubjectId())) {
//                for (SubjectTag subjectTag : subjectTagMap.get(subjectSimple.getSubjectId())) {
//                    if ("ELITE".equals(subjectTag.getCode())) {
//                        subjectSimple.setElite(true);
//                    } else if (SubjectTypeEnum.SubjectSubTypeEnum.contains(subjectTag.getCode())) {
//                        subjectSimple.setAnalysisCode(subjectTag.getCode());
//                    }
//                    //竞彩大学新增的，，，皂皂20170809
//                    else if(JCDXArticleTypeEnum.contains(subjectTag.getCode())){
//                        jcdxType = subjectTag.getCode();
////                        if(subjectTag.getCode().equals(JCDXArticleTypeEnum.ARTICLE_PREDICT.name())){
////                            if(jczqAsiaPlateUserRacePredictClientSimple!=null
////                                    &&jczqAsiaPlateUserRacePredictClientSimple.getAwardResultList()!=null
////                                    &&jczqAsiaPlateUserRacePredictClientSimple.getAwardResultList().size()>0){
////                                PredictResultStatus = jczqAsiaPlateUserRacePredictClientSimple.getAwardResultList().get(0);
////                            }
////                        }
//                    }
//                }
//            }
//        }


//        if(jczqAsiaPlateUserRacePredictClientSimple!=null){
//            if(jczqAsiaPlateUserRacePredictClientSimple.getAllOptionSpMap()!=null){
//                jczqAsiaPlateUserRacePredictClientSimple.getAllOptionSpMap().keySet();
//
//                if(jczqAsiaPlateUserRacePredictClientSimple.getAllOptionSpMap().containsKey("SF_WIN")){
//
//                    setSpBean("SF_WIN");
//                }
//
//                if(jczqAsiaPlateUserRacePredictClientSimple.getAllOptionSpMap().containsKey("SF_LOST")){
//                    setSpBean("SF_LOST");
//                }
//
//            }
//        }
    }
//
//    private void setSpBean(String key){
//
//        ArticleDetailSpShowBean bean= new ArticleDetailSpShowBean();
//
//        bean.setOptionName(key);
//
//        bean.setSp(jczqAsiaPlateUserRacePredictClientSimple.getAllOptionSpMap().get(key));
//
//        if(jczqAsiaPlateUserRacePredictClientSimple.getOptions().get(0).getName().equals(key)){
//
//            bean.setHaveBackGroungColor(true);
//
//            switch (RacePredictStatusEnum.safeValueOf(jczqAsiaPlateUserRacePredictClientSimple.getPredictStatus().getName())){
//                case DRAW://走
//                    bean.setBackGround(R.drawable.shape_all_blue_conner5);
//                    break;
//                case NOT_CORRECT://输
//                    bean.setBackGround(R.drawable.shape_all_gray_conner5);
//                    break;
//                case CORRECT://赢
//                case READY_CALCULATE://待计算
//                    bean.setBackGround(R.drawable.shape_all_red_conner5);
//                default:
//                    break;
//            }
//        }
//
//        articleShowSpList.add(bean);
//
//    }

    public static class SubjectSimpleBean {
        private String summary;
        private String content;
        private int replyCount;
        private int likeCount;
        private String gmtCreate;
        private boolean liked;
        private boolean delete;
        private boolean vip;
        private int collectId;

        private String userId;

        private ObjectTypeBean objectType;

        private String userLogoUrl;

        private String userName;

        private String loginName;

        private String subjectId;

        private String title;

        private boolean commentOff;

        private boolean setTop;

        @Expose
        private List<JczjMedalBean> userMedalBeanList;

        @Expose
        private String analysisCode;

        @Expose
        private boolean isElite;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public boolean isSetTop() {
            return setTop;
        }

        public void setSetTop(boolean setTop) {
            this.setTop = setTop;
        }

        public String getAnalysisCode() {
            return analysisCode;
        }

        public void setAnalysisCode(String analysisCode) {
            this.analysisCode = analysisCode;
        }

        public boolean isElite() {
            return isElite;
        }

        public void setElite(boolean elite) {
            isElite = elite;
        }

        public List<JczjMedalBean> getUserMedalBeanList() {
            return userMedalBeanList;
        }

        public void setUserMedalBeanList(List<JczjMedalBean> userMedalBeanList) {
            this.userMedalBeanList = userMedalBeanList;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public void setCollectId(int collectId) {
            this.collectId = collectId;
        }

        public void setObjectType(ObjectTypeBean objectType) {
            this.objectType = objectType;
        }

        public String getSummary() {
            return summary;
        }

        public String getContent() {

            return content;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public boolean isLiked() {
            return liked;
        }

        public boolean isDelete() {
            return delete;
        }

        public int getCollectId() {
            return collectId;
        }

        public ObjectTypeBean getObjectType() {
            return objectType;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCommentOff() {
            return commentOff;
        }

        public void setCommentOff(boolean commentOff) {
            this.commentOff = commentOff;
        }

        public static class ObjectTypeBean {
            private Object value;
            private String message;
            private String name;

            public void setValue(Object value) {
                this.value = value;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getValue() {
                return value;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}

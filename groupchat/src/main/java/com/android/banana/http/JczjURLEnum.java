package com.android.banana.http;

import com.android.banana.groupchat.AppParam;
import com.android.httprequestlib.BaseRequestHttpName;


/**
 * Created by zhouyi on 2015/11/3 11:28.
 */
public enum JczjURLEnum implements BaseRequestHttpName {

    USER_LOGO_URL(AppParam.S_URL, "USER_LOGO_URL"),

    LOTTERY_SHOW(AppParam.S_URL, "LOTTERY_SHOW"),

    DISCOVERY_ANALYSIS(AppParam.S_URL, "DISCOVERY_ANALYSIS"),

    EDITOR_RECOMMEND(AppParam.S_URL, "EDITOR_RECOMMEND"),

    OAP_AUTH_LOGIN_INFO_VALIDATE(AppParam.S_URL, "OAP_AUTH_LOGIN_INFO_VALIDATE"),

    LOTTERY_BET_LIST_QUERY_BY_USER(AppParam.S_URL, "LOTTERY_BET_LIST_QUERY_BY_USER"),

    //禁言
    POST_FORBIDDEN(AppParam.S_URL, "POST_FORBIDDEN"),

    /*
    * 广告页
    * */
    HOME_PAGE(AppParam.S_URL, "HOME_PAGE"),

    /**
     * 获取搜索用户
     */
    USER_INFO_QUERY(AppParam.S_URL, "USER_INFO_QUERY"),
    /**
     * 获取关注列表
     */
    USER_ATTENTIONS_INFO(AppParam.S_URL, "USER_ATTENTIONS_INFO"),
    /**
     * 获取粉丝列表
     */
    USER_FANS_INFO(AppParam.S_URL, "USER_FANS_INFO"),
    /**
     * 抢红包信息查询
     */
    FETCH_COUPON_INFO_QUERY(AppParam.S_URL, "FETCH_COUPON_INFO_QUERY"),
    /**
     * 红包可抢数查询
     */
    COUPON_AVAILABLE_NUM_QUERY(AppParam.S_URL, "COUPON_AVAILABLE_NUM_QUERY"),
    /**
     * 数据埋点列表查询
     */
    COUNT_POINT(AppParam.S_URL, "COUNT_POINT"),
    /**
     * 判断是否可抢红包
     */
    QUERY_AVAILABLE_COUPON(AppParam.S_URL, "QUERY_AVAILABLE_COUPON"),

    /**
     * 拆红包动作
     */
    USER_FETCH_COUPON(AppParam.S_URL, "USER_FETCH_COUPON"),

    JCZQ_MATCH_QUERY(AppParam.S_URL, "JCZQ_MATCH_QUERY"),


    SERVICE_TIMESTAMP_QUERY(AppParam.S_URL, "SERVICE_TIMESTAMP_QUERY"),

    IMAGE_TEMP_UPLOAD(AppParam.S_URL, "IMAGE_TEMP_UPLOAD"),


    /**
     * 消息条数查询
     */
    MESSAGE_NOTICE_QUERY(AppParam.S_URL, "MESSAGE_NOTICE_QUERY"),


    USER_LOGO_EDIT(AppParam.S_URL, "USER_LOGO_EDIT"),

    MY_PURCHASE_ORDER_QUERY(AppParam.S_URL, "MY_PURCHASE_ORDER_QUERY"),

    LOGOUT(AppParam.S_URL, "LOGOUT"),

    CLIENT_PUSH_SETTINGS_QUERY(AppParam.S_URL, "CLIENT_PUSH_SETTINGS_QUERY"),

    RACE_TEAM_RANK_MAP_QUERY(AppParam.BT_API_S_URL, "RACE_TEAM_RANK_MAP_QUERY"),

    TEAM_CURRENT_RANK_QUERY(AppParam.FT_API_S_URL, "TEAM_CURRENT_RANK_QUERY"),

    CLIENT_PUSH_SETTINGS_MANAGE(AppParam.S_URL, "CLIENT_PUSH_SETTINGS_MANAGE"),

    //点击红包按钮时候的验证
    COUPON_CREATE_BEFORE_VALIDATE(AppParam.S_URL, "COUPON_CREATE_BEFORE_VALIDATE"),
    //留言查询接口
    MESSAGE_BOARD_QUERY(AppParam.S_URL, "MESSAGE_BOARD_QUERY"),
    //红包创建接口
    COUPON_CREATE(AppParam.S_URL, "COUPON_CREATE"),
    //红包记录接口
    DIRECT_COUPON_QUERY(AppParam.S_URL, "DIRECT_COUPON_QUERY"),

    LOGIN_KEY_ENABLED(AppParam.S_URL, "LOGIN_KEY_ENABLED"),

    CAROUSEL_WALL(AppParam.S_URL, "CAROUSEL_WALL"),

    USER_MESSAGE_NOTICE(AppParam.S_URL, "USER_MESSAGE_NOTICE"),

    USER_SEND_COUPON_QUERY(AppParam.S_URL, "USER_SEND_COUPON_QUERY"),

    USER_RECEIVE_COUPON_QUERY(AppParam.S_URL, "USER_RECEIVE_COUPON_QUERY"),

    COUPON_DETAIL_QUERY(AppParam.S_URL, "COUPON_DETAIL_QUERY"),

    COMMENT_SET_TOP_OPERATE(AppParam.S_URL, "COMMENT_SET_TOP_OPERATE"),

    COUPON_QUERY_POSITION(AppParam.S_URL, "COUPON_QUERY_POSITION"),

    EUR_ODDS_QUERY(AppParam.FT_API_S_URL, "EUR_ODDS_QUERY"),

    ASIA_ODDS_QUERY(AppParam.FT_API_S_URL, "ASIA_ODDS_QUERY"),

    OVERUNDER_ODDS_QUERY(AppParam.FT_API_S_URL, "OVERUNDER_ODDS_QUERY"),

    EUR_HISTORY_ODDS_QUERY(AppParam.FT_API_S_URL, "EUR_HISTORY_ODDS_QUERY"),

    ASIA_HISTORY_ODDS_QUERY(AppParam.FT_API_S_URL, "ASIA_HISTORY_ODDS_QUERY"),

    OVERUNDER_HISTORY_ODDS_QUERY(AppParam.FT_API_S_URL, "OVERUNDER_HISTORY_ODDS_QUERY"),

    TEAM_LEA_RANK_QUERY(AppParam.FT_API_S_URL, "TEAM_LEA_RANK_QUERY"),

    /**
     * 足球数据
     */
    RACE_EVENT_QUERY(AppParam.FT_API_S_URL, "RACE_EVENT_QUERY"),

    //获取小析数据
    LATEST_RACES_ODDS_QUERY(AppParam.FT_API_S_URL, "LATEST_RACES_ODDS_QUERY"),

    //查询评论数
    COMMENT_QUERY_COUNT(AppParam.S_URL, "COMMENT_QUERY_COUNT"),

    //赛事析头部信息查询
    RACE_DETAIL_QUERY(AppParam.FT_API_S_URL, "RACE_DETAIL_QUERY"),

    TEAM_LAST_AGAINST_RACES_QUERY(AppParam.FT_API_S_URL, "TEAM_LAST_AGAINST_RACES_QUERY"),

    TEAM_LATEST_RACES_QUERY(AppParam.FT_API_S_URL, "TEAM_LATEST_RACES_QUERY"),

    TEAM_FUTURE_RACES_QUERY(AppParam.FT_API_S_URL, "TEAM_FUTURE_RACES_QUERY"),

    TEAM_RANK_QUERY(AppParam.FT_API_S_URL, "TEAM_RANK_QUERY"),

    TEAM_LEA_MATCH_RANK_QUERY(AppParam.FT_API_S_URL, "TEAM_LEA_MATCH_RANK_QUERY"),

    JCZQ_DATA_QUERY_BY_BIZID(AppParam.S_URL, "JCZQ_DATA_QUERY_BY_BIZID"),

    JCZQ_DATA_PREDICT(AppParam.S_URL, "JCZQ_DATA_PREDICT"),

    TAG_OR_LEVEL_COMMON_QUERY(AppParam.S_URL, "TAG_OR_LEVEL_COMMON_QUERY"),

    JCZQ_PREDICT_BEFORE_CREATE(AppParam.S_URL, "JCZQ_PREDICT_BEFORE_CREATE"),

    FT_DYNAMIC_SOCRE(AppParam.CZ_FT_URL, "FT_DYNAMIC_SOCRE"),

    DISCOVERY_RECENT(AppParam.S_URL, "DISCOVERY_RECENT"),

    JCLQ_DATA_QUERY_BY_BIZID(AppParam.S_URL, "JCLQ_DATA_QUERY_BY_BIZID"),

    JCLQ_TEAM_LAST_AGAINST_RACES_QUERY(AppParam.BT_API_S_URL, "LATEST_AGAINTST_RACES_QUERY"),

    JCLQ_TEAM_LATEST_RACES_QUERY(AppParam.BT_API_S_URL, "TEAM_LATEST_RACES_QUERY"),

    JCLQ_TEAM_RANK_COUNT_QUERY(AppParam.BT_API_S_URL, "TEAM_RANK_COUNT_QUERY"),

    JCLQ_TEAM_FUTURE_RACES_QUERY(AppParam.BT_API_S_URL, "TEAM_FUTURE_RACES_QUERY"),

    JCLQ_TEAM_RANK_QUERY(AppParam.BT_API_S_URL, "TEAM_RANK_QUERY"),

    JCLQ_EUR_ODDS_QUERY(AppParam.BT_API_S_URL, "EUR_ODDS_QUERY"),

    JCLQ_LET_ODDS_QUERY(AppParam.BT_API_S_URL, "LET_ODDS_QUERY"),

    JCLQ_OU_ODDS_QUERY(AppParam.BT_API_S_URL, "OVER_UNDER_ODDS_QUERY"),

    JCLQ_OVER_UNDER_HISTORY_ODDS_QUERY(AppParam.BT_API_S_URL, "OVER_UNDER_HISTORY_ODDS_QUERY"),

    JCLQ_EUR_HISTORY_ODDS_QUERY(AppParam.BT_API_S_URL, "EUR_HISTORY_ODDS_QUERY"),

    JCLQ_LET_HISTORY_ODDS_QUERY(AppParam.BT_API_S_URL, "LET_HISTORY_ODDS_QUERY"),

    JCLQ_LATEST_RACES_ODDS_QUERY(AppParam.BT_API_S_URL, "LATEST_RACES_ODDS_QUERY"),

    JCLQ_DYNAMIC_SCORE_DATA(AppParam.BT_API_S_URL, "DYNAMIC_SCORE_DATA"),

    COPY_PURCHASE_PLAN_CREATE(AppParam.S_URL, "COPY_PURCHASE_PLAN_CREATE"),

    COPY_PURCHASE_PLAN_RECORDS_QUERY(AppParam.S_URL, "COPY_PURCHASE_PLAN_RECORDS_QUERY"),

    COPY_PURCHASE_PLAN_DETAIL(AppParam.S_URL, "COPY_PURCHASE_PLAN_DETAIL"),

    COPY_PURCHASE_PLAN_CANCEL(AppParam.S_URL, "COPY_PURCHASE_PLAN_CANCEL"),

    USER_MEDAL_HOME_PAGE_QUERY(AppParam.S_URL, "USER_MEDAL_HOME_PAGE_QUERY"),

    RULE_DESCRIPTION_CHANNEL_QUERY(AppParam.S_URL, "RULE_DESCRIPTION_CHANNEL_QUERY"),

    CMS_INFO_CONTENT_QUERY(AppParam.S_URL, "CMS_INFO_CONTENT_QUERY"),

    MEDAL_CONFIG_QUERY(AppParam.S_URL, "MEDAL_CONFIG_QUERY"),

    HOT_SEARCH_USER(AppParam.S_URL, "HOT_SEARCH_USER"),

    CMS_INFO_QUERY(AppParam.S_URL, "CMS_INFO_QUERY"),

    FRESH_TOPIC(AppParam.S_URL, "FRESH_TOPIC"),

    DAILY_EXCELLENT_PROJECT(AppParam.S_URL, "DAILY_EXCELLENT_PROJECT"),

    APP_COLUMN_INFO_QUERY(AppParam.S_URL, "APP_COLUMN_INFO_QUERY"),

    JCLQ_MATCH_QUERY(AppParam.S_URL, "JCLQ_MATCH_QUERY"),

    PURCHASE_ORDER_COUNT_PROFIT(AppParam.S_URL, "PURCHASE_ORDER_COUNT_PROFIT"),

    GET_PURCHASE_ORDER_COUNT_DATA(AppParam.S_URL, "GET_PURCHASE_ORDER_COUNT_DATA"),

    FOCUS_RACE_LIST(AppParam.S_URL, "FOCUS_RACE_LIST"),

    GET_RACE_COUNT_DATA(AppParam.S_URL, "GET_RACE_COUNT_DATA"),

    GET_USER_PUR_ORDER_DAY_COUNT_DATA(AppParam.S_URL, "GET_USER_PUR_ORDER_DAY_COUNT_DATA"),

    CHARM_LIST_INFLUENTIAL_PERSON(AppParam.S_URL, "CHARM_LIST_INFLUENTIAL_PERSON"),

    CHARM_LIST_HISTORY_SHOW(AppParam.S_URL, "CHARM_LIST_HISTORY_SHOW"),

    CHARM_LIST_HISTORY_PRIZE_FEE(AppParam.S_URL, "CHARM_LIST_HISTORY_PRIZE_FEE"),

    CHARM_LIST_HISTORY_PROFIT_RED(AppParam.S_URL, "CHARM_LIST_HISTORY_PROFIT_RED"),

    CHARM_LIST_HISTORY_SUM_PRIZE_FEE(AppParam.S_URL, "CHARM_LIST_HISTORY_SUM_PRIZE_FEE"),

    HALL_OF_FAME(AppParam.S_URL, "HALL_OF_FAME"),

    CHARM_LIST_HISTORY_QUERY_PRIZE_FEE_BY_PURCHASE_NO(AppParam.S_URL, "CHARM_LIST_HISTORY_QUERY_PRIZE_FEE_BY_PURCHASE_NO"),

    CHARM_LIST_INFLUENTAIL_PERSON_TOTAL_FEE_QUERY(AppParam.S_URL, "CHARM_LIST_INFLUENTAIL_PERSON_TOTAL_FEE_QUERY"),

    GET_USER_PUR_ORDER_DAY_COUNT_TOTAL_FEE(AppParam.S_URL, "GET_USER_PUR_ORDER_DAY_COUNT_TOTAL_FEE"),

    FROMINENT_TIMELINE_CONFIG_QUERY(AppParam.S_URL, "FROMINENT_TIMELINE_CONFIG_QUERY"),

    APP_FUNCTION_DISPLAY_CONFIG_QUERY(AppParam.S_URL, "APP_FUNCTION_DISPLAY_CONFIG_QUERY"),


    ANDROID_UPDATE_CONFIG(AppParam.S_URL, "ANDROID_UPDATE_CONFIG"),

    USER_GUESS_APP_LOGIN_SIGN_QUERY(AppParam.S_URL, "USER_GUESS_APP_LOGIN_SIGN_QUERY"),

    /**
     * 阵营选择
     */
    INCREASE_THED_USER_TEAM_DATA(AppParam.S_URL, "INCREASE_THED_USER_TEAM_DATA"),

    /**
     * 聊天创建
     */
    GROUP_MESSAGE_SEND(AppParam.S_URL, "GROUP_MESSAGE_SEND"),


    /**
     * 进入聊天室
     */
    GROUP_USER_ENTRY_ENTER(AppParam.S_URL, "GROUP_USER_ENTRY_ENTER"),

    /**
     * 退出聊天室
     */
    GROUP_USER_ENTRY_LEAVE(AppParam.S_URL, "GROUP_USER_ENTRY_LEAVE"),

    /**
     * 拉取聊天记录
     */
    GROUP_MESSAGE_QUERY_BY_USER_LAST_READ(AppParam.S_URL, "GROUP_MESSAGE_QUERY_BY_USER_LAST_READ"),

    /**
     * 下拉加载历史记录
     */
    GROUP_MESSAGE_QUERY_BY_USER(AppParam.S_URL, "GROUP_MESSAGE_QUERY_BY_USER"),
    /**
     * 获取分享的url和发红包权限，广告位的数据
     */
    NORMAL_AND_ACTIVITY_DATA_QUERY(AppParam.S_URL, "NORMAL_AND_ACTIVITY_DATA_QUERY"),

    /**
     * 聊天记录
     */
    CHAT_CONTENT_QUERY(AppParam.S_URL, "CHAT_CONTENT_QUERY"),

    /**
     * 拉取聊天记录前验证用户登录
     */
    USER_AUTH_LOGIN_CHAT_VALIDATE(AppParam.S_URL, "USER_AUTH_LOGIN_CHAT_VALIDATE"),

    /**
     * 发送聊天内容之前的权限校验
     */
    USER_AUTHORITY_OR_CHAT_STATE_QUERY(AppParam.S_URL, "USER_AUTHORITY_OR_CHAT_STATE_QUERY"),
    /**
     * 发送聊天信息
     */
    GROUP_USER_ENTRY_BASED_MESSAGE_SEND(AppParam.S_URL, "GROUP_USER_ENTRY_BASED_MESSAGE_SEND"),
    /**
     * 根据userid查询 用户主客队标签
     */
    LIVE_CHAT_USER_INFO_QUERY_BY_USER_ID(AppParam.S_URL, "LIVE_CHAT_USER_INFO_QUERY_BY_USER_ID"),

    /**
     * 弹幕
     */
    EDITOR_LIVE_BARRAGE_QUERY(AppParam.S_URL, "EDITOR_LIVE_BARRAGE_QUERY"),

    /**
     * 近五场比赛数据
     */
    JCLQ_SEASON_RACE_LATEST_RESULT_QUERY(AppParam.BT_API_S_URL, "SEASON_RACE_LASTEST5_RESULT_QUERY"),

    JCZQ_SEASON_RACE_LATEST_RESULT_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_LASTEST5_RESULT_QUERY"),

    /**
     * 比分直播竞彩篮球数据
     */
    SEASON_RACE_TECH_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_TECH_QUERY"),

    //直播入口判断
    GAME_AND_COUPON_FETCH_ENTRACE(AppParam.S_URL, "GAME_AND_COUPON_FETCH_ENTRACE"),

    MY_QUIZZES_QUERY(AppParam.S_URL, "MY_QUIZZES_QUERY"),


    JCZQ_DGP_ENTRY_GROUP_QUERY(AppParam.S_URL, "JCZQ_DGP_ENTRY_GROUP_QUERY"),

    SUPPORT_APP_TYPE_QUERY(AppParam.S_URL, "SUPPORT_APP_TYPE_QUERY"),


    //积分排行榜改版后接口
    TEAM_LEA_RANK_EXTRA_QUERY(AppParam.FT_API_S_URL, "TEAM_LEA_RANK_EXTRA_QUERY"),

    //盘赔回查赔率公司查询
    SAME_ODDS_COMPANY_AND_ODDS_QUERY(AppParam.BT_API_S_URL, "SAME_ODDS_COMPANY_AND_ODDS_QUERY"),


    //爆料查询
    SEASON_RACE_NEWS_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_NEWS_QUERY"),

    //伤停查询
    SEASON_RACE_INJURY_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_INJURY_QUERY"),


    //获取直播源列表
    SEASON_RACE_LIVE_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_LIVE_QUERY"),

    //欧赔盘赔回查相同首赔赛事查询
    APP_SAME_EUR_ODDS_RACE_QUERY(AppParam.FT_API_S_URL, "APP_SAME_EUR_ODDS_RACE_QUERY"),
    //亚盘盘赔回查盘口变化赛事查询
    APP_SAME_ASIA_ODDS_RACE_QUERY(AppParam.FT_API_S_URL, "APP_SAME_ASIA_ODDS_RACE_QUERY"),

    //大小盘盘赔回查盘口变化赛事查询
    APP_SAME_OVER_UNDER_ODDS_RACE_QUERY(AppParam.FT_API_S_URL, "APP_SAME_OVER_UNDER_ODDS_RACE_QUERY"),

    //成交指数
    BETFAIR_TRADE_QUERY(AppParam.FT_API_S_URL, "BETFAIR_TRADE_QUERY"),

    //动画直播技术统计
    RACE_TEAM_TECH_AND_EVENT_QUERY(AppParam.FT_API_S_URL, "RACE_TEAM_TECH_AND_EVENT_QUERY"),

    //动画直播头部数据接口
    FLASH_SCORE_QUERY(AppParam.FT_API_S_URL, "FLASH_SCORE_QUERY"),

    //ft系统的获取近五场
    SEASON_RACE_LASTEST5_RESULT_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_LASTEST5_RESULT_QUERY"),

    //SEASON_RACE_STATUS_QUERY赛室状态查询
    SEASON_RACE_STATUS_QUERY(AppParam.FT_API_S_URL, "SEASON_RACE_STATUS_QUERY"),

    //评论书目查询
    COMMENT_AND_COMMENT_REPLY_COUNT_QUERY(AppParam.S_URL, "COMMENT_AND_COMMENT_REPLY_COUNT_QUERY"),


    //新接口从这里开始,上面的要被删减的
    /*---------------------------------创建群组-------------------------*/
     /*群聊设置页*/
    RELATION_GROUP_CHAT_ROOM_MANAGE_QUERY(AppParam.S_URL, "RELATION_GROUP_CHAT_ROOM_MANAGE_QUERY"),

    //聊天室成员查询
    GROUP_MEMBER_QUERY(AppParam.S_URL, "GROUP_MEMBER_QUERY"),

    /*禁言用户列表查询*/
    USER_FORBIDDEN_QUERY(AppParam.S_URL, "USER_FORBIDDEN_QUERY"),

    /*取消指定人员的禁言*/
    USER_FORBBIDEN_ACTION_CANCEL(AppParam.S_URL, "USER_FORBBIDEN_ACTION_CANCEL"),

    /*指定人员指定时间禁言*/
    USER_FORBBIDEN_ACTION_OPERATE(AppParam.S_URL, "USER_FORBBIDEN_ACTION_OPERATE"),

    //发言设置页
    GROUP_USER_PERMISSION_QUERY(AppParam.S_URL, "GROUP_USER_PERMISSION_QUERY"),
    //消息列表
    GROUP_MESSAGE_COUNT_QUERY(AppParam.S_URL, "GROUP_MESSAGE_COUNT_QUERY"),

    //赛事直播 头部 篮球足球 合并接口头部信息小平
    CHAT_TOPIC_INFO_QUERY_BY_OBJECT_ID(AppParam.S_URL, "CHAT_TOPIC_INFO_QUERY_BY_OBJECT_ID"),

    //删除发言人
    GROUP_MESSAGE_ALLOWED_SENDER_REMOVE(AppParam.S_URL, "GROUP_MESSAGE_ALLOWED_SENDER_REMOVE"),

    //删除成员
    CHAT_MEMBER_REMOVE_RELATION_GROUP(AppParam.S_URL, "CHAT_MEMBER_REMOVE_RELATION_GROUP"),

    //搜索成员
    GROUP_USER_JOINED_QUERY(AppParam.S_URL, "GROUP_USER_JOINED_QUERY"),

    //创建公告
    MODIFY_NOTICE(AppParam.S_URL, "MODIFY_NOTICE"),//groupId,notice


    /*群聊i删除消息*/
    GROUP_MESSAGE_DELETE(AppParam.S_URL, "GROUP_MESSAGE_DELETE"),

    /*加入群聊*/
    USER_ENTER_CHAT_ROOM_OPERATE(AppParam.S_URL, "USER_ENTER_CHAT_ROOM_OPERATE"),


    //消息列表取消置顶
    PAYLOAD_ORDER_BY_USER_DISABLED(AppParam.S_URL, "PAYLOAD_ORDER_BY_USER_DISABLED"),

    //消息列表置顶
    PAYLOAD_ORDER_BY_USER_SET(AppParam.S_URL, "PAYLOAD_ORDER_BY_USER_SET"),

    //群聊通知
    GROUP_MESSAGE_NOTICE(AppParam.S_URL, "GROUP_MESSAGE_NOTICE"),

    //退出聊天室
    GROUP_USER_QUIT(AppParam.S_URL, "GROUP_USER_QUIT"),

    //添加发言人
    GROUP_MESSAGE_ALLOWED_SENDER_ADD(AppParam.S_URL, "GROUP_MESSAGE_ALLOWED_SENDER_ADD"),

    //聊天室列表
    RELATION_GROUP_CHAT_ROOM_QUERY_LIST(AppParam.S_URL, "RELATION_GROUP_CHAT_ROOM_QUERY_LIST"),

    //消息列表删除
    MODIFY_MESSAGE_LAST_READ_TO_NEWEST(AppParam.S_URL, "MODIFY_MESSAGE_LAST_READ_TO_NEWEST"),

    /*查询用户是否被禁言*/
    QUERY_GROUP_USER_FORBIDDEN(AppParam.S_URL, "QUERY_GROUP_USER_FORBIDDEN"),

    //获取指定聊天室最后一条消息
    LATEST_GROUP_MESSAGE_DATA_QUERY(AppParam.S_URL, "LATEST_GROUP_MESSAGE_DATA_QUERY"),
    /*创建群口令验证*/
    GROUP_CREATE_BEFORE_CODE_VALIDATE(AppParam.S_URL, "GROUP_CREATE_BEFORE_CODE_VALIDATE"),

    /*群组搜索*/
    GROUP_SEARCH(AppParam.S_URL, "GROUP_SEARCH"),


    /*创建群*/
    GROUP_CREATE(AppParam.S_URL, "GROUP_CREATE"),

    /*进入群聊*/
    USER_ENTER_GROUP_OPERATE(AppParam.S_URL, "USER_ENTER_GROUP_OPERATE"),

    /*申请加入群聊*/
    USER_GROUP_JOIN_APPLY(AppParam.S_URL, "USER_GROUP_JOIN_APPLY"),

    /*权限检测*/
    USER_AUTHORITY_OR_GROUP_QUERY(AppParam.S_URL, "USER_AUTHORITY_OR_GROUP_QUERY"),


    ACCOUNT_LOG_QUERY(AppParam.S_URL, "ACCOUNT_LOG_QUERY"),

    //设置群战袍名
    MEDAL_LABEL_APPLY(AppParam.S_URL, "MEDAL_LABEL_APPLY"),

    //战袍详情
    COST_ARMOR_DETAIL_SIGN_QUERY(AppParam.S_URL, "COST_ARMOR_DETAIL_SIGN_QUERY"),

    //群信息界面
    GROUP_CHAT_SET_PAGE_QUERY(AppParam.S_URL, "GROUP_CHAT_SET_PAGE_QUERY"),

    //管理员界面
    GROUP_USER_ROLE_AUTHORIZATION_QUERY(AppParam.S_URL, "GROUP_USER_ROLE_AUTHORIZATION_QUERY"),

    //添加管理员
    GROUP_USER_ROLE_ADD(AppParam.S_URL, "GROUP_USER_ROLE_ADD"),

    //待审核成员
    GROUP_USER_STAY_AUDIT_QUERY(AppParam.S_URL, "GROUP_USER_STAY_AUDIT_QUERY"),

    //管理员开关主题
    GROUP_CHAT_CTL_OPERATE(AppParam.S_URL, "GROUP_CHAT_CTL_OPERATE"),

    //审核页面同意拒绝操作
    USER_MANAGE_GROUP_JOINED_OPERATION(AppParam.S_URL, "USER_MANAGE_GROUP_JOINED_OPERATION"),

    //删除管理员
    GROUP_USER_ROLE_DELETE(AppParam.S_URL, "GROUP_USER_ROLE_DELETE"),


    /*创建私聊*/
    USER_SOLE_INIT(AppParam.S_URL, "USER_SOLE_INIT"),

    /*进入私聊房间--用不到*/
    SOLE_USER_ENTRY_ENTER(AppParam.IM_MAPI_XJQ, "SOLE_USER_ENTRY_ENTER"),

    /*查询私聊消息*/
    SOLE_MESSAGE_QUERY_BY_SOLE_USER_ENTRY(AppParam.IM_MAPI_XJQ, "SOLE_MESSAGE_QUERY_BY_SOLE_USER_ENTRY"),
    /*发送私聊消息*/
    SOLE_USER_MESSAGE_SEND(AppParam.S_URL, "SOLE_USER_MESSAGE_SEND"),

    /*打赏*/
    COIN_REWARD(AppParam.S_URL, "COIN_REWARD"),

    /*我的金币账户信息*/
    COIN_REWARD_CONFIG_QUERY(AppParam.S_URL, "COIN_REWARD_CONFIG_QUERY"),

    /*直接打赏情况下获取indentityId*/
    COIN_REWARD_INIT(AppParam.S_URL, "COIN_REWARD_INIT"),

    /*群聊主题查询*/
    THEME_QUERY(AppParam.S_URL, "THEME_QUERY"),
    /* 私聊消息，群聊通知消息 */
    SOLE_MESSAGE_COUNT_QUERY(AppParam.S_URL, "SOLE_MESSAGE_COUNT_QUERY"),

    //获取我的所有群聊
    USER_JOINED_GROUP_QUERY(AppParam.S_URL, "USER_JOINED_GROUP_QUERY"),

    //评论的回复创建
    COMMENT_REPLY_CREATE(AppParam.S_URL, "COMMENT_REPLY_CREATE"),

    //举报
    BLACK_REPORT_MANAGE(AppParam.S_URL, "BLACK_REPORT_MANAGE"),

    //查询回复
    COMMENT_REPLY_QUERY(AppParam.S_URL, "COMMENT_REPLY_QUERY"),

    //查询回复
    COMMENT_QUERY_BY_COMMENT_ID(AppParam.S_URL, "COMMENT_QUERY_BY_COMMENT_ID"),

    //查询回复
    COMMENT_REPLY_QUERY_POSITION(AppParam.S_URL, "COMMENT_REPLY_QUERY_POSITION"),

    //发表话题
    SUBJECT_CREATE(AppParam.S_URL, "SUBJECT_CREATE"),

    //评论的创建
    COMMENT_CREATE(AppParam.S_URL, "COMMENT_CREATE"),

    //香蕉球发送消息
    GROUP_USER_MESSAGE_SEND(AppParam.S_URL, "GROUP_USER_MESSAGE_SEND"),

    //群消息修改
    GROUP_INFO_MODIFY(AppParam.S_URL, "GROUP_INFO_MODIFY"),//groupId,groupName,notice

    //已加入的群聊集合查询
    USER_JOINED_GROUP_INFO_QUERY(AppParam.S_URL, "USER_JOINED_GROUP_INFO_QUERY"),// curpager

    //GROUP_TOPIC_QUERY群空间
    GROUP_TOPIC_QUERY(AppParam.S_URL, "GROUP_TOPIC_QUERY"),//groupId,curpager

    //发话题的配置
    SUBJECT_CREATE_BEFORE_QUERY(AppParam.S_URL, "SUBJECT_CREATE_BEFORE_QUERY"),

    TEST(AppParam.S_URL, "TEST");

    private String url;

    private String desc;

    JczjURLEnum(String url, String desc) {

        this.url = url;

        this.desc = desc;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getName() {
        return desc;
    }

}
package com.android.banana.groupchat.groupchat.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.banana.BuildConfig;
import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.CouponDetailDialog;
import com.android.banana.commlib.coupon.FetCouponValidateUtils;
import com.android.banana.commlib.coupon.OpenCouponDialog;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.escapeUtils.StringEscapeUtils;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.view.HtmlEmojiTextView;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.android.banana.groupchat.chatenum.GroupTransMessageType;
import com.android.banana.groupchat.groupchat.GroupShirtDetailActivity;
import com.android.banana.groupchat.groupchat.forbidden.ForbiddenActivitty;
import com.android.banana.groupchat.groupchat.widget.GroupShirtMedalDrawable;
import com.android.banana.groupchat.ilistener.ChatAdapterHelperCallback;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.TimelineDecoration;
import com.android.banana.utils.DialogHelper;
import com.android.banana.utils.ListSelectDialog;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.bean.ChatMsgMedalBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mrs on 2017/4/7.
 * //聊天记录,适用于群聊，私聊
 * <p>
 * 1、需要删除，禁言请在{@link ChatAdapterHelper}中 写入，并调用{@link ChatAdapter#setChatAdapterHelper(ChatAdapterHelper)}方法绑定
 * <p>
 * 2、{@link ChatAdapterHelper}中回调方法的解释清查看{@link ChatAdapterHelperCallback}
 * <p>
 * 3、如果需要标签、勋章的显示，请使用并调用{@link ChatAdapter#setUserIdAndRoleCodeMap(Map)} 方法绑定
 */
public class ChatAdapter extends MultiTypeSupportAdapter<ChatMsgBody> implements TimelineDecoration.TimeLineCallback, ChatAdapterHelperCallback {
    public static final int NETWORK_PREVIEW = 3;

    private String mLoginUserId, mUserName, userLogoUrl;
    private String patronUserId;
    private ChatAdapterHelper mAdapterHelper;
    private OpenCouponDialog openCouponDialog;
    private Map<String, String> mUserIdAndRoleCodeMap;
    private String mGroupChatId;
    private String mNickName;

    public ChatAdapter(Context context, ArrayList<ChatMsgBody> list, MultiTypeSupport typeSupport) {
        super(context, list, 0, typeSupport);
        mLoginUserId = LoginInfoHelper.getInstance().getUserId();
        mUserName = LoginInfoHelper.getInstance().getNickName();
        LogUtils.e("kk", "自己的昵称" + mUserName);
        userLogoUrl = LoginInfoHelper.getInstance().getUserLogoUrl();
    }

    @Override
    public void onBindNormalHolder(final ViewHolder holder, final ChatMsgBody msgBody, final int position) {

        int viewType = holder.getItemViewType();
        boolean isSendByMe = TextUtils.equals(mLoginUserId, msgBody.sendUserId);

        setUpNormalItemView(holder, msgBody, isSendByMe);

        if (viewType == R.layout.layout_chat_text_left
                || viewType == R.layout.layout_chat_text_right) {

            HtmlEmojiTextView emojiTv = holder.getView(R.id.contentTv);
            if (emojiTv == null)
                return;
            //长按删除
            CharSequence content = msgBody.getEmojiContent(mContext);
            emojiTv.setText(content);
            emojiTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showListDialog(msgBody, holder);
                    return true;
                }
            });

        } else if (viewType == R.layout.layout_left_img
                || viewType == R.layout.layout_right_img) {

            setUpImageItemView(holder, msgBody);

        } else if (viewType == R.layout.layout_chat_coupon_left
                || viewType == R.layout.layout_chat_coupon_right) {

            setUpCouponItemView(holder, msgBody);

        } else if (viewType == R.layout.layout_chat_coupon_system) {

            setUpSystemCouponItemView(holder, msgBody);

        } else if (viewType == R.layout.layout_chat_coupon_reward) {

            setUpRewardCouponItemView(holder, msgBody);
        } else if (viewType == R.layout.layout_chat_transform_left || viewType == R.layout.layout_chat_transform_right) {

            setUpTransformItemView(holder, msgBody);

        } else if (viewType == R.layout.layout_chat_pk_right || viewType == R.layout.layout_chat_pk_left) {

            setUpPKItemView(holder, msgBody);

        } else if (viewType == R.layout.layout_chat_hand_speed_right || viewType == R.layout.layout_chat_hand_speed_left) {

            setUpHandSpeedItemView(holder, msgBody);

        }
    }

    //直播间极限手速转发
    private void setUpHandSpeedItemView(final ViewHolder holder, final ChatMsgBody msgBody) {
        final ChatMsgBody.Body.Parameters parameters = msgBody.bodies.get(0).parameters;
        if (parameters == null)
            return;

        holder.setOnClickListener(R.id.hand_speed_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.androd.xjq.live_activity");
                intent.putExtra("channelId", parameters.platformObjectId);
                mContext.startActivity(intent);
            }
        });

        holder.setImageByUrl(mContext, R.id.hostPortraitIv, parameters.patronUserlogoUrl, R.drawable.icon_guest_default)
                .setText(R.id.hostNameTv, parameters.patronUserName)
                .setText(R.id.prizeNameTv, parameters.prizeItemName)
                .setText(R.id.prizeNumTv, "共" + parameters.prizeTotalCount + "个")
                .setText(R.id.pk_title, parameters.content);
        MedalLayout medalLayout = holder.getView(R.id.medal_layout);
        ArrayList<ChatMsgMedalBean> patronMedalList = parameters.patronMedalList;
        medalLayout.removeAllViews();
        if (patronMedalList == null || patronMedalList.size() <= 0)
            return;
        for (ChatMsgMedalBean medalBean : patronMedalList) {
            medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(mContext, medalBean.medalConfigCode, medalBean.medalLevelConfigCode));
        }
        holder.setText(R.id.trans_from, "查看详情");
        holder.setOnLongClickListener(R.id.image, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showListDialog(msgBody, holder);
                return true;
            }
        });
    }


    //直播间pk转发
    private void setUpPKItemView(final ViewHolder holder, final ChatMsgBody msgBody) {
        final ChatMsgBody.Body.Parameters parameters = msgBody.bodies.get(0).parameters;
        if (parameters == null)
            return;
        holder.setImageByUrl(mContext, R.id.leftGiftIv, parameters.optionOne_GiftUrl, R.drawable.icon_guest_default)
                .setImageByUrl(mContext, R.id.rightGiftIv, parameters.optionTwo_GiftUrl, R.drawable.icon_guest_default)
                .setText(R.id.themeTv, parameters.title)
                .setText(R.id.leftPointTv, parameters.optionOne_OptionName)
                .setText(R.id.rightPointTv, parameters.optionTwo_OptionName)
                .setText(R.id.leftJoinPeopleNumTv, parameters.optionOne_UserCount + "人")
                .setText(R.id.rightJoinPeopleNumTv, parameters.optionTwo_UserCount + "人");

        holder.setOnClickListener(R.id.pk_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.androd.xjq.live_activity");
                intent.putExtra("channelId", parameters.sourceId);
                mContext.startActivity(intent);
            }
        });
        holder.setText(R.id.trans_from, "查看详情");
        holder.setOnLongClickListener(R.id.image, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showListDialog(msgBody, holder);
                return true;
            }
        });
    }

    private void setUpTransformItemView(final ViewHolder holder, final ChatMsgBody msgBody) {
        final ChatMsgBody.Body.Parameters parameters = msgBody.bodies.get(0).parameters;
        if (parameters == null)
            return;
        GroupTransMessageType enumOf = GroupTransMessageType.safeEnumOf(parameters.objectType);
        GroupTransMessageType turnEnumOf = GroupTransMessageType.safeEnumOf(parameters.subjectType);
        holder.setText(R.id.trans_title, parameters.title)
                .setText(R.id.trans_content, TextUtils.isEmpty(parameters.memo) ? parameters.summary : parameters.memo)
                .setViewVisibility(R.id.trans_title, TextUtils.isEmpty(parameters.title) ? View.GONE : View.VISIBLE);
        DetailsHtmlShowUtils.setHtmlText((TextView) holder.getView(R.id.trans_content), TextUtils.isEmpty(parameters.memo) ? parameters.summary : parameters.memo,
                false, null);
        String articlePageAction = "com.android.xjq.subject_detail_activity";
        String livePageAction = "com.android.xjq.live_wall_activity";
        String transmitPageAction = "com.android.xjq.transmit_detail_activity";
        String targetPageAction = transmitPageAction;
        String logo = parameters.giftUrl;
        int iconRes = R.drawable.image_empty;


        switch (turnEnumOf) {//专门判断跳转详情页的
            case ARTICLE:
            case PERSONAL_ARTICLE:
            case NORMAL:
                targetPageAction = articlePageAction;
                break;
            case XJQ_VIDEO:
                targetPageAction = livePageAction;
                break;
        }

        switch (enumOf) {
            case DEFAULT:
            case NORMAL:
                iconRes = R.drawable.ic_dynmic_speak;
                logo = parameters.imageUrl;
                break;//普通
            case ARTICLE://文章，
            case PERSONAL_ARTICLE:  //个人文章
            case MARK_ATTITUDE_COMMENT://评论被标记态度
            case MARK_ATTITUDE_PERSONAL_ARTICLE://个人文章被标记态度
                iconRes = R.drawable.ic_dynmic_article;
                logo = parameters.imageUrl;
                break;
            case XJQ_VIDEO: //香蕉球视频
                iconRes = R.drawable.ic_trans_dynmic_video;
                break;
            case MARK_ATTITUDE_MSG_SCREEN://弹幕被标记上态度
                iconRes = R.drawable.ic_dynmic_comment;
                break;
            case MARK_ATTITUDE_ROCKETS: //火箭
                iconRes = R.drawable.ic_dynmic_rocket;
                break;
            case EVENT_WIN_PK://PK
                iconRes = R.drawable.ic_dynmic_pk;
                break;
            case EVENT_WIN_CHEER: //助威
                iconRes = R.drawable.ic_dynmic_cheer;
                break;
            case EVENT_SEND_COUPON://发红包
                iconRes = R.drawable.ic_dynmic_coupon;
                break;
            case EVENT_SPONSOR_DRAW://赞助抽奖
                iconRes = R.drawable.ic_dynmic_lucky_draw;
                break;
            case EVENT_REWARD_ANCHOR: //打赏主播
                logo = parameters.giftUrl;
                iconRes = R.drawable.ic_dynamic_gift_default;
                break;
        }
        holder.setText(R.id.trans_from, "By-" + parameters.userName);
        holder.setImageByUrl(mContext, R.id.trans_logo, logo, iconRes);
        final String finalTargetPageAction = targetPageAction;
        holder.setOnClickListener(R.id.transform_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalTargetPageAction);
                //文章详情页,转发详情页 传参
                intent.putExtra("subjectId", parameters.subjectId);

                mContext.startActivity(intent);
            }
        });

        holder.setOnLongClickListener(R.id.transform_layout, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showListDialog(msgBody, holder);
                return true;
            }
        });
    }

    private void setUpNormalItemView(final ViewHolder holder, final ChatMsgBody msgBody, boolean isSendByMe) {
        holder.setImageByUrl(mContext, R.id.portraitIv, isSendByMe ?
                userLogoUrl : msgBody.sendUserLogoUrl, R.drawable.user_default_logo);
        holder.setText(R.id.userName, isSendByMe ? (TextUtils.isEmpty(mNickName) ?
                mUserName : mNickName) : (TextUtils.isEmpty(msgBody.sendUserNickName) ? msgBody.sendUserLoginName : msgBody.sendUserNickName));

        holder.setOnClickListener(R.id.medal_iv1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupShirtDetailActivity.startGroupShirtDetailActivity(mContext, mGroupChatId);
            }
        });

        setUpRoleCode(holder, msgBody);//根据角色  判断标签是否显示
        holder.setOnClickListener(R.id.portraitIv, new JumpUserInfoClick(msgBody.sendUserId, msgBody.groupId,msgBody.getRoleCode()));
        holder.setOnClickListener(R.id.userName, new JumpUserInfoClick(msgBody.sendUserId, msgBody.groupId,msgBody.getRoleCode()));
        if (msgBody.properties == null) {
            holder.setImageDrawable(R.id.medal_iv1, null);
            return;
        }

        List<ChatMsgMedalBean> chatMedalList = msgBody.properties.getChatMedalList();
        if (chatMedalList == null || chatMedalList.size() == 0) {
            holder.setImageDrawable(R.id.medal_iv1, null);
            return;
        }
        ChatMsgMedalBean medalBean = chatMedalList.get(0);
        if (TextUtils.equals("GROUP_VIP_MEDAL", medalBean.medalConfigCode)) {
            String txt = (medalBean.labelInfoList == null || medalBean.labelInfoList.size() == 0) ? "" : medalBean.labelInfoList.get(0).getContent();
            if (TextUtils.isEmpty(txt)) {
                holder.setImageDrawable(R.id.medal_iv1, ContextCompat.getDrawable(mContext, R.drawable.icon_group_shirt_no_txt_bg));
            } else {
                GroupShirtMedalDrawable drawable = new GroupShirtMedalDrawable(txt, mContext.getResources());
                holder.setImageDrawable(R.id.medal_iv1, drawable);
            }
        }

    }

    private class JumpUserInfoClick implements View.OnClickListener {
        private String mUserId;
        private String mGroupId;
        private String mClickObjectRoleCode;

        public JumpUserInfoClick(String userId, String groupId, String clickObjectRoleCode) {
            mUserId = userId;
            mGroupId = groupId;
            mClickObjectRoleCode = clickObjectRoleCode;
        }

        @Override
        public void onClick(View v) {
            if (mAdapterHelper == null) return;
            Intent intent = new Intent("com.android.xjq.userinfo");
            intent.putExtra("userId", mUserId);
            intent.putExtra("groupId", mGroupId);
            intent.putExtra("isAdmin", mAdapterHelper.mIsManager);
            intent.putExtra("isOwner", mAdapterHelper.mIsOwner);
            intent.putExtra("mClickObjectRoleCode", mClickObjectRoleCode);
            mContext.startActivity(intent);
        }
    }

    //图片类型
    private void setUpImageItemView(final ViewHolder holder, final ChatMsgBody msgBody) {

        final ChatMsgBody.Body body = msgBody.bodies.get(0);
        final String imageUrl;
        if (body.parameters == null) {
            imageUrl = body.imageUrl;
        } else {
            imageUrl = body.parameters.url;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            holder.setImageResource(R.id.image, R.drawable.ic_imchat_placholder);
            return;
        }
        holder.setImageByUrl(mContext, R.id.image, imageUrl, R.drawable.ic_imchat_placholder);
        holder.setOnClickListener(R.id.image, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add(imageUrl);
                Intent intent = new Intent(mContext, FullScreenImageGalleryActivity.class);
                intent.putExtra("images", list);
                intent.putExtra("position", 0);
                intent.putExtra("type", NETWORK_PREVIEW);
                mContext.startActivity(intent);
            }
        });
        holder.setOnLongClickListener(R.id.image, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showListDialog(msgBody, holder);
                return true;
            }
        });
    }

    //打赏类型的item
    private void setUpRewardCouponItemView(ViewHolder holder, ChatMsgBody msgBody) {
        String amount = "";
        String receiveUserLoginName = null;
        if (msgBody.bodies != null && msgBody.bodies.size() > 0) {
            ChatMsgBody.Properties properties = msgBody.bodies.get(0).properties;
            if (properties != null) {
                amount = properties.rewardAmount;
                receiveUserLoginName = properties.receiveUserLoginName;
            }
        }
        String sendUserId = msgBody.sendUserId;
        String sender = msgBody.sendUserLoginName;
        TextView conponTv = holder.getView(R.id.couponRecordShowTv);
        if (TextUtils.equals(sendUserId, mLoginUserId)) {
            conponTv.setText(String.format(mContext.getString(R.string.live_score_coupon_fetch_1), receiveUserLoginName));
        } else {
            conponTv.setText(String.format(mContext.getString(R.string.live_score_coupon_fetch_2), sender));
        }

        conponTv.append(SpannableStringHelper.changeTextColor(amount, ContextCompat.getColor(mContext, R.color.yellowred)));
        conponTv.append(mContext.getString(R.string.reward_coin));
    }

    //抢红包系统通知类型
    private void setUpSystemCouponItemView(ViewHolder holder, ChatMsgBody msgBody) {
        if (msgBody.bodies == null || msgBody.bodies.size() <= 0 || msgBody.bodies.get(0) == null)
            return;
        ChatMsgBody.Body.Parameters parameters = msgBody.bodies.get(0).parameters;
        if (parameters == null)
            return;
        String reciver = parameters.receiveUserName;
        String sender = parameters.sendUserName;
        String amount = parameters.amount;
        TextView conponTv = holder.getView(R.id.couponRecordShowTv);
        conponTv.setText(String.format(mContext.getString(R.string.live_score_coupon_fetch), reciver, sender));
        conponTv.append(SpannableStringHelper.changeTextColor(amount, ContextCompat.getColor(mContext, R.color.yellowred)));
        conponTv.append(mContext.getString(R.string.reward_coin));
    }

    //发送 或收到红包类型
    private void setUpCouponItemView(final ViewHolder holder, final ChatMsgBody msgBody) {
        if (msgBody.bodies == null || msgBody.bodies.size() <= 0 || msgBody.bodies.get(0) == null)
            return;
        final ChatMsgBody.Body.Parameters parameters = msgBody.bodies.get(0).parameters;
        if (parameters == null)
            return;
        holder.setText(R.id.coupon_message, StringEscapeUtils.unescapeHtml(parameters.title));
        holder.setOnClickListener(R.id.coupon_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameters == null || mAdapterHelper == null)
                    return;
                getActivity().showProgressDialog();
                mAdapterHelper.fetchCoupon(parameters.groupCouponId, holder.getAdapterPosition());
            }
        });
    }

    /**
     * 根据传进来的 mUserIdAndRoleCodeMap 来判断是什么角色，并显示相应的标签。角色确定后进行赋值，避免多次for循环
     * 也就是说 一个消息体只会去map中被查找一遍
     *
     * @param holder
     * @param msgBody
     */
    private void setUpRoleCode(ViewHolder holder, ChatMsgBody msgBody) {
        try {
            if (TextUtils.isEmpty(msgBody.getRoleCode())) {
                if (mUserIdAndRoleCodeMap != null && mUserIdAndRoleCodeMap.size() > 0) {
                    if (mUserIdAndRoleCodeMap.containsKey(msgBody.sendUserId)) {
                        String userRole = mUserIdAndRoleCodeMap.get(msgBody.sendUserId);
                        boolean isAdmin = TextUtils.equals(userRole, ChatRoomMemberLevelEnum.GROUP_ADMIN.name());
                        boolean isOwner = TextUtils.equals(userRole, ChatRoomMemberLevelEnum.GROUP_OWNER.name());
                        holder.setViewVisibility(R.id.medal_iv, (isAdmin || isOwner) ? View.VISIBLE : View.GONE)
                                .setImageResource(R.id.medal_iv, isOwner ? R.drawable.icon_group_owner : R.drawable.icon_group_admin);
                        if (isAdmin) {
                            msgBody.setRoleCode(ChatRoomMemberLevelEnum.GROUP_ADMIN.name());
                        } else if (isOwner) {
                            msgBody.setRoleCode(ChatRoomMemberLevelEnum.GROUP_OWNER.name());
                        }
                    } else {
                        holder.setViewVisibility(R.id.medal_iv, View.GONE);
                        msgBody.setRoleCode(ChatRoomMemberLevelEnum.NORMAL.name());
                    }
                }
            } else {
                boolean isAdmin = TextUtils.equals(msgBody.getRoleCode(), ChatRoomMemberLevelEnum.GROUP_ADMIN.name());
                boolean isOwner = TextUtils.equals(msgBody.getRoleCode(), ChatRoomMemberLevelEnum.GROUP_OWNER.name());
                if (isAdmin || isOwner) {
                    holder.setViewVisibility(R.id.medal_iv, (isAdmin || isOwner) ? View.VISIBLE : View.GONE)
                            .setImageResource(R.id.medal_iv, isOwner ? R.drawable.icon_group_owner : R.drawable.icon_group_admin);
                } else {
                    holder.setViewVisibility(R.id.medal_iv, View.GONE);
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                LibAppUtil.showTip(mContext, "setUpRoleCode" + e.toString());
        }
    }

    @Override
    public String getMessageTime(int position) {
        if (position < 0 || position >= mDatas.size())
            return "";
        return mDatas.get(position).gmtCreate;
    }

    @Override
    public String getPreMessageTime(int prePosition) {
        return getMessageTime(prePosition);
    }

    @Override
    public boolean isCouponMessage(int position) {
        if (position < 0 || position > mDatas.size())
            return false;
        return TextUtils.equals(mDatas.get(position).typeCode, MessageType.COUPON_NOTIFICATION);
    }

    public void addNewMsg(ChatMsgBody chatMsgBody) {
        mDatas.add(chatMsgBody);
        notifyItemRangeInserted(getItemCount(), 1);
    }

    public void addNewMsg(List<ChatMsgBody> list) {
        int curCount = getItemCount();
        this.mDatas.addAll(list);
        notifyItemRangeInserted(curCount, list.size());
    }

    public ChatMsgBody addNormalMessage(String message, String groupId) {//直播间点过来groupId
        ChatMsgBody chatMsgBody = new ChatMsgBody(groupId, message,
                MessageType.NORMAL,
                TextUtils.isEmpty(mNickName) ? mUserName : mNickName,
                mLoginUserId, mAdapterHelper == null ? ChatRoomMemberLevelEnum.NORMAL.name() : mAdapterHelper.mUserRoleCode);
        notifyItemInserted(getItemCount());
        return chatMsgBody;

    }

    public ChatMsgBody addCouponMessage(String couponTitle, String groupCouponId, String groupId) {
        ChatMsgBody chatMsgBody = new ChatMsgBody(groupId, couponTitle,
                MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT,
                TextUtils.isEmpty(mNickName) ? mUserName : mNickName,
                mLoginUserId,
                groupCouponId,
                mAdapterHelper == null ? ChatRoomMemberLevelEnum.NORMAL.name() : mAdapterHelper.mUserRoleCode);
        notifyItemInserted(getItemCount());
        return chatMsgBody;

    }

    public ChatMsgBody addImageMessage(String filePath, String groupId) {
        ChatMsgBody chatMsgBody = new ChatMsgBody(groupId, filePath,
                MessageType.IMAGE_VIEWABLE,
                TextUtils.isEmpty(mNickName) ? mUserName : mNickName,
                mLoginUserId,
                mAdapterHelper == null ? ChatRoomMemberLevelEnum.NORMAL.name() : mAdapterHelper.mUserRoleCode);
        notifyItemInserted(getItemCount());
        return chatMsgBody;
    }

    @Override
    public void onQueryAvailableCouponResult(int adapterPos, GroupCouponInfoBean groupCouponInfo, JSONObject jsonError) throws JSONException {
        getActivity().closeProgressDialog();
        if (jsonError != null) {
            FetCouponValidateUtils couponValidateUtils = new FetCouponValidateUtils(getActivity(), groupCouponInfo);
            couponValidateUtils.handleFalse(jsonError);
            return;
        }

        if (groupCouponInfo.isOwnAllocated()) {
            CouponDetailDialog dialog = new CouponDetailDialog(mContext, groupCouponInfo.getUniqueId());
            dialog.show();
        } else {
            OpenCouponDialog openCouponDialog = new OpenCouponDialog(mContext, groupCouponInfo);
            openCouponDialog.show();
        }
    }

    private void showListDialog(final ChatMsgBody item, final ViewHolder holder) {
        if (mAdapterHelper == null || !mAdapterHelper.mIsManager || TextUtils.isEmpty(item.id))
            return;
        boolean equals = TextUtils.equals(item.sendUserId, mLoginUserId);
        if (equals) {//是自己的消息直接弹框
            DialogHelper.showListDialog(getActivity().getSupportFragmentManager(), new ListSelectDialog.OnClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    showConfirmDialog(holder.getAdapterPosition(), item.id, item.groupId);
                }
            }, mContext.getString(R.string.delete_content));
        } else {
            mAdapterHelper.checkForbidden(holder.getAdapterPosition(), equals, item.sendUserId, item.sendUserLoginName, item.id, item.groupId);
        }

    }

    @Override
    public void onCheckForbiddenResult(RequestContainer request, final boolean forbidden, JSONObject jsonError) throws JSONException {
        if (jsonError != null) {
            getActivity().operateErrorResponseMessage(jsonError);
            return;
        }
        final int posInAdapter = request.getInt("posInAdapter");
        final boolean sendByMe = request.getBoolean("sendByMe");
        final String sendUserId = request.getString("userId");
        final String sendUserName = request.getString("sendUserName");
        final String msgId = request.getString("id");
        final String groupId = request.getString("groupId");

        DialogHelper.showListDialog(getActivity().getSupportFragmentManager(),
                new ListSelectDialog.OnClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        switch (pos) {
                            case 0:
                                SimpleChatActivity.startSimpleChatActivity(getActivity(), sendUserId, sendUserName, null, groupId, "GROUP");
                                break;
                            case 1:
                                showConfirmDialog(posInAdapter, msgId, groupId);
                                break;
                            case 2:
                                if (!forbidden)
                                    ForbiddenActivitty.start4GagActivity(mContext, sendUserId, groupId);
                                else
                                    mAdapterHelper.cancelForbidden(msgId, sendUserId, groupId);
                                break;
                        }
                    }
                }, "私聊Ta", sendByMe ? mContext.getString(R.string.delete_content) : mContext.getString(R.string.delete_content), mContext.getString(forbidden ? R.string.forbidden_cancel : R.string.forbidden));
    }

    @Override
    public void onCancelForbiddenResult(boolean success, JSONObject jsonError) throws JSONException {
        if (success)
            LibAppUtil.showTip(mContext, mContext.getString(R.string.forbidden_cancel_success));
        else
            getActivity().operateErrorResponseMessage(jsonError);
    }

    @Override
    public void onDeleteMessageResult(int position, JSONObject jsonError) throws JSONException {
        if (jsonError != null) {
            getActivity().operateErrorResponseMessage(jsonError);
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void onRemoveGroupChatMemberResult(int adapterPos, JSONObject jsonError) throws JSONException {

    }

    private void showConfirmDialog(final int position, final String msgId, final String mGroupId) {
        DialogHelper.showConfirmDialog(mContext, mContext.getString(R.string.group_chat_delete_confirm), new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterHelper.deleteMessage(mGroupId, position, msgId);
            }
        }, null);
    }

    public void updateMessageId(int position, String id) {
        mDatas.get(position).id = id;
    }

    //更新图片发送成功后的 消息体first---->是否是推送过来的图片消息的第一条。
    //第一次更新战袍标签
    //第二次更新 图片url
    public void updateMessage(ChatMsgBody newMsgBody, boolean first) {
        int size = mDatas.size();
        for (int i = size - 1; i >= 0; i--) {
            ChatMsgBody chatMsgBody = mDatas.get(i);
            ChatMsgBody.Body body = chatMsgBody.bodies.get(0);
            //更新红包的战袍标签
            if (TextUtils.equals(chatMsgBody.typeCode, MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT)) {
                if (body.parameters != null && TextUtils.equals(body.parameters.groupCouponId, newMsgBody.bodies.get(0).parameters.groupCouponId)) {
                    chatMsgBody.properties.medealInfoList = newMsgBody.properties.medealInfoList;
                    notifyItemChanged(i);
                    break;
                }
            } else {
                if (TextUtils.equals(chatMsgBody.id, newMsgBody.id)) {
                    if (first)
                        chatMsgBody.properties.medealInfoList = newMsgBody.properties.medealInfoList;
                    else
                        body.parameters = newMsgBody.bodies.get(0).parameters;

                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }


    //传递列表消息体角色map集合
    public void setUserIdAndRoleCodeMap(Map<String, String> userIdAndRoleCodeMap) {
        mUserIdAndRoleCodeMap = userIdAndRoleCodeMap;
    }

    //适配器帮助类
    public void setChatAdapterHelper(ChatAdapterHelper chatAdapterHelper) {
        if (chatAdapterHelper == null)
            return;
        this.mAdapterHelper = chatAdapterHelper;
        this.mAdapterHelper.setHelperCallback(this);
    }

    //群聊groupchatid
    public void setGroupChatId(String groupChatId) {
        mGroupChatId = groupChatId;
    }

    //群聊里的昵称
    public void setUserNickName(String nickName) {

        mNickName = nickName;
    }
}

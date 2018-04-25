package com.android.xjq.adapter.live;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.encryptUtils.StringUtils;
import com.android.xjq.R;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.VoteOptionsType;
import com.android.xjq.bean.medal.MedalInfoBean;
import com.android.xjq.frescoUtils.DraweeSpanStringBuilder;
import com.android.xjq.frescoUtils.SimpleDraweeSpanTextView;
import com.android.xjq.model.BubbleColorEnum;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.expandtv.ChatTextView;
import com.android.xjq.view.expandtv.CustomMedalDrawable;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.widget.text.span.BetterImageSpan;

import java.util.ArrayList;
import java.util.List;

import static com.android.xjq.R.id.contentLayout;
import static com.android.xjq.R.id.giftMessageTv;
import static com.android.xjq.R.id.messageTv;
import static com.android.xjq.model.BubbleColorEnum.NORMAL_BUBBLE;
import static com.android.xjq.model.live.LiveCommentMessageTypeEnum.GIFTCORE_GIFT_GIVE_TEXT;

/**
 * Created by lingjiu on 2017/7/5.
 */

public class LiveCommentAdapter2 extends MultiTypeSupportAdapter<LiveCommentBean> {

    public LiveCommentAdapter2(Context context, ArrayList<LiveCommentBean> list, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
    }

    public void setHasBlackBubble(boolean hasBlackBubble) {
        isHasBlackBubble = hasBlackBubble;
    }

    private boolean isHasBlackBubble;

    @Override
    public void onBindNormalHolder(ViewHolder holder, LiveCommentBean item, int position) {
        int itemViewType = holder.getItemViewType();

        if (isHasBlackBubble) {
            holder.setBackgroundRes(contentLayout, R.drawable.shape_chat_gray_bg);
            holder.setTextColor(messageTv, Color.WHITE);
        } else {
            holder.setTextColor(messageTv, Color.BLACK);
        }
        holder.setBackgroundRes(R.id.itemLayout, R.color.transparent);

        LiveCommentBean liveCommentBean = mDatas.get(position);

        if (R.layout.item_live_comment_message == itemViewType) {
            if (isHasBlackBubble) {
                holder.setTextColor(giftMessageTv, Color.WHITE);
            } else {
                holder.setTextColor(giftMessageTv, Color.BLACK);
            }
            holder.setViewVisibility(R.id.messageTv, View.GONE);
            holder.setViewVisibility(giftMessageTv, View.GONE);
            holder.setViewVisibility(R.id.bubbleTv, View.GONE);
            switch (LiveCommentMessageTypeEnum.safeValueOf(liveCommentBean.getType())) {
                case NORMAL:
                case TEXT:
                    holder.setViewVisibility(R.id.messageTv, View.VISIBLE);
                    setTextMessage(liveCommentBean, holder);
                    break;
                case GIFTCORE_GIFT_GIVE_TEXT:
                    holder.setViewVisibility(giftMessageTv, View.VISIBLE);
                    setGiftShow(liveCommentBean, holder);
                    break;
                case SPECIAL_EFFECT_ENTER_NOTICE:
                    holder.setViewVisibility(R.id.messageTv, View.VISIBLE);
                    setSpecialEffectShow(liveCommentBean, holder);
                    break;
                case DECREE_USER_PRIZED_TEXT:
                    holder.setViewVisibility(R.id.messageTv, View.VISIBLE);
                    setDecreeUserPrizedShow(liveCommentBean, holder);
                    break;
                case COUPON_CREATE_SUCCESS_NOTICE_TEXT:
                    holder.setViewVisibility(R.id.messageTv, View.VISIBLE);
                    setCouponMessage(liveCommentBean, holder);
                    break;
            }

        }
    }

    private void setDecreeUserPrizedShow(LiveCommentBean liveCommentBean, ViewHolder holder) {
        TextView messageTv = holder.getView(R.id.messageTv);
        String senderName = liveCommentBean.getSenderName();
        SpannableStringBuilder ssb = new SpannableStringBuilder("恭喜 ");
        ssb.append(SpannableStringHelper.changeTextColor(senderName, Color.parseColor("#fe6969")));
        String totalAmount = liveCommentBean.getContent().getBody().getTotalAmount();
        ssb.append(" 从圣旨中抢到了" + totalAmount + "礼金红包");
        messageTv.setText(ssb);
    }

    private void setCouponMessage(LiveCommentBean liveCommentBean, ViewHolder holder) {
        if (!isHasBlackBubble) {
            holder.setBackgroundRes(R.id.itemLayout, R.color.normal_bg2);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString("图片");
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_home_coupon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.8), (int) (drawable.getIntrinsicHeight() * 0.8));
        ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        ssb.append(liveCommentBean.getSenderName());
        ssb.append("发了一个");
        ssb.append(SpannableStringHelper.changeTextColor(liveCommentBean.getContent().getBody().getCouponType(), Color.parseColor("#fe6969")));
        holder.setText(R.id.messageTv, ssb);
    }

    private void setSpecialEffectShow(LiveCommentBean liveCommentBean, ViewHolder holder) {
        TextView messageTv = holder.getView(R.id.messageTv);
        String senderName = liveCommentBean.getSenderName();
        SpannableStringBuilder ssb = new SpannableStringBuilder("欢迎 ");
        ssb.append(SpannableStringHelper.changeTextColor(senderName, Color.parseColor("#fe6969")));
        ssb.append(" 进入直播间");
        messageTv.setText(ssb);
    }

    private void setGiftShow(LiveCommentBean liveCommentBean, ViewHolder holder) {
        SimpleDraweeSpanTextView giftMessageTv = holder.getView(R.id.giftMessageTv);
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        DraweeSpanStringBuilder ssb = new DraweeSpanStringBuilder();
        SpannableStringBuilder giftDescSpannable = new SpannableStringBuilder();
        String name = HhsUtils.splitString(liveCommentBean.getSenderName(), 14);
        giftDescSpannable.append(SpannableStringHelper.changeTextColor(name, Color.parseColor("#30a9ef")));
        giftDescSpannable.append(" 送出");
        giftDescSpannable.append(SpannableStringHelper.changeTextColor(body.getTotalCount() + "个", Color.parseColor("#fe6969")));
        giftDescSpannable.append(body.getGiftConfigName() + " ");
        String tag = "[图片]";
        ssb.append(giftDescSpannable);
        ssb.append(tag);
        //礼物规则生效,并且价格超过配置金额,要显示多少连击
        if (liveCommentBean.isRuleEffect() && !liveCommentBean.isShowFloatingLayer() && Integer.valueOf(body.getDoubleHit()) > 1) {
            ssb.append(SpannableStringHelper.changeTextColor(body.getDoubleHit() + "连击", Color.parseColor("#fe6969")));
        }
        DraweeHierarchy draweeHierarchy = GenericDraweeHierarchyBuilder.newInstance(mContext.getResources())
                //.setPlaceholderImage(new ColorDrawable(Color.RED))
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                .build();

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(liveCommentBean.getContent().getBody().getGiftImageUrl()))
                .build();

        ssb.setImageSpan(
                mContext,
                draweeHierarchy,
                draweeController,
                giftDescSpannable.length(),
                giftDescSpannable.length() + tag.length() - 1,
                LibAppUtil.dip2px(mContext, 30),
                LibAppUtil.dip2px(mContext, 30),
                false,
                BetterImageSpan.ALIGN_CENTER);
        giftMessageTv.setDraweeSpanStringBuilder(ssb);

    }


    private void setTextMessage(LiveCommentBean liveCommentBean, ViewHolder holder) {
        ChatTextView messageTv = holder.getView(R.id.messageTv);
        ChatTextView bubbleTv = holder.getView(R.id.bubbleTv);
        LiveCommentBean.UserPropertiesBean properties = liveCommentBean.getProperties();
        int nameColor;
        if (TextUtils.equals(VoteOptionsType.SUPPORT_HOME_TEAM, properties.getUserVotedContent())) {
            nameColor = ContextCompat.getColor(mContext, R.color.light_red7);
        } else if (TextUtils.equals(VoteOptionsType.SUPPORT_GUEST_TEAM, properties.getUserVotedContent())) {
            nameColor = ContextCompat.getColor(mContext, R.color.light_blue3);
        } else {
            nameColor = ContextCompat.getColor(mContext, R.color.colorTextG2);
        }

        List<String> horseList = properties.getHorseList();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString userName = SpannableStringHelper.changeTextColor(liveCommentBean.getSenderName() + " : ", nameColor);
        String text = liveCommentBean.getContent().getText();
        if (StringUtils.isBlank(text)) {
            text = "";
        }
        //添加马甲、主客队、房管
        SpannableString clothesSpan = getYellowClothes(horseList);
        ssb.append(clothesSpan);
        //添加领域勋章
        List<MedalInfoBean> medalInfoBeanList = properties.getMedalInfoBeanList();
        if (medalInfoBeanList != null) {
            for (MedalInfoBean medalInfoBean : medalInfoBeanList) {
                int medalResourceId = SubjectMedalEnum.getMedalResourceId(mContext,
                        medalInfoBean.getMedalConfigCode(), medalInfoBean.getMedalLevelConfigCode());
                ssb.append(getMedalSpanstr(medalResourceId));
            }
        }
        //名字
        ssb.append(userName);

       /* SpannableString levelSpan = null;
        if (!TextUtils.isEmpty(properties.getMedalLevelCode())) {
            int levelResId = UIUtils.getResIdByMixedName(mContext, properties.getMedalLevelCode());
            levelSpan = getLevelSpanstr(properties.getMedalName(), levelResId);
            ssb.append(levelSpan);
        }
        ssb.append(userName);*/

        //如果有气泡,内容超出当前行则换至下一行加背景,内容与名字分开,具体见效果图;如果无气泡则不需要控制换行
        BubbleColorEnum bubbleColor = BubbleColorEnum.safeValueOf(liveCommentBean.getContent().getFontColor());
        if (NORMAL_BUBBLE != bubbleColor && !isHasBlackBubble) {
            bubbleTv.setVisibility(View.VISIBLE);
            bubbleTv.setEmojiText(text, ChatTextView.EmojiType.SCALE_WITH_TXT);
            bubbleTv.setBackgroundResource(bubbleColor.getResId());
            holder.setTextColor(R.id.bubbleTv, Color.parseColor(bubbleColor.getTextColor()));
        } else {
            ssb.append(text);
        }
        //ssb.append(new SpannableString("<img src='" + properties.getUserlevelImgUrl() + "'>"));
        //int index = levelSpan == null ? clothesSpan.length() : clothesSpan.length() + levelSpan.length();
        messageTv.setSpannableString(ssb,
                null,
                0,
                ChatTextView.EmojiType.SCALE_WITH_TXT);
    }

    private SpannableString getMedalSpanstr(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString ssp = new SpannableString("图片");
        ssp.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ssp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssp;
    }

    private SpannableString getLevelSpanstr(String drawtxt, int resId) {
        CustomMedalDrawable drawable = new CustomMedalDrawable(drawtxt, resId, mContext.getResources());
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString ssp = new SpannableString("图片");
        ssp.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ssp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssp;
    }

    private int getResId(String level) {
        String resName = "icon_fans_medal_" + level + "";
        int resId = mContext.getResources().getIdentifier(resName.toLowerCase(), "drawable", mContext.getPackageName());
        return resId;
    }

    public boolean judgeIsFilterSameGroupGift(LiveCommentBean liveCommentBean) {
        if (GIFTCORE_GIFT_GIVE_TEXT.equals(LiveCommentMessageTypeEnum.safeValueOf(liveCommentBean.getType()))) {
            LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
            //自己发的礼物不用匹配
            if (body.isOwnGift()) return false;
            //取近30条数据匹配
            for (int i = mDatas.size() - 30 > 0 ? mDatas.size() - 30 : 0; i < mDatas.size(); i++) {
                if (GIFTCORE_GIFT_GIVE_TEXT.equals(LiveCommentMessageTypeEnum.safeValueOf(mDatas.get(i).getType()))) {
                    LiveCommentBean.ContentBean.BodyBean body2 = mDatas.get(i).getContent().getBody();
                    if (body2.isOwnGift()) {
                        continue;
                    }
                    if (body.getGiftConfigId().equals(body2.getGiftConfigId()) &&
                            body.getDoubleHitGroupNo().equals(body2.getDoubleHitGroupNo()) &&
                            Integer.valueOf(body.getDoubleHit()) < Integer.valueOf(body2.getDoubleHit())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置黄马甲、主客队、房管
     *
     * @param horseList
     * @return
     */
    public SpannableString getYellowClothes(List<String> horseList) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (horseList == null || horseList.size() == 0) return new SpannableString(ssb);
        //管家
        if (horseList.contains("CHANNEL_AREA_MANAGER")) {
            addIcon(ssb, R.drawable.icon_room_manager_tag);
        }
        //主客队
        if (horseList.contains("SUPPORT_HOME_TEAM")) {
            addIcon(ssb, R.drawable.icon_home_team_tag);
        } else if (horseList.contains("SUPPORT_GUEST_TEAM")) {
            addIcon(ssb, R.drawable.icon_guest_team_tag);
        }
        if (horseList.contains("CTL_YELLOW_HORSE")) {
            addIcon(ssb, R.drawable.icon_control_clothes);
        }
        if (horseList.contains("HONOR_GREEN_HORSE")) {
            addIcon(ssb, R.drawable.icon_green_horse);
        }
        if (horseList.contains("HONOR_BLUE_HORSE")) {
            addIcon(ssb, R.drawable.icon_blue_horse);
        }
        return new SpannableString(ssb);
    }

    public void addIcon(SpannableStringBuilder ssb, int iconId) {
        SpannableString str1 = new SpannableString("黄 ");
        Drawable d = ContextCompat.getDrawable(mContext, iconId);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        str1.setSpan(span, 0, str1.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(str1);
    }
}
